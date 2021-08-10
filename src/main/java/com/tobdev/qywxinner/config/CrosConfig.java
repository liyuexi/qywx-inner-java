package com.tobdev.qywxinner.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CrosConfig {

    @Bean
    public CorsFilter corsFilter(){
        //方式二 跨域
        //https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-cors
        CorsConfiguration config = new CorsConfiguration();

// Possibly...
// config.applyPermitDefaultValues()

        config.setAllowCredentials(false);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        CorsFilter filter= new CorsFilter(source);
        return  filter;

    }
}
