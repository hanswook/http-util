package com.common.utils.http_utils;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;

public class HttpManager {

    //    private static HttpStateMap httpStateMap;
    private static final Map<String, String> HTTP_HEADERS = new HashMap<>();
    private static final Map<Retrofits, IRetrofit> RETROFIT_MAP = new HashMap<>();
    static boolean isInit = false;
    private static HttpManagerImpl httpManagerImpl = new HttpManagerImpl();

    public static void init(Context context) {
//        httpStateMap = HttpStateMap.getInstance(context);
//        DNSCache.Instance.init(context, new DNSCacheConfig.Builder().build(), HostResolveStrategy.DEFAULT);
        isInit = true;
    }

    public static void addHeader(String name, String value) {
        HTTP_HEADERS.put(name, value);
    }

    public static Map<String, String> getHeaders() {
        return HTTP_HEADERS;
    }

    public static IRetrofit addRetrofit(Retrofits name, String url, List<Interceptor> interceptors, long connectTimeout, long readTimeout, long writeTimeout) {
        return addRetrofit(name, url, interceptors, null, connectTimeout, readTimeout, writeTimeout);
    }

    public static IRetrofit addRetrofit(Retrofits name, String url, List<Interceptor> interceptors, List<Interceptor> networkInterceptor,
                                        long connectTimeout, long readTimeout, long writeTimeout) {
        return addRetrofit(name, new DefaultRetrofit(url, interceptors, networkInterceptor, connectTimeout, readTimeout, writeTimeout));
    }

    public static IRetrofit addRetrofit(Retrofits name, IRetrofit retrofit) {
        RETROFIT_MAP.put(name, retrofit);
        return retrofit;
    }

    public static IRetrofit getRetrofit(Retrofits name) {
        return RETROFIT_MAP.get(name);
    }


    public static IHttpManager getIHttpManager() {
        return httpManagerImpl;
    }

    public static boolean isHttpProxy(Context context) {
        // 是否大于等于4.0
        final boolean isIcsOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        String proxyAddress;
        int proxyPort;
        if (isIcsOrLater) {
            proxyAddress = System.getProperty("");
            String proxyPortStr = System.getProperty("");
            proxyPort = Integer.parseInt(proxyPortStr != null ? proxyPortStr : "-1");
        } else {
            proxyAddress = android.net.Proxy.getHost(context);
            proxyPort = android.net.Proxy.getPort(context);
        }
        return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
    }

    static class HttpManagerImpl implements IHttpManager {
        @Override
        public void addHeaders(Map<String, String> headers) {
            if (EmptyUtils.isEmpty(headers)) {
                return;
            }
            for (Map.Entry<String, String> header : headers.entrySet()) {
                addHeader(header.getKey(), header.getValue());
            }
        }
    }

    public enum Retrofits {
        base,                   // base
    }
}
