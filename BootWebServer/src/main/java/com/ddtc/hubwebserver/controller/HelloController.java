package com.ddtc.hubwebserver.controller; /**
 * Created by Administrator on 2017/7/19.
 */

import com.ddtc.hubwebserver.utils.SysContent;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.ddtc.hubwebserver.service.BluetoothService;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;


@Controller
@RequestMapping("/hub")
public class HelloController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static AtomicInteger notiCount = new AtomicInteger(0);
    @Autowired
    BluetoothService bluetoothService;
    @Autowired
    HttpSession session;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/login")
    public String login() {
      return "login";
    }

    @RequestMapping("/loginin")
    public String loginin() {
        String appId = (String) session.getAttribute("appId");
        System.out.println("appId:  "+appId);
        return "redirect:success";
    }

    @RequestMapping("/success")
    public String loginsuccess(HttpSession session, ModelMap map) {

        String toBtoken = (String )session.getAttribute("toBtoken");

        System.out.println("toBtoken: "+toBtoken);

        map.addAttribute("toBtoken",toBtoken);
        return "success";
    }
}
