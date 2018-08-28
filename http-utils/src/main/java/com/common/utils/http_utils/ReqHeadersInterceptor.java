package com.common.utils.http_utils;

import android.content.Context;


import java.io.IOException;
import java.util.Map;

import io.reactivex.annotations.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ReqHeadersInterceptor implements Interceptor {

    private Context context;
    private Map<String, String> headers;

    public ReqHeadersInterceptor(@NonNull Context context, @NonNull Map<String, String> headers) {
        this.context = EmptyUtils.checkNotNull(context).getApplicationContext();
        this.headers = EmptyUtils.checkNotNull(headers);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder request = chain
                .request()
                .newBuilder();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            request.header(entry.getKey(), entry.getValue());
        }
        return chain.proceed(request.build());
    }
}
