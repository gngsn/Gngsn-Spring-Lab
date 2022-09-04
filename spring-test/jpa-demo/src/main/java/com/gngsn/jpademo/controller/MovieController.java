package com.gngsn.jpademo.controller;

import com.gngsn.jpademo.dto.PagingDTO;
import com.gngsn.jpademo.entity.Genre;
import com.gngsn.jpademo.entity.Movie;
import com.gngsn.jpademo.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @RequestMapping("/movies")
    public String thymeleaf(PagingDTO pagingDTO, Model model) {

        if (pagingDTO.getSize() < 1) {
            pagingDTO.setSize(20);
        }

        Page<Movie> movieList = movieService.getMovieList(pagingDTO);
        List<Genre> genreList = movieService.getMovieGenreList();

        model.addAttribute("movieList", movieList.get().collect(Collectors.toList()));
        pagingDTO.setTotal(movieList.getTotalElements(), movieList.getTotalPages());
        model.addAttribute("paging", pagingDTO);
        model.addAttribute("genreList", genreList);

        log.info("genreList: {}, pagingDTO: {}", genreList, pagingDTO);
        return "movies/index";
    }
}
