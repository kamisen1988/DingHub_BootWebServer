package com.ddtc.hubwebserver.service;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.TimeUnit;
import net.sf.json.JSONObject;

/**
 * Created by Administrator on 2017/7/24.
 */
@Service
public class BluetoothService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**通过appId和MD5加密后的appSecret，向丁丁的服务器获取token。
     * 对应地址为：
     * https://public.dingdingtingche.com/ddtcSDK/
     * http://test.dingdingtingche.com/ddtcSDK/
     * 更具体信息，请参考《丁丁停车toB SDK服务端接口文档》
     * */
    public String queryAccessToken(String appId, String appSecretMD5) {

        String OPERLOCK_URL = "http://test.dingdingtingche.com/ddtcSDK/queryAccessToken?appId="+appId+"&appSecret="+appSecretMD5;
        System.out.println(OPERLOCK_URL);
        logger.info("建立连接...");
        RestTemplate template = new RestTemplate();
        String response = template.getForObject(OPERLOCK_URL, String.class, appId, appSecretMD5);
        System.out.println("resp: "+response);
        JSONObject jsonObject = JSONObject.fromObject(response);
        String accessToken = String.valueOf(jsonObject.get("accessToken"));
        logger.info("accessToken: "+accessToken);
        return accessToken;
    }
}
