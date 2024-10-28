package com.rdev.freetube.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert
    void addFavorite(Movie movie);

    @Delete
    void removeFavorite(Movie movie);

    @Query("SELECT * FROM favorite_movies")
    List<Movie> getFavoriteMovies();

    @Query("SELECT * FROM favorite_movies WHERE id = :id LIMIT 1")
    Movie getMovieById(int id);
}
