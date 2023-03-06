package com.gngsn.s3batch.vo;

import java.time.LocalDate;

public class Movie {
    private String movieId;
    private String title;
    private long budget;
    private String overview;
    private String runtime;
    private String revenue;
    private String movieStatus;

    public Movie() {}

    public Movie(String movieId, String title, long budget, String overview, String runtime, String revenue, String movieStatus) {
        this.movieId = movieId;
        this.title = title;
        this.budget = budget;
        this.overview = overview;
        this.runtime = runtime;
        this.revenue = revenue;
        this.movieStatus = movieStatus;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getRevenue() {
        return revenue;
    }

    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }

    public String getMovieStatus() {
        return movieStatus;
    }

    public void setMovieStatus(String movieStatus) {
        this.movieStatus = movieStatus;
    }
}
