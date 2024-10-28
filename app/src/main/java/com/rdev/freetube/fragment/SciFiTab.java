package com.rdev.freetube.fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.rdev.freetube.MainActivity.PREFS_NAME;
import static com.rdev.freetube.MainActivity.PREF_LANGUAGE_KEY;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rdev.freetube.R;
import com.rdev.freetube.api.MovieFetcher;

public class SciFiTab extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.child_home, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.videos_rv);
        assert getParentFragment() != null;
        FragmentManager fragmentManager = getParentFragment().getParentFragmentManager(); // Use getParentFragmentManager() for fragments

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String currentLanguage = sharedPreferences.getString(PREF_LANGUAGE_KEY, "English");

        // Get Related Movies
        String url = "https://raw.githubusercontent.com/ranjit485/FreeTube/main/"+currentLanguage+"/Action.json";
        MovieFetcher movieFetcher = new MovieFetcher();
        movieFetcher.setFragmentManager(fragmentManager);
        movieFetcher.getMovies(getContext(), url, rootView,recyclerView);
        return rootView;
    }

}
