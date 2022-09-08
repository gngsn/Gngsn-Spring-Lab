package com.gngsn.jpademo.repository.impl;

import com.gngsn.jpademo.entity.Genre;
import com.gngsn.jpademo.repository.GenreRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class GenreRepositoryImpl implements GenreRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Genre> findTopOrderByGenreIdAscLimitedTo(int limit) {
        return entityManager.createQuery("SELECT g FROM Genre g ORDER BY g.genreId",
            Genre.class).setMaxResults(limit).getResultList();
    }
}