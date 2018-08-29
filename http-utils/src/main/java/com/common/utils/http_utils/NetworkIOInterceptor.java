package com.common.utils.http_utils;

import android.content.Context;
import android.util.Config;


import com.common.utils.common_utils.LogUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkIOInterceptor implements Interceptor {
    private Context mContext;

    public NetworkIOInterceptor(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response;
        try {
            response = chain.proceed(request);
            if (!response.isSuccessful()) {
                // TODO
            }
            // TODO
        } catch (IOException e) { // 对业务层屏蔽掉一些网络错误
            if (BuildConfig.DEBUG)
                LogUtils.d(e);
            throw new IOException(e.getMessage());
        }
        return response;
    }
}
