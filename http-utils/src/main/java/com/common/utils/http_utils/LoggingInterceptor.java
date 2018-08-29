package com.common.utils.http_utils;

import android.util.Log;


import com.common.utils.common_utils.EmptyUtils;
import com.common.utils.common_utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class LoggingInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final String TAG = "LoggingInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String url = request.url().toString();
        String headers = request.headers().toString();
        String requestPayload = "";
        RequestBody requestBody = request.body();
        if (requestBody != null && requestBody.contentType() != null && !"multipart".equals(requestBody.contentType().type())) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = requestBody.contentType().charset(UTF8);
            if (isPlaintext(buffer)) {
                requestPayload = buffer.readString(charset);
            }
        }
        long t1 = System.nanoTime();
        LogUtils.d(String.format(Locale.CHINA, "Sending request %s on %s%n%s%n%s",
                url, chain.connection(), headers, requestPayload));

        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            LogUtils.d(String.format(Locale.CHINA, "HTTP FAILED:%n url:%s%n headers:%s%n error:%s%n%s",
                    url, headers, e.getMessage(), getStackTraceString(e)));
            throw e;
        }

        long t2 = System.nanoTime();
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset charset = Charset.forName("UTF-8");
        String bodyStr = "";
        if (responseBody.contentLength() != 0) {
            bodyStr = buffer.clone().readString(charset);
            bodyStr = jsonPretty(bodyStr);
        }
        LogUtils.d(String.format(Locale.CHINA, "Received response for %s in %.1fms%n%s%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers(), bodyStr));
        return response;
    }

    private String jsonPretty(String json) {
        if (EmptyUtils.isEmpty(json)) {
            return json;
        }
        try {
            json = json.trim();
            if (json.startsWith("{")) {
                return new JSONObject(json).toString(2);
            }
            if (json.startsWith("[")) {
                return new JSONArray(json).toString(2);
            }
        } catch (JSONException e) {
            Log.w(TAG, String.format(Locale.CHINA, "json: %s, errorMsg: %s", json, e.getMessage()), e);
        }
        return json;
    }

    private String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

}
