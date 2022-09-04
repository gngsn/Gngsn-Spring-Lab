package com.gngsn.jpademo.service;

import com.gngsn.jpademo.dto.PagingDTO;
import com.gngsn.jpademo.entity.Movie;
import org.springframework.data.domain.Page;

public interface MovieService {

    Page<Movie> getMovieList(PagingDTO pagingDTO);
}
