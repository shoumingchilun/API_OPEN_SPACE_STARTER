package com.chilun.apiopenspace.starter;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author 齿轮
 * @date 2024-02-17-14:35
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class APIAccessClient<T> {
    private OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS)
            .readTimeout(60000, TimeUnit.MILLISECONDS)
            .build();

    private String accesskey;
    private String secretkey;
    private Long expiration = 300000L;//300000毫秒，5分钟

    //如"http://localhost:8002/api/7"
    private String GatewayAPIURI;

    public Response sentGetRequest() throws IOException, NoSuchAlgorithmException {
        //一、生成请求所需信息
        //1.开始时间a
        long beginStamp = System.currentTimeMillis();
        //2.盐值
        int salt = new Random().nextInt(1001) + 1000;
        //3.签名
        String sign = SignatureUtils.generateSignature(accesskey + secretkey + GatewayAPIURI + String.valueOf(beginStamp) + salt, secretkey);

        //二、发送请求
        //1组装请求
        Headers headers = new Headers.Builder()
                .add("ChilunAPISpace-sendTimestamp", String.valueOf(beginStamp))
                .add("ChilunAPISpace-expireTimestamp", String.valueOf(beginStamp + expiration))
                .add("ChilunAPISpace-accesskey", accesskey)
                .add("ChilunAPISpace-salt", String.valueOf(salt))
                .add("ChilunAPISpace-sign", sign)
                .build();
        Request request = new Request.Builder()
                .url(GatewayAPIURI)
                .headers(headers)
                .get()
                .build();
        //2发送请求
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new RuntimeException("Failed to get response: " + response+"\n Body: "+(response.body()!=null?response.body().string():"null"));
        }
        return response;
    }

    public Response sentPostRequest(T sendData) throws IOException, NoSuchAlgorithmException {
        //一、生成请求所需信息
        //1.开始时间
        long beginStamp = System.currentTimeMillis();
        //2.盐值
        int salt = new Random().nextInt(1001) + 1000;
        //3.签名
        String sign = SignatureUtils.generateSignature(accesskey + secretkey + GatewayAPIURI + String.valueOf(beginStamp) + salt, secretkey);

        //二、发送请求
        //1组装请求
        Headers headers = new Headers.Builder()
                .add("ChilunAPISpace-sendTimestamp", String.valueOf(beginStamp))
                .add("ChilunAPISpace-expireTimestamp", String.valueOf(beginStamp + expiration))
                .add("ChilunAPISpace-accesskey", accesskey)
                .add("ChilunAPISpace-salt", String.valueOf(salt))
                .add("ChilunAPISpace-sign", sign)
                .build();
        Request request = new Request.Builder()
                .url(GatewayAPIURI)
                .headers(headers)
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),JSON.toJSONString(sendData)))
                .build();
        //2发送请求
        OkHttpClient okHttpClient = new OkHttpClient();
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new RuntimeException("Failed to get response: " + response+"\n Body: "+(response.body()!=null?response.body().string():"null"));
        }
        return response;
    }
}
