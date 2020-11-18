package com.huashengfu.StemCellsManager.activity.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.utils.StringUtils;
import com.tencent.liteav.demo.play.SuperPlayerConst;
import com.tencent.liteav.demo.play.SuperPlayerGlobalConfig;
import com.tencent.liteav.demo.play.SuperPlayerModel;
import com.tencent.liteav.demo.play.SuperPlayerView;
import com.tencent.rtmp.TXLiveConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

// 视频播放
public class VideoPlayActivity extends BaseActivity {

    @BindView(R.id.superVodPlayerView)
    SuperPlayerView mSuperPlayerView;

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.
                FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_video_play);

        unbinder = ButterKnife.bind(this);

        // 播放器配置
        SuperPlayerGlobalConfig prefs = SuperPlayerGlobalConfig.getInstance();
        // 开启悬浮窗播放
        prefs.enableFloatWindow = true;
        // 设置悬浮窗的初始位置和宽高
        SuperPlayerGlobalConfig.TXRect rect = new SuperPlayerGlobalConfig.TXRect();
        rect.x = 0;
        rect.y = 0;
        rect.width = 810;
        rect.height = 540;
        prefs.floatViewRect = rect;
        // 播放器默认缓存个数
        prefs.maxCacheItem = 5;
        // 设置播放器渲染模式
        prefs.enableHWAcceleration = true;
        prefs.renderMode = TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION;

        String url = getIntent().getStringExtra(Constants.Tag.data);
        if(!StringUtils.isNullOrBlank(url)){
            // 通过URL方式的视频信息配置
            SuperPlayerModel model2 = new SuperPlayerModel();
            model2.title  = getResources().getString(R.string.app_name);
            model2.url = url;
            // 开始播放
            mSuperPlayerView.playWithModel(model2);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 重新开始播放
        if (mSuperPlayerView.getPlayState() == SuperPlayerConst.PLAYSTATE_PLAYING) {
            mSuperPlayerView.onResume();
            if (mSuperPlayerView.getPlayMode() == SuperPlayerConst.PLAYMODE_FLOAT) {
                mSuperPlayerView.requestPlayMode(SuperPlayerConst.PLAYMODE_WINDOW);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 停止播放
        if (mSuperPlayerView.getPlayMode() != SuperPlayerConst.PLAYMODE_FLOAT) {
            mSuperPlayerView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 释放
        mSuperPlayerView.release();
        if (mSuperPlayerView.getPlayMode() != SuperPlayerConst.PLAYMODE_FLOAT) {
            mSuperPlayerView.resetPlayer();
        }

        unbinder.unbind();
    }


    //    private SuperVideoPlayer mSuperVideoPlayer;
//    private ImageView mPlayBtnView;
//    private String videoUrl;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_video_play);
//        hideBottomUIMenu();//隐藏虚拟按键，否则全屏时虚拟按键会影响视频播放进度条的调整。
//        checkPermission();//检查权限
//        getWindow().addFlags(WindowManager.LayoutParams.
//                FLAG_KEEP_SCREEN_ON);
//        initView();
//    }
//
//    /**
//     * 初始化控件
//     */
//    private void initView() {
//        videoUrl = "http://www.jmzsjy.com/UploadFile/微课/地方风味小吃——宫廷香酥牛肉饼.mp4";
//        mSuperVideoPlayer = (SuperVideoPlayer) findViewById(R.id.video_player_item);
//        //创建主播放器
//        mSuperVideoPlayer.setVideoPlayCallback(mVideoPlayCallback);
//        //播放按钮
//        mPlayBtnView = (ImageView) findViewById(R.id.play_btn);
//        mPlayBtnView.setOnClickListener(this);
//        playVideo();
//    }
//
//    /**
//     * 检查权限
//     */
//    private void checkPermission() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            List<String> permissions = new ArrayList<>();
//            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
//                permissions.add(Manifest.permission.CAMERA);
//            }
//            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            }
//            if (permissions.size() != 0) {
//                ActivityCompat.requestPermissions(this, permissions.toArray(new String[0]), 100);
//            }
//        }
//    }
//
//
//    /**
//     * 播放视频
//     */
//    private void playVideo() {
//        mPlayBtnView.setVisibility(View.GONE);
//        mSuperVideoPlayer.setVisibility(View.VISIBLE);
//        mSuperVideoPlayer.setAutoHideController(false);
//        Video video = new Video();
//        VideoUrl videoUrl1 = new VideoUrl();
//        videoUrl1.setFormatName("1080P");//视频格式名称，例如高清，标清，720P等等
//        videoUrl1.setFormatUrl(videoUrl);//视频Url
//        videoUrl1.setIsOnlineVideo(false);//设置是否在线播放
//        ArrayList<VideoUrl> arrayList1 = new ArrayList<>();
//        arrayList1.add(videoUrl1);
//        video.setVideoName("测试视频一");
//        video.setVideoUrl(arrayList1);
//        ArrayList<Video> videoArrayList = new ArrayList<>();
//        videoArrayList.add(video);
//        mSuperVideoPlayer.loadMultipleVideo(videoArrayList, 0, 0, 0);//按照指定的格式，指定的进度播放视频列表中指定的视频。
//    }
//
//    //视频播放中的回调处理
//    private SuperVideoPlayer.VideoPlayCallbackImpl mVideoPlayCallback = new SuperVideoPlayer.VideoPlayCallbackImpl() {
//        @Override
//        public void onCloseVideo() {
//            mSuperVideoPlayer.onDestroy();
//            mPlayBtnView.setVisibility(View.VISIBLE);
//            mSuperVideoPlayer.setVisibility(View.GONE);
//            resetPageToPortrait();
//        }
//
//        @Override
//        public void onSwitchPageType() {
//            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);
//            } else {
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                mSuperVideoPlayer.setPageType(MediaController.PageType.EXPAND);
//            }
//        }
//
//        @Override
//        public void onPlayFinish() {
//            mPlayBtnView.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        public void onBack() {
//            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);
//            } else {
//                finish();
//            }
//        }
//
//        @Override
//        public void onLoadVideoInfo(VodRspData data) {
//        }
//    };
//
//    /***
//     * 恢复屏幕至竖屏
//     */
//    private void resetPageToPortrait() {
//        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.play_btn) {
//            playVideo();
//
//        }
//    }
//
//    /**
//     * 隐藏虚拟按键，并且全屏
//     */
//    protected void hideBottomUIMenu() {
//        //隐藏虚拟按键，并且全屏
//        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
//            View v = this.getWindow().getDecorView();
//            v.setSystemUiVisibility(View.GONE);
//        } else if (Build.VERSION.SDK_INT >= 19) {
//            //for new api versions.
//            View decorView = getWindow().getDecorView();
//            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
//                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
//            decorView.setSystemUiVisibility(uiOptions);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        if (mSuperVideoPlayer != null) {
//            mSuperVideoPlayer.onDestroy();
//        }
//        super.onDestroy();
//    }
}
