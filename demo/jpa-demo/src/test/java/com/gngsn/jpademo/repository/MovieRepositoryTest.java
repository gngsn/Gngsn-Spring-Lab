package com.gngsn.jpademo.repository;

import com.gngsn.jpademo.entity.Movie;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@SpringBootTest
//@TestPropertySource(locations = {"classpath:jdbc.properties"})
class MovieRepositoryTest {
    private final static Logger log = LoggerFactory.getLogger(MovieRepositoryTest.class);

    @Autowired
    MovieRepository movieRepository;

    @Test
    void findAll() {
        Page<Movie> moviePage = movieRepository.findAll(Pageable.ofSize(20));

        log.info("\n\nmovie page information: {}\n", moviePage.toString());
        moviePage.forEach(movie -> log.info(movie.toString()));

    }
}