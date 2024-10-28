package com.rdev.freetube.database;

import android.content.Context;

import java.util.List;

public class MovieRepository {
    private MovieDao movieDao;

    public MovieRepository(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        movieDao = db.movieDao();
    }

    public void addFavorite(Movie movie) {
        new Thread(() -> movieDao.addFavorite(movie)).start();
    }

    public void removeFavorite(Movie movie) {
        new Thread(() -> movieDao.removeFavorite(movie)).start();
    }

    public List<Movie> getFavoriteMovies() {
        return movieDao.getFavoriteMovies();
    }

    public Movie getMovieById(int id) {
        return movieDao.getMovieById(id);
    }
}
