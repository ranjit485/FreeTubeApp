package com.rdev.freetube.interfaces;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.rdev.freetube.modal.HomeVideoModal;

public interface ItemListener {
    void onItemClick(HomeVideoModal homeVideoModal);
}
