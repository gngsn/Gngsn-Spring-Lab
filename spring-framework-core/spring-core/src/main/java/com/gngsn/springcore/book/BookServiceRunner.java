package com.gngsn.springcore.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BookServiceRunner implements ApplicationRunner {

    @Autowired
    BookService bookService;

    @Autowired
    ApplicationContext applicationContext;

    // 2. 혹은 AutowiredAnnotationBeanPostProcessor가 Bean으로 등록되었다는 것을 확인하기 위해 직접 Autowired를 사용할 수도 있음
//    @Autowired
//    AutowiredAnnotationBeanPostProcessor getBeans;

    @Override
    public void run(ApplicationArguments args) {
        bookService.print();
        this.print();
    }

    public void print() {
        // BeanFactory 가 자신에게 등록된 BeanPostProcessor들을 찾는데,
        // 그 중 하나가 AutowiredAnnotationBeanPostProcessor이며 Bean으로 등록되어짐.
        // 1. 위의 내용을 증명하기 위한 코드 - Bean 으로 등록되어있는지 확인
        AutowiredAnnotationBeanPostProcessor bean = applicationContext.getBean(AutowiredAnnotationBeanPostProcessor.class);
        System.out.println(bean);
    }

}
