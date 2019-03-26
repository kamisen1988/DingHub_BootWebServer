package com.ddtc.hubwebserver.aop;

import com.alibaba.fastjson.JSON;
import com.ddtc.hubwebserver.controller.HelloController;
import com.ddtc.hubwebserver.entity.TAppAccessTokenInfo;
import com.ddtc.hubwebserver.service.BluetoothService;
import com.ddtc.hubwebserver.utils.SysContent;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.naming.NoPermissionException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Administrator on 2017/8/17.
 */
@Aspect
@Component
public class AuthAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    HttpSession session;
    @Autowired
    BluetoothService bluetoothService;

    @Pointcut("execution(public * com.ddtc.hubwebserver.controller.*Controller.*(..) ))")
    public void auth() {

    }

    @Around("auth()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();


        //System.out.println(request.getRequestURL().toString());


//        //打印所有参数
//        Enumeration<String> enu=request.getParameterNames();
//        while(enu.hasMoreElements()){
//            String paraName=(String)enu.nextElement();
//            System.out.println(paraName+": "+request.getParameter(paraName));
//        }

        String userAppId = request.getParameter("appId");
        String userAppSecret = request.getParameter("appSecret");
        String userAccessToken;

        if (request.getRequestURI().contains("loginin")) {
            if (userAppId == null) {
                userAppId = (String) session.getAttribute("appId");
                userAppSecret = (String) session.getAttribute("appSecret");
            } else {
                session.setAttribute("appId", userAppId);
                session.setAttribute("appSecret", userAppSecret);
            }
            TAppAccessTokenInfo tAppAccessTokenInfo = new TAppAccessTokenInfo();

            if (session.getAttribute("appId") != null) {
                userAppId = (String) session.getAttribute("appId");
                userAppSecret = (String) session.getAttribute("appSecret");

                userAccessToken = bluetoothService.queryAccessToken(userAppId, getMD5(userAppSecret));
                tAppAccessTokenInfo.setAccessToken(userAccessToken);
            }

            if (tAppAccessTokenInfo != null) {
                session.setAttribute("toBtoken", tAppAccessTokenInfo.getAccessToken());

                return proceedingJoinPoint.proceed();
            } else {

                return "success";

            }
        } else {
            return proceedingJoinPoint.proceed();
        }
    }


    private static String getMD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes("utf-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private HttpSession getSession() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpSession session = request.getSession();
        return session;
    }
}
