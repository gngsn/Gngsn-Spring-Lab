package com.gngsn.jpademo.service;

import com.gngsn.jpademo.dto.PagingDTO;
import com.gngsn.jpademo.repository.MovieRepository;
import com.gngsn.jpademo.vo.MovieVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public Page<MovieVO> getMovieList(PagingDTO pagingDTO) {
        return movieRepository.findAll(PageRequest.of(pagingDTO.getPage(), 20));
    }
}
