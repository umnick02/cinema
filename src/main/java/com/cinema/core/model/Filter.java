package com.cinema.core.model;

public class Filter {
    private String[] genresIncl;
    private String[] genresExcl;
    private String[] countriesIncl;
    private String[] countriesExcl;
    private Double minIMDB;
    private Double minKP;
    private Integer minVotes;
    private Short fromYear;
    private Short toYear;
    private Boolean isSeriesIncl;
    private int page = 0;
    private int size = 24;

    public String[] getGenresIncl() {
        return genresIncl;
    }

    public void setGenresIncl(String... genresIncl) {
        this.genresIncl = genresIncl;
    }

    public String[] getGenresExcl() {
        return genresExcl;
    }

    public void setGenresExcl(String... genresExcl) {
        this.genresExcl = genresExcl;
    }

    public String[] getCountriesIncl() {
        return countriesIncl;
    }

    public void setCountriesIncl(String... countriesIncl) {
        this.countriesIncl = countriesIncl;
    }

    public String[] getCountriesExcl() {
        return countriesExcl;
    }

    public void setCountriesExcl(String... countriesExcl) {
        this.countriesExcl = countriesExcl;
    }

    public Double getMinIMDB() {
        return minIMDB;
    }

    public void setMinIMDB(Double minIMDB) {
        this.minIMDB = minIMDB;
    }

    public Double getMinKP() {
        return minKP;
    }

    public void setMinKP(Double minKP) {
        this.minKP = minKP;
    }

    public Integer getMinVotes() {
        return minVotes;
    }

    public void setMinVotes(Integer minVotes) {
        this.minVotes = minVotes;
    }

    public Short getFromYear() {
        return fromYear;
    }

    public void setFromYear(Short fromYear) {
        this.fromYear = fromYear;
    }

    public Short getToYear() {
        return toYear;
    }

    public void setToYear(Short toYear) {
        this.toYear = toYear;
    }

    public Boolean getSeriesIncl() {
        return isSeriesIncl;
    }

    public void setSeriesIncl(Boolean seriesIncl) {
        isSeriesIncl = seriesIncl;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
