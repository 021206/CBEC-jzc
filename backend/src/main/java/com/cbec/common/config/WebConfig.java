package com.cbec.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Token 拦截器，拦截所有请求
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/error", "/auth/menu","/auth/login");

        // 注意：之前的 TraceIdInterceptor 可以合并到 TokenInterceptor 里，也可以保留分开注册。
        // 目前为了简单，可以先把它注释掉，或者让两个都生效（不影响）。
        // 但如果保留 TraceIdInterceptor，记得在 TokenInterceptor 之前执行。
        // 建议：在 TokenInterceptor 里也生成 traceId，这样就不用单独的拦截器了。
        // 但我们可以保留 TraceIdInterceptor 或把 traceId 生成逻辑移到 TokenInterceptor 里。
        // 最简单做法：保留两个，但调整顺序。
        // registry.addInterceptor(new TraceIdInterceptor()).addPathPatterns("/**");
    }
}