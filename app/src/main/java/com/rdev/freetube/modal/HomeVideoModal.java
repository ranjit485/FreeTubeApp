package com.rdev.freetube.modal;

public class HomeVideoModal {

    String movieId;
    String movieName;
    String channelLogo;
    String movieThumbnail;
    String movieAbout;
    String movieDuration;
    String uplodedBy;

    public HomeVideoModal(String movieId, String movieName, String channelLogo, String movieThumbnail, String movieAbout, String movieDuration, String uplodedBy) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.channelLogo = channelLogo;
        this.movieThumbnail = movieThumbnail;
        this.movieAbout = movieAbout;
        this.movieDuration = movieDuration;
        this.uplodedBy = uplodedBy;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getChannelLogo() {
        return channelLogo;
    }

    public String getMovieThumbnail() {
        return movieThumbnail;
    }

    public String getMovieAbout() {
        return movieAbout;
    }

    public String getMovieDuration() {
        return movieDuration;
    }

    public String getUplodedBy() {
        return uplodedBy;
    }
}
