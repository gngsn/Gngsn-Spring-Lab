package com.gngsn.jpademo.service;

import com.gngsn.jpademo.dto.PagingDTO;
import com.gngsn.jpademo.entity.Genre;
import com.gngsn.jpademo.entity.Movie;
import com.gngsn.jpademo.repository.MovieRepository;
import com.gngsn.jpademo.repository.impl.GenreRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepositoryImpl genreRepository;

    @Override
    public Page<Movie> getMovieList(PagingDTO pagingDTO) {
        return movieRepository.findAll(PageRequest.of(pagingDTO.getPage(), 20));
    }

    @Override
    public List<Genre> getMovieGenreList() {
        return genreRepository.findTopOrderByGenreIdAscLimitedTo(15);
    }
}
