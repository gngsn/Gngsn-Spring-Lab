package com.gngsn.jpademo.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @Column(name = "movie_id")
    private Long movieId;

    private String title;
    private int budget;
    private String homepage;
    private String overview;
    private double popularity;
    private LocalDate releaseDate;
    private int revenue;
    private int runtime;

    @Column(name = "movie_status")
    private String movieStatus;

    private String tagline;

    @Column(name = "vote_average")
    private double voteAverage;

    @Column(name = "vote_count")
    private int voteCount;
}