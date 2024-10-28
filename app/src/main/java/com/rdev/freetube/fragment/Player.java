package com.rdev.freetube.fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.rdev.freetube.MainActivity.PREFS_NAME;
import static com.rdev.freetube.MainActivity.PREF_LANGUAGE_KEY;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.rdev.freetube.MainActivity;
import com.rdev.freetube.R;
import com.rdev.freetube.adaptor.VideosAdaptor;
import com.rdev.freetube.api.MySingleton;
import com.rdev.freetube.database.Movie;
import com.rdev.freetube.database.MovieRepository;
import com.rdev.freetube.helpers.DetailsBottomSheetDialogFragment;
import com.rdev.freetube.helpers.PlayerUiController;
import com.rdev.freetube.interfaces.ItemListener;
import com.rdev.freetube.modal.HomeVideoModal;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Player extends Fragment implements ItemListener{
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer youTubePlayer;
    private String videoId, videoTitle, videoDescription, videoLanguage;
    private String videoThumbnailUrl;
    private String videoDuration;
    private String views;
    private String channelName;
    private String channelLogo;
    private MainActivity mainActivity;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private RecyclerView relatedVideoRecyclerView;
    private List<HomeVideoModal> videosModalList;
    private VideosAdaptor videosAdaptor;
    private boolean isPlayerInitialized = false;
    ImageView channelLogoView;
    TextView chName;
    TextView movieTitle;
    ImageView fullscreenButton;
    private ProgressBar progressBar;

    private MovieRepository repository;
    private Movie currentMovie;

    ShimmerFrameLayout shimmerView;


    public Player(String videoId,String videoThumbnailUrl,String videoDuration, String videoTitle, String videoDescription, String views, String videoLanguage, String channelName, String channelLogo) {
        this.videoDuration = videoDuration;
        this.videoThumbnailUrl =videoThumbnailUrl;
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.videoDescription = videoDescription;
        this.views = views;
        this.videoLanguage = videoLanguage;
        this.channelName = channelName;
        this.channelLogo = channelLogo;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.hideAppBar();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mainActivity != null) {
            mainActivity.showAppBar();
        }
        if (youTubePlayerView != null) {
            youTubePlayerView.release();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);

        progressBar = rootView.findViewById(R.id.progress_bar);

        // Show the progress bar before loading
        progressBar.setVisibility(View.VISIBLE);

        shimmerView = rootView.findViewById(R.id.shimmer_view_container);
        shimmerView.startShimmer();

        channelLogoView = rootView.findViewById(R.id.channel_logo_holder);
        chName = rootView.findViewById(R.id.channelName);
        movieTitle = rootView.findViewById(R.id.home_video_title);

        Button descToggleButton = rootView.findViewById(R.id.toggle_bottom_sheet_button);
        descToggleButton.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
            DetailsBottomSheetDialogFragment bottomSheet = new DetailsBottomSheetDialogFragment(channelName, channelLogo, videoTitle, videoDescription, views, "Hindi N", videoId, videoLanguage);
            View targetView = rootView.findViewById(R.id.someWhat);
            bottomSheet.setTargetView(targetView);
            bottomSheet.show(getChildFragmentManager(), "MyBottomSheetDialogFragment");

//            if (bottomSheet.getShowsDialog()){
//
//            }
        });

        Button saveButton = rootView.findViewById(R.id.saveVideo);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Save Button Clicked", Toast.LENGTH_SHORT).show();
                // Assuming you have a Movie object from intent or other sources
                currentMovie = new Movie();
                currentMovie.setMovieId(videoId);
                currentMovie.setMovieName(videoTitle);
                currentMovie.setChannelLogo(channelLogo);
                currentMovie.setMovieThumbnail(videoThumbnailUrl);
                currentMovie.setMovieAbout(videoDescription);
                currentMovie.setMovieDuration(videoDuration);
                currentMovie.setUploadedBy(channelName);
                repository.addFavorite(currentMovie);

            }
        });

        youTubePlayerView = rootView.findViewById(R.id.youtube_player_view);
        initYouTubePlayerView(this.videoId,videoTitle,channelLogo,channelName,videoDescription);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String currentLanguage = sharedPreferences.getString(PREF_LANGUAGE_KEY, "English");

        this.relatedVideoRecyclerView = rootView.findViewById(R.id.related_videos_rv);
        String url = "https://raw.githubusercontent.com/ranjit485/FreeTube/main/"+currentLanguage+"/Action.json";
        getRelatedMovies(url);

        repository = new MovieRepository(getContext());


        return rootView;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            youTubePlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            youTubePlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            youTubePlayerView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            youTubePlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
    }

    private void initYouTubePlayerView(String videoId,String movieName,String channelLogo,String uplodedBy, String movieAbout) {

        Picasso.get()
                .load(channelLogo)
                .placeholder(R.drawable.ic_avatar_circle)
                .error(R.drawable.ic_avatar_circle)
                .into(channelLogoView);

        chName.setText(uplodedBy);
        movieTitle.setText(movieName);

        getLifecycle().addObserver(youTubePlayerView);
        if (!isPlayerInitialized) {
            IFramePlayerOptions options = new IFramePlayerOptions.Builder().controls(0).rel(1).fullscreen(1).build();
            try {
                youTubePlayerView.initialize(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onPlaybackQualityChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlaybackQuality playbackQuality) {
                        super.onPlaybackQualityChange(youTubePlayer, playbackQuality);
                    }

                    @Override
                    public void onReady(@NonNull YouTubePlayer initializedYouTubePlayer) {
                        progressBar.setVisibility(View.GONE);
                        youTubePlayer = initializedYouTubePlayer;
                        youTubePlayerView.setVisibility(View.VISIBLE);
                        PlayerUiController playerUiController = new PlayerUiController(mainActivity, youTubePlayerView, youTubePlayer);
                        youTubePlayerView.setCustomPlayerUi(playerUiController.getRootView());
                        YouTubePlayerUtils.loadOrCueVideo(youTubePlayer, getLifecycle(), videoId, 0);
                    }
                }, true,options);
                isPlayerInitialized = true;
            } catch (Exception e) {
                Log.e("YouTubePlayerInit", "Error initializing YouTube player", e);
            }
        } else {
            YouTubePlayerUtils.loadOrCueVideo(youTubePlayer, getLifecycle(), videoId, 0);
        }
    }

    @Override
    public void onItemClick(HomeVideoModal homeVideoModal) {
        initYouTubePlayerView(homeVideoModal.getMovieId(),homeVideoModal.getMovieName(),homeVideoModal.getChannelLogo(),homeVideoModal.getUplodedBy(),homeVideoModal.getMovieAbout());
        this.videoId = homeVideoModal.getMovieId();
        this.videoTitle = homeVideoModal.getMovieName();
        this.videoDescription = homeVideoModal.getMovieAbout();
        this.channelName = homeVideoModal.getUplodedBy();
        this.channelLogo = homeVideoModal.getChannelLogo();
    }

    public void getRelatedMovies(String apiUrl) {
        shimmerView.startShimmer();
        videosModalList = new ArrayList<>();
        relatedVideoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        videosAdaptor = new VideosAdaptor(videosModalList, this);
        relatedVideoRecyclerView.setAdapter(videosAdaptor);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, apiUrl, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        shimmerView.stopShimmer();
                        shimmerView.setVisibility(View.GONE);
                        relatedVideoRecyclerView.setVisibility(View.VISIBLE);
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
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        shimmerView.stopShimmer();
                        shimmerView.setVisibility(View.GONE);
                        Log.e("VolleyError", "Error fetching related videos", error);
                    }
                });

        MySingleton.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);
    }
    @Override
    public void onResume() {
        super.onResume();
            shimmerView.startShimmer();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (shimmerView != null) {
            shimmerView.stopShimmer();
        }
    }

}
