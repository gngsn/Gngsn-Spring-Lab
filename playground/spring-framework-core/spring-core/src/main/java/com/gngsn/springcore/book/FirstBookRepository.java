package com.gngsn.springcore.book;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class FirstBookRepository implements BookRepository {
}
