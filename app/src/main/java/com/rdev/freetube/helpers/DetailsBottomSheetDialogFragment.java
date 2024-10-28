package com.rdev.freetube.helpers;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rdev.freetube.R;
import com.squareup.picasso.Picasso;

public class DetailsBottomSheetDialogFragment extends BottomSheetDialogFragment {

    View targetView;
    String videoTitle;
    String videoDesc;
    String videoViews;
    String videoCategory;
    String channelName;
    String channelLogo;
    String linkVideo;
    String videoLanguage;

    public DetailsBottomSheetDialogFragment(String channelName,String channelLogo, String videoTitle, String videoDesc, String videoViews, String videoCategory, String linkVideo,String videoLanguage) {
        this.channelLogo = channelLogo;
        this.channelName = channelName;
        this.videoLanguage = videoLanguage;
        this.videoTitle = videoTitle;
        this.videoDesc = videoDesc;
        this.videoViews = videoViews;
        this.videoCategory = videoCategory;
        this.linkVideo = linkVideo;
    }

    @Override
    public int getTheme() {
        return R.style.Theme_MyBottomSheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootv = inflater.inflate(R.layout.details_bottom_sheet, container, false);

        TextView language = rootv.findViewById(R.id.language);
        TextView category = rootv.findViewById(R.id.category);
        TextView videoDesc = rootv.findViewById(R.id.videoDesc);
        videoDesc.setMinLines(20);

        videoDesc.setText(this.videoDesc);
        language.setText(this.videoLanguage);
        category.setText(this.videoCategory);

        return rootv;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);



        // Set up behavior when the dialog is shown
        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
            FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                // Calculate height from top of screen to top of targetView
                int targetViewTop = targetView.getTop();
                int windowHeight = getResources().getDisplayMetrics().heightPixels;
                int desiredHeight = windowHeight - targetViewTop;

                // Adjust bottom sheet height dynamically
                ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
                layoutParams.height = desiredHeight-15;
                bottomSheet.setLayoutParams(layoutParams);
                // Optionally, adjust behavior settings
                BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setSkipCollapsed(true);
            }
        });

        return dialog;
    }

    // Setter method to set the target view from outside
    public void setTargetView(View targetView) {
        this.targetView = targetView;
    }
}
