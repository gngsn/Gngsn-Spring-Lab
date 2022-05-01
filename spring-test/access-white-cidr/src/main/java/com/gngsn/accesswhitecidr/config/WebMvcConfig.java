package com.gngsn.accesswhitecidr.config;

import com.gngsn.accesswhitecidr.interceptor.IpAccessControlInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final IpAccessControlInterceptor ipAccessControlInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(ipAccessControlInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/resources/**")
            .excludePathPatterns("/error/**");
    }
}