package com.gngsn.springcore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import other.pack.MyService;

@SpringBootApplication
public class SpringCoreApplication {

    // GenericApplicationContext.registerBean
    // -> ComponentScan 범위 밖의 패키지를 Bean으로 등록해서 Autowired 가능하게 만듦
    @Autowired
    MyService myService;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringCoreApplication.class);
        // 구동 전 실행하고 싶은 게 있다면 addInitializers
        app.addInitializers((ApplicationContextInitializer<GenericApplicationContext>) ctx -> {
            ctx.registerBean(MyService.class);
            ctx.registerBean(ApplicationRunner.class, () -> args1 -> System.out.println("Functional Bean Definition"));
        });
        app.run(args);
    }

}
