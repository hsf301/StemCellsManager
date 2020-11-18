package com.huashengfu.StemCellsManager.http;


import com.huashengfu.StemCellsManager.Constants;

public class HttpHelper {

    public static class Url {

        public static final String shared = "http://huashengfu.cn/share/?no=";
        public static final String protocol = "http://www.huashengfu.cn/server/services-agreement.html";
        public static final String privacy = "http://www.huashengfu.cn/server/privacy-policy.html";

        public static class Admin {
            //刷新token
            public static final String refreshToken = Constants.BaseUrl + "/admin/refreshToken";
            //登录以后返回token
            public static final String login = Constants.BaseUrl + "/admin/login";
            //获取当前登录用户信息
            public static final String info = Constants.BaseUrl + "/admin/info";
            //登录获取手机验证码
            public static final String verification = Constants.BaseUrl + "/admin/verification";
            //店铺申请
            public static final String register = Constants.BaseUrl + "/admin/register";
            //找回密码
            public static final String retrievePassword = Constants.BaseUrl + "/admin/retrievePassword";
            //修改指定用户密码
            public static final String updatePassword = Constants.BaseUrl + "/admin/updatePassword";
        }

        public static class Service {
            //创建服务--基本信息
            public static final String addService = Constants.BaseUrl + "/sms/addService";
            //创建服务--轮播和封皮
            public static final String addServiceBanner = Constants.BaseUrl + "/sms/addServiceBanner";
            // 获取未完成服务接口
            public static final String unfinished = Constants.BaseUrl + "/sms/unfinished";
            // 删除未完成的服务接口
            public static final String delUnfinished = Constants.BaseUrl + "/sms/delUnfinished/";
            //创建服务前初始化(修改前初始化)接口
            public static final String init = Constants.BaseUrl + "/sms/init";
            //更新服务--基础文字描述信息
            public static final String modifyBasicInfo = Constants.BaseUrl + "/sms/modifyBasicInfo";
            //服务轮播图上传
            public static final String uploadBanner = Constants.BaseUrl + "/sms/upload/banner";
            //查询服务分页列表
            public static final String serviceList = Constants.BaseUrl + "/sms/serviceList";
            //服务封皮上传
            public static final String uploadCover = Constants.BaseUrl + "/sms/upload/cover";
            //更新服务--banner和封皮
            public static final String modifyBanner = Constants.BaseUrl + "/sms/modifyBanner";
            //3创建服务--促销信息
            public static final String characteristicAdd = Constants.BaseUrl + "/sms/characteristic/add";
            //3更新服务--促销信息
            public static final String characteristicModify = Constants.BaseUrl + "/sms/characteristic/modify";
            //4创建服务--服务特权
            public static final String outlineAdd = Constants.BaseUrl + "/sms/outline/add";
            //4更新服务--特权信息
            public static final String outlineModify = Constants.BaseUrl + "/sms/outline/modify/";
            //5创建服务--服务流程
            public static final String stepAdd = Constants.BaseUrl + "/sms/step/add";
            //5更新服务--流程信息
            public static final String stepModify = Constants.BaseUrl + "/sms/step/modify/";
            //服务详情图片上传
            public static final String detailsUpload = Constants.BaseUrl + "/sms/details/upload";
            //5创建服务--服务介绍
            public static final String detailsAdd = Constants.BaseUrl + "/sms/details/add";
            //6更新服务--介绍信息
            public static final String detailsModify = Constants.BaseUrl + "/sms/details/modify/";
            //服务上下架
            public static final String upDownStatus = Constants.BaseUrl + "/sms/upDownStatus/";
            //获取服务详情
            public static final String serversInfo = Constants.BaseUrl + "/sms/serversInfo";

            public static class Comment {
                //获取互动列表
                public static final String list = Constants.BaseUrl + "/sms/comment/list";
                //商户回复用户的提问
                public static final String add = Constants.BaseUrl + "/sms/comment/add";
            }

            public static class Topic {
                //获取话题列表
                public static final String list = Constants.BaseUrl + "/sms/topic/list";
            }

            public static class Consult {
                //通过服务编号获取报名列表（商户端的咨询）
                public static final String listBySid = Constants.BaseUrl + "/sms/consult/listBySid";
            }

        }

        public static class Goods {
            //创建商品前初始化(修改前初始化)接口
            public static final String init = Constants.BaseUrl + "/goods/init";
            //商品轮播图上传
            public static final String upload = Constants.BaseUrl + "/goods/upload";
            //商品上传视频
            public static final String uploadVideo = Constants.BaseUrl + "/goods/upload/video";
            //商品规格图片上传
            public static final String uploadPic = Constants.BaseUrl + "/goods/upload/pic";
            //2-创建商品
            public static final String add = Constants.BaseUrl + "/goods/add";
            //获取商品分页列表
            public static final String list = Constants.BaseUrl + "/goods/list";
            //单商品上下架
            public static final String updatePublishStatus = Constants.BaseUrl + "/goods/update/publishStatus/";
            //4-更新商品信息
            public static final String modifyInfo = Constants.BaseUrl + "/goods/modifyInfo";
            //3-更新商品banner
            public static final String modifyBanner = Constants.BaseUrl + "/goods/modifyBanner";
            //商品预览
            public static final String info = Constants.BaseUrl + "/goods/info";

            public static class Parameters {
                //初始化商品参数必填项
                public static final String initByTypeId = Constants.BaseUrl + "/goodsParam/initByTypeId";
                //获取商品参数列表
                public static final String initByGoodsId = Constants.BaseUrl + "/goodsParam/initByGoodsId";
                //修改商品参数
                public static final String modify = Constants.BaseUrl + "/goodsParam/modify/";
            }

            public static class Stock {
                //查询商品库存列表
                public static final String list = Constants.BaseUrl + "/goods/stock/list";
                //修改sku
                public static final String modify = Constants.BaseUrl + "/goods/stock/modify/";
            }

            public static class Details {
                //创建商品详情
                public static final String add = Constants.BaseUrl + "/goodsDetails/add";
                //商品详情图上传
                public static final String upload = Constants.BaseUrl + "/goodsDetails/upload";
                //更新商品详情
                public static final String modify = Constants.BaseUrl + "/goodsDetails/modify";
            }
        }

        public static class Dynamic {
            //发布动态
            public static final String add = Constants.BaseUrl + "/dms/dynamic/add";
            //动态视频
            public static final String video = Constants.BaseUrl + "/dms/dynamic/video";
            //动态内容图片
            public static final String upload = Constants.BaseUrl + "/dms/dynamic/upload";
            //获取机构动态分页列表
            public static final String dynamicList = Constants.BaseUrl + "/dms/dynamic/dynamicList";
            //按Id获取机构动态详情页
            public static final String info = Constants.BaseUrl + "/dms/dynamic/info";
            //根据编号逻辑删除动态
            public static final String del = Constants.BaseUrl + "/dms/dynamic/del/";

            public static class Comment {
                //通过动态编号获取动态评论列表
                public static final String commentListByDynamicId = Constants.BaseUrl + "/dms/comment/commentListByDynamicId";
                //批量删除动态评论
                public static final String deleteComment = Constants.BaseUrl + "/dms/comment/deleteComment";
            }
        }

        public static class Activity {
            //创建活动前初始化(修改前初始化)接口
            public static final String init = Constants.BaseUrl + "/activity/init";
            //新建活动
            public static final String add = Constants.BaseUrl + "/activity/add";
            //获取活动分页列表
            public static final String pageList = Constants.BaseUrl + "/activity/pageList";
            //修改活动状态
            public static final String modifyStatus = Constants.BaseUrl + "/activity/modifyStatus/";
            //修改活动封面
            public static final String modifyBanner = Constants.BaseUrl + "/activity/modifyBanner/";
            //修改活动信息
            public static final String modifyInfo = Constants.BaseUrl + "/activity/modifyInfo/";
            //根据id获取活动详情
            public static final String info = Constants.BaseUrl + "/activity/info";

            public static class Details {
                //活动封皮上传
                public static final String uploadBanner = Constants.BaseUrl + "/activity/details/upload/banner";
                //修改活动详情
                public static final String modify = Constants.BaseUrl + "/activity/details/modify/";
                //新建活动详情
                public static final String add = Constants.BaseUrl + "/activity/details/add";
                //活动详情图片上传
                public static final String upload = Constants.BaseUrl + "/activity/details/upload";
            }

            public static class Signup {
                //通过活动编号获取报名列表
                public static final String listByAid = Constants.BaseUrl + "/activity/signup/listByAid";
            }

        }

        public static class Store {

            //店铺广告banner上传
            public static final String uploadBanner = Constants.BaseUrl + "/store/upload/banner";
            //店铺logo上传
            public static final String uploadLogo = Constants.BaseUrl + "/store/upload/logo";
            //更新广告
            public static final String modifyAD = Constants.BaseUrl + "/store/modifyAD";
            //获取店铺广告列表
            public static final String getAD = Constants.BaseUrl + "/store/getAD";
            //获取客服电话列表
            public static final String customerServicesList = Constants.BaseUrl + "/store/customerServicesList";
            //客服电话更新设置
            public static final String modifyCustomerServices = Constants.BaseUrl + "/store/modifyCustomerServices";
            //完善企业信息
            public static final String modify = Constants.BaseUrl + "/store/modify";


            public static class Type {
                //获取机构类型分页列表
                public static final String pageList = Constants.BaseUrl + "/store/type/pageList";
            }

            public static class Branch {
                //获取企业分支机构分页列表
                public static final String list = Constants.BaseUrl + "/store/branch/list";
                //新建分支机构
                public static final String add = Constants.BaseUrl + "/store/branch/add";
                //删除分支机构
                public static final String del = Constants.BaseUrl + "/store/branch/del/";
                //修改分支机构信息
                public static final String update = Constants.BaseUrl + "/store/branch/updateQuestionStatus";
            }
        }

        public static class Order {
            //查询订单分页列表
            public static final String pageList = Constants.BaseUrl + "/order/pageList";
            //商家订单发货
            public static final String sendOutGoods = Constants.BaseUrl + "/order/sendOutGoods";
            //商家同意退款
            public static final String agreeRefund = Constants.BaseUrl + "/order/agreeRefund";
        }

        public static class Courier {
            //查询物流公司列表
            public static final String list = Constants.BaseUrl + "/courier/list";
        }
    }

    public static class Params {
        public static final String pageNum = "pageNum";
        public static final String pageSize = "pageSize";
        public static final String status = "status";
        public static final String password = "password";
        public static final String username = "username";
        public static final String token = "token";
        public static final String tokenHead = "tokenHead";
        public static final String Authorization = "Authorization";
        public static final String labelList = "labelList";
        public static final String id = "id";
        public static final String oid = "oid";
        public static final String dNo = "dNo";

        public static final String goodsId = "goodsId";
        public static final String goodsDetailsList = "goodsDetailsList";
        public static final String content = "content";
        public static final String name = "name";
        public static final String phone = "phone";
        public static final String titles = "titles";
        public static final String typeId = "typeId";
        public static final String title = "title";

        public static final String aid = "aid";
        public static final String aId = "aId";

        public static final String files = "files";
        public static final String video = "video";
        public static final String pic = "pic";

        public static final String subTitle = "subTitle";
        public static final String thumbnail = "thumbnail";
        public static final String assetsHeight = "assetsHeight";
        public static final String assetsWidth = "assetsWidth";
        public static final String detailsList = "detailsList";
        public static final String contentType = "contentType";

        public static final String verification = "verification";
        public static final String newPassword = "newPassword";
        public static final String oldPassword = "oldPassword";
        public static final String repeatPassword = "repeatPassword";
        public static final String storeName = "storeName";
        public static final String type = "type";

        public static final String banners = "banners";
        public static final String banner = "banner";
        public static final String cover = "cover";
        public static final String width = "width";
        public static final String height = "height";
        public static final String sid = "sid";
        public static final String sId = "sId";

        public static final String stepNo = "stepNo";
        public static final String dayNo = "dayNo";
        public static final String processList = "processList";
        public static final String step = "step";

        public static final String details = "details";
        public static final String titleOne = "titleOne";
        public static final String titleTwo = "titleTwo";
        public static final String titleThree = "titleThree";
        public static final String titleFour = "titleFour";
        public static final String detailsText = "detailsText";

        public static final String brand = "brand";
        public static final String categoryId = "categoryId";
        public static final String deliverAddress = "deliverAddress";
        public static final String labels = "labels";
        public static final String logistics = "logistics";
        public static final String minPrice = "minPrice";
        public static final String originalPrice = "originalPrice";
        public static final String params = "params";
        public static final String serviceRemarks = "serviceRemarks";
        public static final String skus = "skus";
        public static final String skuName = "skuName";
        public static final String skuPic = "skuPic";
        public static final String skuPrice = "skuPrice";
        public static final String skuSum = "skuSum";
        public static final String videoUrl = "videoUrl";

        public static final String publishStatus = "publishStatus";

        public static final String addr = "addr";
        public static final String endDate = "endDate";
        public static final String startDate = "startDate";
        public static final String latitude = "latitude";
        public static final String longitude = "longitude";
        public static final String quota = "quota";

        public static final String activityStatus = "activityStatus";

        public static final String category = "category";
        public static final String uid = "uid";
        public static final String tid = "tid";
        public static final String dynamicId = "dynamicId";
        public static final String ids = "ids";
        public static final String businessHours = "businessHours";

        public static final String newPhone = "newPhone";

        public static final String courierInfo = "courierInfo";
        public static final String orderId = "orderId";
        public static final String courierNo = "courierNo";
        public static final String courierName = "courierName";


        public static final String ceo = "ceo";
        public static final String city = "city";
        public static final String createDate = "createDate";
        public static final String enterpriseName = "enterpriseName";
        public static final String enterpriseType = "enterpriseType";
        public static final String icon = "icon";
        public static final String officialWeb = "officialWeb";
        public static final String registerAddress = "registerAddress";
        public static final String sketch = "sketch";
        public static final String weChat = "weChat";
    }

}
