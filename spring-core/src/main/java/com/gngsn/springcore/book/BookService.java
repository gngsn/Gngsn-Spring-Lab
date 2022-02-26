package com.gngsn.springcore.book;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class BookService {

    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

//    public Book save(Book book) {
//        book.setCreated(new Date())
//    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("============= CREATE BOOK SERVICE BEAN ============");
    }
}