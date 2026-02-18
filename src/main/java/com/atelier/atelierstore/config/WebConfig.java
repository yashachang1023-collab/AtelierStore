package com.atelier.atelierstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class WebConfig {

    @Bean // 告诉 Spring：这是一个必须要加载的组件！
    public CorsFilter corsFilter() {
        // 加个打印，证明它活着
        System.out.println(">>>>>>>>>> [终极方案] CorsFilter 正在启动... <<<<<<<<<<");

        CorsConfiguration config = new CorsConfiguration();
        // 允许前端地址
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        // 允许所有方法
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 允许所有头信息
        config.setAllowedHeaders(Arrays.asList("*"));
        // 允许携带凭证（以后登录要用）
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}