package com.gngsn.springcore.environment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
public class AppRunner2 implements ApplicationRunner {

    @Autowired
    ApplicationContext ctx;

    @Autowired
    BookRepository bookRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Environment env = ctx.getEnvironment();
        System.out.println("app.name : " + env.getProperty("app.name"));
        System.out.println("app.about : " + env.getProperty("app.about"));

//        System.out.println(Arrays.toString(env.getActiveProfiles()));
//        System.out.println(Arrays.toString(env.getDefaultProfiles()));
    }
}
