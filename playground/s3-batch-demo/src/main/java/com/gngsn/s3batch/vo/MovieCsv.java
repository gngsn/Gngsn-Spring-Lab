package com.gngsn.s3batch.vo;

import java.time.LocalDate;

public class MovieCsv {
    private String movieId;
    private String title;
    private String budget;
    private String overview;
    private String runtime;
    private String revenue;
    private String movieStatus;

    public MovieCsv() {}

    public MovieCsv(String movieId, String title, String budget, String overview, String runtime, String revenue, String movieStatus) {
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

    public String getTitle() {
        return title;
    }

    public String getBudget() {
        return budget;
    }

    public String getOverview() {
        return overview;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getRevenue() {
        return revenue;
    }

    public String getMovieStatus() {
        return movieStatus;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }

    public void setMovieStatus(String movieStatus) {
        this.movieStatus = movieStatus;
    }
}
