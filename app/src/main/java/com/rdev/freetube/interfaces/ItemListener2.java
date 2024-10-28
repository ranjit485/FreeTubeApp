package com.rdev.freetube.interfaces;

import com.rdev.freetube.database.Movie;
import com.rdev.freetube.modal.HomeVideoModal;

public interface ItemListener2 {
    void onItemClick(Movie movie);
    void onRemoveFavClick(Movie movie);

}
