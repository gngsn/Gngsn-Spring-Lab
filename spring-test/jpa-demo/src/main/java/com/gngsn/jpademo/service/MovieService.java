package com.gngsn.jpademo.service;

import com.gngsn.jpademo.dto.PagingDTO;
import com.gngsn.jpademo.vo.MovieVO;
import org.springframework.data.domain.Page;

public interface MovieService {

    Page<MovieVO> getMovieList(PagingDTO pagingDTO);
}
