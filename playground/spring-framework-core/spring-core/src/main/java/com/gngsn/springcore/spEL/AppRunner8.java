package com.gngsn.springcore.spEL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

//@Component
public class AppRunner8 implements ApplicationRunner {

    @Value("#{ 1+1 }")
    int value;

    @Value("#{'hello ' + 'world'}")
    String greeting;

    @Value("#{1 eq 1}")
    String tf;

    @Value("hello")
    String hello;

    // properties
    @Value("${my.value}")
    int myValue;

    @Value("#{${my.value} eq 100}")
    boolean myValueTf100;

    @Value("#{sample.data}")
    int sampleData;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("======= SpEL AppRunner =======");
        System.out.println(value);
        System.out.println(greeting);
        System.out.println(tf);
        System.out.println(myValue);
        System.out.println(myValueTf100);
        System.out.println(sampleData);

        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("2 + 100"); // 안의 스트링 자체가 익스프레션
        Integer value = expression.getValue(Integer.class);
        System.out.println(value);
    }
}
