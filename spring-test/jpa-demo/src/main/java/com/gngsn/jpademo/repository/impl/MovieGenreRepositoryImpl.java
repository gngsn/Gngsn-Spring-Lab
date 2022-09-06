package com.gngsn.jpademo.repository.impl;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MovieGenreRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

//    public List<MovieGenre> findByMovie_MovieIdIn(List<Long> movieIds) {
//        return entityManager
//            .createQuery(
//                "SELECT m, g FROM MovieGenre m INNER JOIN Genre g ON m.genre.genreId = g.genreId WHERE m.movie.movieId IN (:movieIds)", MovieGenre.class)
////                "SELECT m.movie.movieId, g.genreName FROM MovieGenre m INNER JOIN Genre g ON m.genre.genreId = g.genreId WHERE m.movie.movieId IN (?1)", MovieGenre.class)
////            .setParameter(1, movieIds)
//            .setParameter("movieIds", movieIds.toArray())
//            .getResultList();
//    }

}


/*

select
 moviegenre0_.movie_id as col_0_0_,
 genre1_.genre_name as col_1_0_
 from movie_genres moviegenre0_
    inner join genre genre1_
    on (moviegenre0_.genre_id=genre1_.genre_id)
 where moviegenre0_.movie_id in (?)
 */