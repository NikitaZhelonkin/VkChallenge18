package com.vk.challenge.data.api;

import com.vk.sdk.VKAccessToken;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HttpInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        VKAccessToken token = VKAccessToken.currentToken();
        request = request.newBuilder()
                .url(request.url().newBuilder()
                        .addQueryParameter("access_token", token.accessToken)
                        .addQueryParameter("v", "5.87")
                        .build())
                .build();
        return chain.proceed(request);
    }
}
