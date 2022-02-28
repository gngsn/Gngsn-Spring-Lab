package com.gngsn.springcore.dataBinding.converter;

import com.gngsn.springcore.dataBinding.formatter.EventFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // converter를 등록
//        registry.addConverter(new EventConverter.StringToEventConverter());
        registry.addFormatter(new EventFormatter());
    }
}
