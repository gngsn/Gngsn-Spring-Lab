package com.gngsn.springcore.book;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    // Autowired
//    1. @Primary 설정 가져오기
//    @Autowired
//    private BookRepository bookRepository;

//    2. Qualifier
//    @Autowired @Qualifier("firstBookRepository")
//    private BookRepository bookRepository;

//    3. 이름으로 표시
//    @Autowired
//    private BookRepository firstBookRepository;
    @Autowired
    private BookRepository bookRepository;

    // 생성자 의존성 주입
//    @Autowired
//    public BookService(BookRepository bookRepository) {
//        this.bookRepository = bookRepository;
//    }

    // Setter 주
//    @Autowired
//    public void setBookRepository(BookRepository bookRepository) {
//        this.bookRepository = bookRepository;
//    }


    public void print() {
        System.out.println("bookRepository : " + this.bookRepository.getClass());
    }

    // Bean Lifecycle Interface
//    @PostConstruct
//    public void postConstruct() {
//        System.out.println("============= CREATE BOOK SERVICE BEAN ============");
//    }

    // InitializingBean 을 구현할 경우
//    @Override
//    public void afterPropertiesSet() throws Exception {
//    }
}