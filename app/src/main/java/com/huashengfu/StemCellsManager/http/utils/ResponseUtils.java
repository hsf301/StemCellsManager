package com.huashengfu.StemCellsManager.http.utils;


import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResponseUtils {

    public static boolean ok(JSONObject jsonObject) throws JSONException {
        return jsonObject.getInt(Constants.Tag.code) == Constants.Code.SUCCESS_CODE;
    }

    public static String getMsg(JSONObject jsonObject) throws JSONException {
        int code = jsonObject.getInt(Constants.Tag.code);
        String ret = jsonObject.getString(Constants.Tag.message);//"服务器偷懒了，请稍后再试！";
        if(StringUtils.isNullOrBlank(ret)){
            switch (code){
                case Constants.Code.TOKEN_INVALID:{
                    ret = "Token已失效！";
                    break;
                }
                case Constants.Code.FORBIDDEN:{
                    ret = "没有相关权限！";
                    break;
                }
                case Constants.Code.PWD_FAILED:{
                    ret = "密码错误！";
                    break;
                }
                case Constants.Code.VALIDATE_FAILED:{
                    ret = "参数检验失败！";
                    break;
                }
            }
        }

        return ret;
    }

    public static int getCode(JSONObject jsonObject) throws JSONException {
        return jsonObject.getInt(Constants.Tag.code);
    }

    public static JSONArray getList(JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONArray(Constants.Tag.data);
    }

    public static JSONObject getData(JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONObject(Constants.Tag.data);
    }
}
