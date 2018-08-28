package com.common.utils.http_utils;

import android.support.annotation.NonNull;


import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DefaultRetrofit extends IRetrofit {

    public DefaultRetrofit(@NonNull String baseUrl, @NonNull List<Interceptor> interceptors, List<Interceptor> networkInterceptor) {
        this(baseUrl, interceptors, networkInterceptor, TIMEOUT, TIMEOUT, TIMEOUT);
    }

    public DefaultRetrofit(@NonNull String baseUrl,
                           @NonNull List<Interceptor> interceptors,
                           List<Interceptor> networkInterceptor,
                           long connectTimeout,
                           long readTimeout,
                           long writeTimeout) {
        super(baseUrl, interceptors, networkInterceptor, connectTimeout, readTimeout, writeTimeout);
    }

    @Override
    OkHttpClient buildClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        if (!EmptyUtils.isEmpty(networkInterceptor)) {
            for (Interceptor interceptor : networkInterceptor) {
                builder.addNetworkInterceptor(interceptor);
            }
        }
        if (!EmptyUtils.isEmpty(interceptors)) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        return builder.build();
    }

    @Override
    Converter.Factory converterFactory() {
        return GsonConverterFactory.create();
    }

    @Override
    CallAdapter.Factory callAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }

    @Override
    String baseUrl() {
        return baseUrl;
    }

    /**
     * create retrofit service
     *
     * @param service the service class
     * @param <T>     class type
     * @return the real service instance
     */
    @Override
    public <T> T create(Class<T> service) {
        return retrofit.create(service);
    }
}

