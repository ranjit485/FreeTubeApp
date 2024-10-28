package com.rdev.freetube.api;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.rdev.freetube.R;
import com.rdev.freetube.adaptor.VideosAdaptor;
import com.rdev.freetube.fragment.Player;
import com.rdev.freetube.interfaces.ItemListener;
import com.rdev.freetube.modal.HomeVideoModal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieFetcher extends Fragment implements ItemListener {
    private List<HomeVideoModal> videosModalList;
    private VideosAdaptor videosAdaptor;
    private FragmentManager fragmentManager;
    private ShimmerFrameLayout shimmerView;

    public MovieFetcher() {
        // Default constructor
    }

    public void getMovies(Context context, String apiUrl, View view, RecyclerView recyclerView) {
        videosModalList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        videosAdaptor = new VideosAdaptor(videosModalList, this);
        recyclerView.setAdapter(videosAdaptor);
        shimmerView = view.findViewById(R.id.shimmer_view_container);

        shimmerView.startShimmer();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, apiUrl, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        shimmerView.stopShimmer();
                        shimmerView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject videoObject = response.getJSONObject(i);
                                HomeVideoModal homeVideoModal = new HomeVideoModal(
                                        videoObject.getString("videoId"),
                                        videoObject.getString("title"),
                                        videoObject.getString("channelLogo"),
                                        videoObject.getString("thumbnail"),
                                        videoObject.getString("description"),
                                        videoObject.getString("duration"),
                                        videoObject.getString("channelName")
                                );
                                videosModalList.add(homeVideoModal);
                            }
                            videosAdaptor.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Failed to parse data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        shimmerView.stopShimmer();
                        shimmerView.setVisibility(View.GONE);
                        System.out.println(error);
                        Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    }
                });

        MySingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

    @Override
    public void onItemClick(HomeVideoModal homeVideoModal) {
        if (fragmentManager != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Player newFragment = new Player(homeVideoModal.getMovieId(), homeVideoModal.getMovieThumbnail(), homeVideoModal.getMovieDuration(), homeVideoModal.getMovieName(), homeVideoModal.getMovieAbout(), "100k", "Hindi", homeVideoModal.getUplodedBy(), homeVideoModal.getChannelLogo());
            fragmentTransaction.replace(R.id.main_fragment_container, newFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (shimmerView != null) {
            shimmerView.startShimmer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (shimmerView != null) {
            shimmerView.stopShimmer();
        }
    }
}
