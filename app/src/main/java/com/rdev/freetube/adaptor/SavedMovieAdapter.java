package com.rdev.freetube.adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rdev.freetube.R;
import com.rdev.freetube.database.Movie;
import com.rdev.freetube.interfaces.ItemListener;
import com.rdev.freetube.interfaces.ItemListener2;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SavedMovieAdapter extends RecyclerView.Adapter<SavedMovieAdapter.ViewHolder>{

    private List<Movie> movieList;
    private ItemListener2 listener;



    public SavedMovieAdapter(List<Movie> movieList, ItemListener2 listener) {
        this.movieList = movieList;
        this.listener =listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_video_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movieModal = movieList.get(position);

        Picasso.get()
                .load(movieModal.getMovieThumbnail()) // Assuming you have a method to get the URL of the thumbnail
                .placeholder(R.drawable.loading_image_bg) // Optional placeholder image
                .error(R.drawable.loading_image_bg) // Optional error image
                .into(holder.imageViewThumbnail);

        Picasso.get()
                .load(movieModal.getChannelLogo()) // Assuming you have a method to get the URL of the thumbnail
                .placeholder(R.drawable.ic_avatar_circle) // Optional placeholder image
                .error(R.drawable.ic_avatar_circle) // Optional error image
                .into(holder.channelLogo);


        holder.textViewTitle.setText(movieModal.getMovieName());
        holder.duration.setText(movieModal.getMovieDuration());
//        holder.channelName.setText(movieModal.getUploadedBy());

        holder.removeFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRemoveFavClick(movieList.get(position));
                removeItem(position); // Remove the item from the adapter's data set
            }
        });

        holder.imageViewThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(movieList.get(position));
            }
        });
    }

    public void removeItem(int position) {
        movieList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, movieList.size());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewThumbnail;
        Button removeFav;

        CardView card;
        TextView textViewTitle;
        TextView duration;
        TextView channelName;
        TextView textViewLikes;
        ImageView channelLogo;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            removeFav = itemView.findViewById(R.id.remove_fav);
            imageViewThumbnail = itemView.findViewById(R.id.home_video_thumbnail);
            textViewTitle = itemView.findViewById(R.id.home_video_title);
            channelName = itemView.findViewById(R.id.channelName);
            duration = itemView.findViewById(R.id.duration);
            channelLogo = itemView.findViewById(R.id.channelLogo);
        }
    }

}
