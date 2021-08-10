package com.tobdev.qywxinner.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.List;

@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //方式一 跨域
        //https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-cors
//        registry.addMapping("/api/**")
//                .allowedOrigins("https://domain2.com")
//                .allowedMethods("PUT", "DELETE")
//                .allowedHeaders("header1", "header2", "header3")
//                .exposedHeaders("header1", "header2")
//                .allowCredentials(true).maxAge(3600);

        // Add more mappings...
    }

    @Bean
    public HttpMessageConverter responseBodyConverter() {
        //解决返回值中文乱码
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return converter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(responseBodyConverter());
    }

}