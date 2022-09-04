package com.gngsn.jpademo.service;

import com.gngsn.jpademo.dto.PagingDTO;
import com.gngsn.jpademo.entity.Genre;
import com.gngsn.jpademo.entity.Movie;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MovieService {

    Page<Movie> getMovieList(PagingDTO pagingDTO);

    List<Genre> getMovieGenreList();
}
