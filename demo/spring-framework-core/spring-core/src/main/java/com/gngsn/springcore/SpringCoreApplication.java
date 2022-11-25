package com.gngsn.springcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:/app.properties")
public class SpringCoreApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringCoreApplication.class);
//        app.setWebApplicationType(WebApplicationType.NONE); // web 관련 타입을 꺼줌 (현재 앱에 필요없으므로)
        app.run(args);
    }

}
