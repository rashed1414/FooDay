package com.example.fooday.ui.application.admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fooday.ui.application.admin.ui.AddFoodFragment;
import com.example.fooday.ui.application.admin.ui.UserPreviewFragment;

public class VPAdapter extends FragmentStateAdapter {


    public VPAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new UserPreviewFragment();
            case 1:
                return new AddFoodFragment();

            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
