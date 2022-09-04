package com.gngsn.jpademo.repository;

import com.gngsn.jpademo.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends PagingAndSortingRepository<Movie, Long> {

    @Override
    Page<Movie> findAll(Pageable pageable);

    //    List<MovieVO> findByName(String name);

//    List<MovieVO> findByNameLike(String keyword);
}