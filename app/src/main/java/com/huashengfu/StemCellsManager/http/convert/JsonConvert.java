package com.huashengfu.StemCellsManager.http.convert;

import com.lzy.okgo.convert.Converter;

import org.json.JSONObject;

import okhttp3.Response;

public class JsonConvert implements Converter<JSONObject>{

    @Override
    public JSONObject convertResponse(Response response) throws Throwable {
        if(response.body() == null)
            return null;
        else
            return new JSONObject(response.body().string());
    }
}
