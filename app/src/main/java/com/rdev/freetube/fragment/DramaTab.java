package com.rdev.freetube.fragment;

import static com.rdev.freetube.MainActivity.PREF_LANGUAGE_KEY;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rdev.freetube.R;
import com.rdev.freetube.api.MovieFetcher;

public class DramaTab extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.child_home, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.videos_rv);
        assert getParentFragment() != null;
        FragmentManager fragmentManager = getParentFragment().getParentFragmentManager(); // Use getParentFragmentManager() for fragments

        // Get Related Movies
        String url = "https://raw.githubusercontent.com/ranjit485/FreeTube/main/"+PREF_LANGUAGE_KEY+"/Drama.json";
        MovieFetcher movieFetcher = new MovieFetcher();
        movieFetcher.setFragmentManager(fragmentManager);
        movieFetcher.getMovies(getContext(), url, rootView,recyclerView);

        return rootView;
    }
}
