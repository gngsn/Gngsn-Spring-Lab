package com.gngsn.jpademo.repository;

import com.gngsn.jpademo.entity.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieGenreRepository extends JpaRepository<MovieGenre, String> {

    List<MovieGenre> findByMovie_MovieIdIn(List<Long> movieId);

//    List<Genre> findByMovieIdIn(List<Long> movieId);

}