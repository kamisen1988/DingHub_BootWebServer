package com.ddtc.hubwebserver.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SysContent {
    private static ThreadLocal<HttpServletRequest> requestLocal= new ThreadLocal<>();
    private static ThreadLocal<HttpServletResponse> responseLocal= new ThreadLocal<>();
    private static ThreadLocal<HttpSession> sessionLocal = new ThreadLocal<>();
    public static HttpServletRequest getRequest() {
        return (HttpServletRequest)requestLocal.get();
    }
    public static void setRequest(HttpServletRequest request) {
        requestLocal.set(request);
    }
    public static HttpServletResponse getResponse() {
        return (HttpServletResponse)responseLocal.get();
    }
    public static void setResponse(HttpServletResponse response) {
        responseLocal.set(response);
    }
    public static HttpSession getSession() {
        System.out.println("getSession");
        return requestLocal.get().getSession();
    }
    public static void setSession(HttpSession session) {
        System.out.println("setseesion");
       sessionLocal.set(session);
    }
}