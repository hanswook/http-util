package com.common.utils.http_utils;


import android.support.v4.util.ArrayMap;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * @author: LiBing.
 * @date: 2017/11/17.
 * @version: V1.0.0.
 */

public class HttpRequestBody {
    private static JSONObject obj;


    public static RequestBody getRequestBody(JSONObject jsonObject){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(jsonObject.toString()));
        return body;
    }


    public static RequestBody getRequestBody(String[] keys, String[] values){
        Map<String, Object> jsonParams = new ArrayMap<>();

        for (int i = 0; i < keys.length; i++) {
            jsonParams.put(keys[i], values[i]);
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());

        return body;

    }


    public static RequestBody getRequestBody(String[] keys, int[] values){
        Map<String, Object> jsonParams = new ArrayMap<>();

        for (int i = 0; i < keys.length; i++) {
            jsonParams.put(keys[i], values[i]);
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());

        return body;

    }

    public static RequestBody getRequestBodyList(Object object){
        String jsonData = new Gson().toJson(object);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonData);

        return body;

    }


    public static RequestBody getUserRequestBody(String[] keys, String[] values){
        Map<String, Object> jsonParams = new ArrayMap<>();

        for (int i = 0; i < keys.length; i++) {
            jsonParams.put(keys[i], values[i]);
        }



      /*  {
            "token": "3442.8401241232887",
                "user": {
            "userAddress": null,
                    "authentication_token": 100,
                    "userBirth": null,
                    "userId": 2,
                    "userName": "1Q8f1U5gXDQ",
                    "userHead": null
        },
            "info": 100
        }

        try {
            jsonObject = new JSONObject();
            jsonObject.put("token",jsonParams.get("token").toString());

            JSONObject obj = new JSONObject();
            obj.put("id",jsonParams.get("id"));
            obj.put("userName","Think");
            obj.put("userAddress",jsonParams.get("userAddress"));
            obj.put("userBirth",jsonParams.get("userBirth"));
            jsonObject.put("user",obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/


        try {
            obj = new JSONObject();
            obj.put("token",jsonParams.get("token"));
            obj.put("id",jsonParams.get("id"));
            obj.put("userName",jsonParams.get("userName"));
            obj.put("userAddress",jsonParams.get("userAddress"));
            obj.put("userBirth",jsonParams.get("userBirth"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(obj.toString()));

        return body;

    }

}
