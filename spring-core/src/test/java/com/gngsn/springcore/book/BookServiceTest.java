package com.gngsn.springcore.book;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class BookServiceTest {

    @Mock  // 가짜 객체
    BookRepository bookRepository;

    @Test
    public void save() {
        BookRepository bookRepository = new BookRepository();
        BookService bookService = new BookService(bookRepository);
    }
}