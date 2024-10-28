package com.rdev.freetube.adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.rdev.freetube.R;
import com.rdev.freetube.interfaces.ItemListener;
import com.rdev.freetube.modal.HomeVideoModal;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideosAdaptor extends RecyclerView.Adapter<VideosAdaptor.ViewHolder> {

    private List<HomeVideoModal> homeVideoModalList;
    private ItemListener listener;

    public VideosAdaptor(List<HomeVideoModal> homeVideoModalList, ItemListener listener) {
        this.homeVideoModalList = homeVideoModalList;
        this.listener =listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_video_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeVideoModal homeVideoModal = homeVideoModalList.get(position);

        Picasso.get()
                .load(homeVideoModal.getMovieThumbnail()) // Assuming you have a method to get the URL of the thumbnail
                .into(holder.imageViewThumbnail);

        Picasso.get()
                .load(homeVideoModal.getChannelLogo()) // Assuming you have a method to get the URL of the thumbnail
                .placeholder(R.drawable.ic_avatar_circle) // Optional placeholder image
                .error(R.drawable.ic_avatar_circle) // Optional error image
                .into(holder.channelLogo);


        holder.textViewTitle.setText(homeVideoModal.getMovieName());
        holder.duration.setText(homeVideoModal.getMovieDuration());
        holder.channelName.setText(homeVideoModal.getUplodedBy());

        holder.imageViewThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(homeVideoModalList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeVideoModalList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageViewThumbnail;

    CardView card;
    TextView textViewTitle;
    TextView duration;
    TextView channelName;
    TextView textViewLikes;
    ImageView channelLogo;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        imageViewThumbnail = itemView.findViewById(R.id.home_video_thumbnail);
        textViewTitle = itemView.findViewById(R.id.home_video_title);
        channelName = itemView.findViewById(R.id.channelName);
        duration = itemView.findViewById(R.id.duration);
        channelLogo = itemView.findViewById(R.id.channelLogo);
    }
}
}
