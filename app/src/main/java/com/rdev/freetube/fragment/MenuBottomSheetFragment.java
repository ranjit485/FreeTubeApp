package com.rdev.freetube.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rdev.freetube.R;

public class MenuBottomSheetFragment extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_bottom_sheet, container, false);

//        view.findViewById(R.id.menu_item_1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle Menu Item 1 click
//            }
//        });
//
//        view.findViewById(R.id.menu_item_2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle Menu Item 2 click
//            }
//        });
//
//        view.findViewById(R.id.menu_item_3).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle Menu Item 3 click
//            }
//        });

        return view;
    }
}
