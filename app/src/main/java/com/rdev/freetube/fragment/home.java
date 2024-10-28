package com.rdev.freetube.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.rdev.freetube.MainActivity;
import com.rdev.freetube.R;
import com.rdev.freetube.adaptor.ViewPagerAdapter;

import java.util.Objects;

public class home extends Fragment  {

    MainActivity mainActivity;
    TabLayout tabLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        tabLayout = view.findViewById(R.id.home_category_tab_layout);
        ViewPager2 viewPager2 = view.findViewById(R.id.home_view_pager);

        mainActivity = (MainActivity) getActivity();
        mainActivity.showAppBar();


        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(adapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Objects.requireNonNull(tabLayout.getTabAt(position)).select();
            }
        });

    return view;
    }

    public void showTabLayout() {
        tabLayout.setVisibility(View.VISIBLE);
    }

    public void hideTabLayout() {
        tabLayout.setVisibility(View.GONE);
    }

}