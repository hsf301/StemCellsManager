package com.tencent.liteav.demo.play.protocol;

import com.tencent.liteav.demo.play.bean.TCPlayImageSpriteInfo;
import com.tencent.liteav.demo.play.bean.TCPlayKeyFrameDescInfo;
import com.tencent.liteav.demo.play.bean.TCResolutionName;
import com.tencent.liteav.demo.play.bean.TCVideoQuality;

import java.util.List;

/**
 * 视频信息协议接口
 */
public interface IPlayInfoProtocol {
    /**
     * 发送视频信息协议网络请求
     *
     * @param callback 协议请求回调
     */
    void sendRequest(IPlayInfoRequestCallback callback);

    /**
     * 中途取消请求
     */
    void cancelRequest();

    /**
     * 获取视频播放url
     *
     * @return 视频播放url字符串
     */
    String getUrl();

    /**
     * 获取加密视频播放url
     *
     * @return url字符串
     */
    String getEncyptedUrl(PlayInfoConstant.EncyptedUrlType type);

    /**
     * 获取加密token
     *
     * @return token字符串
     */
    String getToken();

    /**
     * 获取视频名称
     *
     * @return 视频名称字符串
     */
    String getName();

    /**
     * 获取雪碧图信息
     *
     * @return 雪碧图信息对象
     */
    TCPlayImageSpriteInfo getImageSpriteInfo();

    /**
     * 获取关键帧信息
     *
     * @return 关键帧信息数组
     */
    List<TCPlayKeyFrameDescInfo> getKeyFrameDescInfo();

    /**
     * 获取画质信息
     *
     * @return 画质信息数组
     */
    List<TCVideoQuality> getVideoQualityList();

    /**
     * 获取默认画质
     *
     * @return 默认画质信息对象
     */
    TCVideoQuality getDefaultVideoQuality();

    /**
     * 获取视频画质别名列表
     *
     * @return 画质别名数组
     */
    List<TCResolutionName> getResolutionNameList();

    /**
     * 透传内容
     *
     * @return 透传内容
     */
    String getPenetrateContext();
}
