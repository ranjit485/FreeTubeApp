package com.rdev.freetube.adaptor;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.rdev.freetube.fragment.ActionTab;
import com.rdev.freetube.fragment.ComedyTab;
import com.rdev.freetube.fragment.HotTab;
import com.rdev.freetube.fragment.RomanceTab;
import com.rdev.freetube.fragment.SciFiTab;
import com.rdev.freetube.fragment.TabHorror;
import com.rdev.freetube.fragment.home;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull home fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Create and return the correct Fragment for each tab
        switch (position) {
            case 0:
                return new HotTab();
            case 1:
                return new ActionTab();
            case 2:
                return new ComedyTab();
            case 3:
                return new TabHorror();
            case 4:
                return new SciFiTab();
            case 5:
                return new RomanceTab();
            default:
                return new HotTab();
        }
    }

    @Override
    public int getItemCount() {
        return 5; // Update this count based on the number of tabs
    }
}
