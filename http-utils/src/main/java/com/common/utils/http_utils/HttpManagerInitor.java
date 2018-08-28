package com.common.utils.http_utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import okhttp3.Interceptor;

import static com.common.utils.http_utils.HttpManager.Retrofits.base;


public class HttpManagerInitor {
    private static boolean logEnable;
    private static InterceptorsBuilder interceptorsBuilder;

    public static void init(Context context, boolean logEnable) {
        HttpManagerInitor.logEnable = logEnable;
        HttpManager.init(context);
        if (interceptorsBuilder == null) {
            interceptorsBuilder = new InterceptorsBuilder();
        }
        List<Interceptor> interceptors = new ArrayList<>(new HashSet<>(interceptorsBuilder.buildInterceptors(context)));
        List<Interceptor> networkInterceptors = new ArrayList<>(new HashSet<>(interceptorsBuilder.buildNetworkInterceptors(context)));
        HttpUrlManager.init(context);
        addRetrofit(base, HttpUrlManager.getUrl(base), interceptors, networkInterceptors, context);
    }

    private static void addRetrofit(HttpManager.Retrofits name, String url, List<Interceptor> interceptors, List<Interceptor> networkInterceptor, Context context) {
        try {
            Bundle metaData =
                    context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
            HttpManager.addRetrofit(name, url, interceptors, networkInterceptor,
                    metaData.getInt("CONNECT_TIMEOUT", 15),
                    metaData.getInt("READ_TIMEOUT", 30),
                    metaData.getInt("WRITE_TIMEOUT", 15));
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("HttpManagerInitor", e.getMessage(), e);
        }
    }

    public static void setInterceptorsBuilder(InterceptorsBuilder interceptorsBuilder) {
        HttpManagerInitor.interceptorsBuilder = interceptorsBuilder;
    }

    public static class InterceptorsBuilder {
        public List<Interceptor> buildInterceptors(Context context) {
            List<Interceptor> interceptors = new ArrayList<>();
            interceptors.add(new ReqHeadersInterceptor(context, HttpManager.getHeaders()));
            interceptors.add(new NetworkIOInterceptor(context));
            if (logEnable) {
//                interceptors.add(new ChuckInterceptor(context));
                interceptors.add(new LoggingInterceptor());
            }
            return interceptors;
        }

        public List<Interceptor> buildNetworkInterceptors(Context context) {
            return new ArrayList<>();
        }
    }

}
