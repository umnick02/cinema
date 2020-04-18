package com.cinema.core.model;

import com.cinema.core.entity.Movie;

import java.util.Arrays;

public class Filter {

    private String title = "Во все тяжкие";
    private String[] genresIncl;
    private String[] genresExcl;
    private String[] countriesIncl;
    private String[] countriesExcl;
    private Double minIMDB;
    private Double minKP;
    private Integer minVotes;
    private Short fromYear;
    private Short toYear;
    private Movie.Type[] types;

    private Filter() {}

    public String getTitle() {
        return title;
    }

    public String[] getGenresIncl() {
        return genresIncl;
    }

    public String[] getGenresExcl() {
        return genresExcl;
    }

    public String[] getCountriesIncl() {
        return countriesIncl;
    }

    public String[] getCountriesExcl() {
        return countriesExcl;
    }

    public Double getMinIMDB() {
        return minIMDB;
    }

    public Double getMinKP() {
        return minKP;
    }

    public Integer getMinVotes() {
        return minVotes;
    }

    public Short getFromYear() {
        return fromYear;
    }

    public Short getToYear() {
        return toYear;
    }

    public Movie.Type[] getTypes() {
        return types;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "title=" + title +
                ", genresIncl=" + Arrays.toString(genresIncl) +
                ", genresExcl=" + Arrays.toString(genresExcl) +
                ", countriesIncl=" + Arrays.toString(countriesIncl) +
                ", countriesExcl=" + Arrays.toString(countriesExcl) +
                ", minIMDB=" + minIMDB +
                ", minKP=" + minKP +
                ", minVotes=" + minVotes +
                ", fromYear=" + fromYear +
                ", toYear=" + toYear +
                ", types=" + Arrays.toString(types) +
                '}';
    }
    
    public static class Builder {
        
        private Filter filter = new Filter();

        public Filter build() {
            return filter;
        }

        public Builder fromYear(short year) {
            filter.fromYear = year;
            return this;
        }

        public Builder toYear(short year) {
            filter.toYear = year;
            return this;
        }

        public Builder minKP(double rating) {
            filter.minKP = rating;
            return this;
        }

        public Builder minIMDB(double rating) {
            filter.minIMDB = rating;
            return this;
        }

        public Builder minVotes(int votes) {
            filter.minVotes = votes;
            return this;
        }

        public Builder titleLike(String title) {
            filter.title = title;
            return this;
        }

        public Builder types(Movie.Type... types) {
            filter.types = types;
            return this;
        }

        public Builder genresIncl(String... genres) {
            filter.genresIncl = genres;
            return this;
        }

        public Builder genresExcl(String... genres) {
            filter.genresExcl = genres;
            return this;
        }

        public Builder countriesIncl(String... countries) {
            filter.countriesIncl = countries;
            return this;
        }

        public Builder countriesExcl(String... countries) {
            filter.countriesExcl = countries;
            return this;
        }
    }
}
