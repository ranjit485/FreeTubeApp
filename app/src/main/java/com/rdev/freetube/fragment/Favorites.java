package com.rdev.freetube.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rdev.freetube.MainActivity;
import com.rdev.freetube.R;
import com.rdev.freetube.adaptor.SavedMovieAdapter;
import com.rdev.freetube.database.Movie;
import com.rdev.freetube.database.MovieRepository;
import com.rdev.freetube.interfaces.ItemListener2;

import java.util.List;

public class Favorites extends Fragment implements ItemListener2 {

    private MovieRepository repository;
    private RecyclerView recyclerView;
    private SavedMovieAdapter savedMovieAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_saved, container, false);


        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.showAppBar();

        repository = new MovieRepository(getContext());
        recyclerView = rootView.findViewById(R.id.videos_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Handler handler = new Handler(Looper.getMainLooper());

        new Thread(() -> {
            List<Movie> favoriteMovies = repository.getFavoriteMovies();
            handler.post(() -> {
                savedMovieAdapter = new SavedMovieAdapter(favoriteMovies, this);
                recyclerView.setAdapter(savedMovieAdapter);
            });
        }).start();


        return rootView;
    }

    @Override
    public void onItemClick(Movie movie) {
    // Assuming you have a Movie object from intent or other sources
        Toast.makeText(getContext(), movie.getMovieName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRemoveFavClick(Movie movie) {
        repository.removeFavorite(movie);
        Toast.makeText(getContext(), "Removed", Toast.LENGTH_SHORT).show();
    }
}