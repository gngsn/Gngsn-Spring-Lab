package com.gngsn.jpademo.repository;

import com.gngsn.jpademo.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends PagingAndSortingRepository<Movie, Long> {
    Page<Movie> findAll(Pageable pageable);

    List<MovieId> findMovieIdTop20();
}

