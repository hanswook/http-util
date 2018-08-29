package com.common.utils.http_utils;




import com.common.utils.common_utils.EmptyUtils;

import java.util.List;

import io.reactivex.annotations.NonNull;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public abstract class IRetrofit {
    static final long TIMEOUT = 15;

    long connectTimeout;
    long readTimeout;
    long writeTimeout;
    List<Interceptor> interceptors;
    List<Interceptor> networkInterceptor;
    String baseUrl;
    Retrofit retrofit;
    OkHttpClient client;

    IRetrofit(@NonNull String baseUrl,
              @NonNull List<Interceptor> interceptors,
              List<Interceptor> networkInterceptor,
              long connectTimeout,
              long readTimeout,
              long writeTimeout) {
        this.baseUrl = EmptyUtils.checkNotNull(baseUrl);
        this.interceptors = EmptyUtils.checkNotNull(interceptors);
        this.networkInterceptor = networkInterceptor;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.writeTimeout = writeTimeout;
        this.client = buildClient();
        retrofit = new Retrofit.Builder()
                .client(EmptyUtils.checkNotNull(client))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(EmptyUtils.checkNotNull(converterFactory()))
                .addCallAdapterFactory(EmptyUtils.checkNotNull(callAdapterFactory()))
                .baseUrl(EmptyUtils.checkNotNull(baseUrl()))
                .build();
    }

    public OkHttpClient getClient() {
        return client;
    }

    abstract OkHttpClient buildClient();

    abstract Converter.Factory converterFactory();

    abstract CallAdapter.Factory callAdapterFactory();

    abstract String baseUrl();

    public abstract <T> T create(final Class<T> service);
}