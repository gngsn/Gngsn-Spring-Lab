package com.gngsn.springcore.dataBinding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

//@Component
public class AppRunner7 implements ApplicationRunner {

    @Autowired
    ConversionService conversionService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("======= Conversion Service AppRunner =======");
//        System.out.println("conversionService : " + conversionService);
        System.out.println(conversionService.getClass().toString());
    }
}
