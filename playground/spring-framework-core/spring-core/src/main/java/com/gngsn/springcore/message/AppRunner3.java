package com.gngsn.springcore.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

//@Component
public class AppRunner3 implements ApplicationRunner {

    @Autowired
    MessageSource messageSource;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("======= Message AppRunner =======");
//        while(true) {
        System.out.println(messageSource.getMessage("greeting", new String[]{"gngsn"}, Locale.KOREA));
        System.out.println(messageSource.getMessage("greeting", new String[]{"gngsn"}, Locale.getDefault()));
//        }
    }
}
