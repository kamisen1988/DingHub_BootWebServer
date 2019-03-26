package com.ddtc.hubwebserver;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by Administrator on 2017/7/24.
 */

@SpringBootApplication
//public class AppEntryPoint implements CommandLineRunner{
public class AppEntryPoint extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AppEntryPoint.class);
    }

    public static void main(String[] args){
        SpringApplication.run(AppEntryPoint.class, args);
    }
}
