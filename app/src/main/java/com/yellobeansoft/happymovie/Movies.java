package com.yellobeansoft.happymovie;

/**
 * Created by Beboyz on 1/10/15 AD.
 */
public class Movies {

    private String movieImg;
    private String movieTitle;
    private String movieTitleTH;
    private int movieLength;
    private String showtime;
    private int rating;
    private String URLInfo;
    private String URLTrailer;

    public String getMovieImg() {
        return movieImg;
    }

    public void setMovieImg(String movieImg) {
        this.movieImg = movieImg;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieTitleTH() {
        return movieTitleTH;
    }

    public void setMovieTitleTH(String movieTitleTH) {
        this.movieTitleTH = movieTitleTH;
    }

    public int getMovieLength() {
        return movieLength;
    }

    public void setMovieLength(int movieLength) {
        this.movieLength = movieLength;
    }

    public String getShowtime() {
        return showtime;
    }

    public void setShowtime(String showtime) {
        this.showtime = showtime;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getURLInfo() {
        return URLInfo;
    }

    public void setURLInfo(String URLInfo) {
        this.URLInfo = URLInfo;
    }

    public String getURLTrailer() {
        return URLTrailer;
    }

    public void setURLTrailer(String URLTrailer) {
        this.URLTrailer = URLTrailer;
    }
}