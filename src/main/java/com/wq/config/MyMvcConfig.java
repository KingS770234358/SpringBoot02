package com.wq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Locale;

/***
 * 自定义Mvc配置类
 * 扩展SpringMVC的配置
 */
@Configuration
@EnableWebMvc
public class MyMvcConfig implements WebMvcConfigurer {
    // Bean 把这个类交给SpringBoot接管(注入容器)
    @Bean
    public ViewResolver myViewResolver(){
        return new MyViewResolver();
    }
    /***
     * 写一个自己的视图解析器
     */
    public static class MyViewResolver implements ViewResolver{
        @Override
        public View resolveViewName(String s, Locale locale) throws Exception {
            return null;
        }
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        // 在这里进行url和视图的映射
        registry.addViewController("/addViewControllers").setViewName("thymeleafTest.html");
    }

}


