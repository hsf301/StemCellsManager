package com.huashengfu.StemCellsManager.activity.video;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.esay.ffmtool.FfmpegTool;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.view.video.Adapter;
import com.huashengfu.StemCellsManager.view.video.Data;
import com.huashengfu.StemCellsManager.view.video.RangeBar;
import com.huashengfu.StemCellsManager.view.video.UIUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by ZBK on 2017/8/11.
 * Describe:仿微信10秒小视频编辑
 */

public class EsayVideoEditActivity extends BaseActivity implements RangeBar.OnRangeBarChangeListener {

    private final int IMAGE_NUM=30;//每一屏图片的数量

    @BindView(R.id.tv_time)
    TextView tvTime;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.rangeBar)
    RangeBar rangeBar;
    @BindView(R.id.fram)
    FrameLayout fram;
    @BindView(R.id.uVideoView)
    VideoView uVideoView;
    @BindView(R.id.tv_size)
    TextView tvSize;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.btn_save)
    Button btnSave;


    private String videoPath;
//    private String parentPath;
    private long videoTime;
    private Adapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private int imagCount=0;//整个视频要解码图片的总数量
    FfmpegTool ffmpegTool;

    private int firstItem=0;//recycleView当前显示的第一项
    private int lastItem=0;//recycleView当前显示的最后一项
    private int leftThumbIndex=0;//滑动条的左端
    private int rightThumbIndex=IMAGE_NUM;//滑动条的右端
    private int startTime=0,endTime=IMAGE_NUM;//裁剪的开始、结束时间
//    private String videoResutlDir;//视频裁剪结果的存放目录
//    private String videoResutl;
    ExecutorService executorService = Executors.newFixedThreadPool(3);
    private String parentPath;

    private String tmpFile;

    private long videoSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);

        ButterKnife.bind(this);

        videoPath = getIntent().getStringExtra(Constants.Tag.data);
        Log.i("onCreate","videoPath:"+videoPath);
        if (!new File(videoPath).exists()) {
            Toast.makeText(this, "视频文件不存在", Toast.LENGTH_LONG).show();
            finish();
        }

//        String str="temp"+System.currentTimeMillis()/1000;
//        parentPath= getExternalStorageDirectory().getAbsolutePath()
//                +File.separator+"test"+File.separator+str+File.separator;
//
//        videoResutlDir= getExternalStorageDirectory().getAbsolutePath()
//                +File.separator+"test"+File.separator+"clicp";

        parentPath = Constants.Path.clipsPath + System.currentTimeMillis()/1000;

        File file=new File(parentPath);
        if(!file.exists()){
            file.mkdirs();
        }

        videoSize = new File(videoPath).length();

        rangeBar.setmTickCount(IMAGE_NUM+1);
        videoTime= UIUtil.getVideoDuration(videoPath);

        Log.i("onCreate","videoTime:"+videoTime);
        ffmpegTool=FfmpegTool.getInstance(this);
        ffmpegTool.setImageDecodeing(new FfmpegTool.ImageDecodeing() {
            @Override
            public void sucessOne(String s, int i) {
                adapter.notifyItemRangeChanged(i,1);
            }
        });

        initView();
        initData();
    }

    private void initView(){
        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerview.setLayoutManager(linearLayoutManager);

        adapter=new Adapter(this,getDataList(videoTime));
        adapter.setParentPath(parentPath);
        adapter.setRotation(UIUtil.strToFloat(UIUtil.getVideoInf(videoPath)));
        recyclerview.setAdapter(adapter);
        recyclerview.addOnScrollListener(onScrollListener);

        setTime(rangeBar.getRightIndex() - rangeBar.getLeftIndex());

        rangeBar.setOnRangeBarChangeListener(this);//设置滑动条的监听
        uVideoView.setVideoPath(videoPath);
        uVideoView.start();
    }

    private void setTime(int time){
        double size = (double) videoSize / (double) videoTime * time;
        size = size / 1024d;

        tvTime.setText(time + "s ");

        BigDecimal b = new BigDecimal(size);
        //保留2位小数
        double result = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        tvSize.setText(result + "Mb");

        if(result > Constants.videoMaxSize){
            tvSize.setTextAppearance(R.style.TextRed12Sp);
            tvTips.setText("请保持截取后文件大小在10Mb以内");
            btnSave.setEnabled(false);
        }else{
            tvSize.setTextAppearance(R.style.TextGreen12Sp);
            tvTips.setText("");
            btnSave.setEnabled(true);
        }
    }
    /**
     * 第一次解码，先解码两屏的图片
     */
    private void initData(){
        File parent=new File(parentPath);
        if (!parent.exists()){
            parent.mkdirs();
        }
        runImagDecodTask(0,2 * IMAGE_NUM);
    }

    @OnClick(R.id.btn_save)
    public void onClick(View view){
        uVideoView.stopPlayback();
        File file=new File(Constants.Path.videoPath);
        if (!file.exists()){
            file.mkdirs();
        }

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                String video=Constants.Path.videoPath + System.currentTimeMillis()/1000 + ".mp4";

                ffmpegTool.clipVideo(videoPath, video, startTime, endTime - startTime, 2, new FfmpegTool.VideoResult() {
                    @Override
                    public void clipResult(int i, String s, String s1, boolean b, int i1) {
                        Log.i("clipResult","clipResult:"+s1);

                        if(!b){
                            showMessage(R.string.error_video_cut_save);
                        }else{
                            Intent intent = new Intent();
                            intent.putExtra(Constants.Tag.data, s1);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                });
            }
        });
    }


    /**
     * 根据视频的时长，按秒分割成多个data先占一个位置
     * @return
     */
    public  List<Data> getDataList(long videoTime){
        List<Data> dataList=new ArrayList<>();
        int seconds= (int) (videoTime/1000);
        for (imagCount=0;imagCount<seconds;imagCount++){
            dataList.add(new Data(imagCount,"temp"+imagCount+".jpg"));
        }
        return dataList;
    }

    /**
     * rangeBar 滑动改变时监听，重新计算时间
     * @param rangeBar
     * @param leftThumbIndex
     * @param rightThumbIndex
     */
    @Override
    public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex, int rightThumbIndex) {
        Log.i("onIndexChange","leftThumbIndex:"+leftThumbIndex+"___rightThumbIndex:"+rightThumbIndex);
        this.leftThumbIndex=leftThumbIndex;
        this.rightThumbIndex=rightThumbIndex;
        calStartEndTime();
    }

    /**
     * 计算开始结束时间
     */
    private void calStartEndTime(){
        int duration=rightThumbIndex-leftThumbIndex;
        startTime=firstItem+leftThumbIndex;
        endTime=startTime+duration;
        //此时可能视频已经结束，若已结束重新start
        if (!uVideoView.isPlaying()){
            uVideoView.start();
        }
        //把视频跳转到新选择的开始时间
        uVideoView.seekTo(startTime*1000);
        setTime(duration);
    }


    private RecyclerView.OnScrollListener onScrollListener=new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.i("onScrollStateChanged","onScrollStateChanged :"+newState);
            if (newState==RecyclerView.SCROLL_STATE_IDLE){
                firstItem=linearLayoutManager.findFirstVisibleItemPosition();
                lastItem=linearLayoutManager.findLastVisibleItemPosition();
                List<Data> dataList=adapter.getDataList();
                for(int i=firstItem;i<=lastItem;i++){
                    if (!UIUtil.isFileExist(parentPath + dataList.get(i).getImageName())){
                        Log.i("onScrollStateChanged","not exist :"+i);
                        runImagDecodTask(i,lastItem-i+1);
                        break;
                    }
                }
            }
            calStartEndTime();
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    /**
     * 运行一个图片的解码任务
     * @param start 解码开始的视频时间 秒
     * @param count 一共解析多少张
     */
    private void runImagDecodTask(final int start,final int count){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                ffmpegTool.decodToImageWithCall(videoPath, parentPath, start, count);
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){//获取到图片总的显示范围的大小后，设置每一个图片所占有的宽度
            adapter.setImagWidth(rangeBar.getMeasuredWidth()/IMAGE_NUM);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        uVideoView.pause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        uVideoView.stopPlayback();
        //最后不要忘了删除这个临时文件夹 parentPath
        //不然时间长了会在手机上生成大量不用的图片，该activity销毁后这个文件夹就用不到了
        //如果内存大，任性不删也可以
        File file = new File(parentPath);
        if(file.exists())
            file.delete();
    }
}
