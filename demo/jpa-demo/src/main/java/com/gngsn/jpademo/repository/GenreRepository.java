package com.gngsn.jpademo.repository;

import com.gngsn.jpademo.entity.Genre;

import java.util.List;

public interface GenreRepository {

    List<Genre> findTopOrderByGenreIdAscLimitedTo(int limit);
}