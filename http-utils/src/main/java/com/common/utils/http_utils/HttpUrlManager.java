package com.common.utils.http_utils;

import android.content.Context;
import android.content.pm.PackageManager;


import java.util.HashMap;
import java.util.Map;

public class HttpUrlManager {
    private static final Map<HttpManager.Retrofits, String> RETROFITS_URL_MAP = new HashMap<>();

    static void init(Context context) {
        try {
            RETROFITS_URL_MAP.put(HttpManager.Retrofits.base, MetaDataUtils.getString(context, "BASE_URL"));

        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.d(e);
        }

    }

    public static String getUrl(HttpManager.Retrofits retrofits) {
        return RETROFITS_URL_MAP.get(retrofits);
    }

}