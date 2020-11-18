package com.huashengfu.StemCellsManager;

import android.content.Context;
import android.os.Environment;

import com.blankj.utilcode.util.SPUtils;
import com.huashengfu.StemCellsManager.db.DbHandler;
import com.huashengfu.StemCellsManager.entity.User;
import com.huashengfu.StemCellsManager.entity.service.QA;

import java.io.File;

public class Constants {

    public static final String BaseUrl = BuildConfig.baseUrl;



    public static boolean isForeground = false;

    public static final int titleMaxLength = 20;
    public static final int contentMaxLength = 300;
    public static final int promotionContentMaxLength = 100;
    public static final int goodsContentMaxLength = 100;
    public static final int replyMaxLength = 100;
    public static final int promotionMaxLength = 100;
    public static final int flagMaxLength = 6;
    public static final int flagDiscountMaxLength = 10;
    public static final int flagMax = 3;
    public static final int flagDiscountMax = 2;
    // 视频文件大小，单位MB
    public static final double videoMaxSize = 10d;

    public static final long photoMaxSize = 2l * 1000l * 1000l;

    public static class Log {
        public static final String Log = "scmlog";
    }

    public static User getLastLoginUser(Context context){
        DbHandler dbHandler = DbHandler.getInstance(context);
        User user = dbHandler.getUser(SPUtils.getInstance().getString(Tag.lastLoginUser));
        return user;
    }

    public static class Path {
        public static final String basePath = Environment.getExternalStorageDirectory().toString() + File.separator + Constants.class.getPackage().getName() + File.separator;
        public static final String imgPath = basePath + "img" + File.separator;
        public static final String videoPath = basePath + "video" + File.separator;
        public static final String clipsPath = basePath + videoPath + "clips" + File.separator;
    }

    public static class Request {
        public static final int SelectLocation = 101;
        public static final int CameraWithData = 102;
        public static final int SelectPhotoFromAlbum = 103;
        public static final int SelectCity = 104;
        public static final int SelectVideo = 105;
        public static final int CutVideo = 106;
        public static final int SelectAddress = 106;
        public static final int SelectGoodsType = 107;
        public static final int SelectGoodsCommoditySpecifications = 108;
        public static final int SelectGoodsParameters = 109;
    }

    public static class Code {
        /**
         * token失效
         */
        public static final int TOKEN_INVALID = 401;
        /**
         * 操作成功
         */
        public static final int SUCCESS_CODE = 200;
        /**
         * 参数检验失败
         */
        public static final int VALIDATE_FAILED = 404;
        /**
         * 密码错误
         */
        public static final int PWD_FAILED = 402;
        /**
         * 没有相关权限
         */
        public static final int FORBIDDEN = 403;

    }
    
    public static class Type {
        public static class Interaction {
            //服务咨询
            public static final int fwzx = 1;
            //服务问答
            public static final int fwwd = 2;
//            //商品咨询
//            public static final int spzx = 3;
            //动态评论
            public static final int dtpl = 3;
            //活动报名
            public static final int hdbm = 4;
//            //活动咨询
//            public static final int hdzx = 6;
        }

        public static class Detail {
            //图片-PIC,文本-TEXT,视频-VIDEO
            public static final String pic = "PIC";
            public static final String text = "TEXT";
            public static final String video = "VIDEO";
        }

        public static class Comment {
            //1服务报名
            public static final int ServiceRegistration = 1;
            //2服务问答
            public static final int ServiceQA = 2;
            //3动态评论
            public static final int DynamicComment = 3;
            //4活动报名
            public static final int ActivityRegistration = 4;
        }

        public static class CustomerService {
            //1商品
            public static final int goods = 1;
            //2服务
            public static final int service = 2;
            //3活动
            public static final int activity = 3;
        }
    }

    public static class Status {
        public static class GoodsOrders {
            //待发货
            public static final int toBeDelivered = 1;
            //待确认
            public static final int toBeConfirmed = 2;
            //已完成
            public static final int completed = 3;
        }

        public static class User {
            // 完善企业信息
            public static final int uncheck = 0;
            // 审核中
            public static final int checking = 1;
            // 通过
            public static final int pass = 2;
            // 驳回
            public static final int reject = 3;
        }

        public static class Service {
            //已上架
            public static final String yes = "Y";
            //已下架
            public static final String no = "N";
        }

        public static class Goods {
            //已上架
            public static final String yes = "Y";
            //已下架
            public static final String no = "N";
        }

        public static class Activity {
            //已开始
            public static final int progress = 1;
            //未开始
            public static final int notStarted = 0;
            //已结束
            public static final int finished = 2;
            //已处理
            public static final String yes = "Y";
            //未处理
            public static final String no = "N";
        }

        public static class Comment {
            //已处理
            public static final int processed = 1;
            //未处理
            public static final int untreated = 2;
            //已处理
            public static final String yes = "Y";
            //未处理
            public static final String no = "N";
        }

        public static class Topic {
            //已上架
            public static final String yes = "Y";
            //已下架
            public static final String no = "N";
        }

        public static class Order {
            //1是代发货
            public static final int toBeDelivered = 1;
            //2是待确认
            public static final int toBeConfirmed = 2;
            //4已完成
            public static final int completed = 4;
            //6退货，只使用6
            public static final int returnGoods = 6;
            //7退款
            public static final int refund = 7;
            //已退款
            public static final int refunded = 11;
        }
    }

    public static class Action {
    }

    public static class Tag {
        public static final String code = "code";
        public static final String data = "data";
        public static final String token = "token";
        public static final String sessionid = "sessionid";
        public static final String message = "message";
        public static final String lastLoginUser = "lastLoginUser";
        public static final String modify = "modify";
        public static final String gonext = "gonext";
        public static final String list = "list";
        public static final String consult = "consult";
        public static final String servicesQA = "servicesQA";
        public static final String comment = "comment";
        public static final String signup = "signup";
    }
}
