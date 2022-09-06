package com.gngsn.jpademo.dto;

import com.gngsn.jpademo.entity.Genre;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MovieDTO {

    private Long movieId;

    private String title;
    private int budget;
    private String homepage;
    private String overview;
    private double popularity;
    private LocalDate releaseDate;
    private int revenue;
    private int runtime;
    private String movieStatus;
    private String tagline;
    private double voteAverage;
    private int voteCount;

    private List<Genre> genres;
}