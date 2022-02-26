package com.gngsn.springcore.environment;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("test")
public class TestBookRepository implements BookRepository {
}

interface BookRepository {}