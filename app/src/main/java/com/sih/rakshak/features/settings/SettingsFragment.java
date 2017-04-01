package com.sih.rakshak.features.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sih.rakshak.R;
import com.sih.rakshak.base.BaseFragment;
import com.sih.rakshak.features.FragmentIds;

import butterknife.ButterKnife;

/**
 * Created by Batdroid on 1/4/17 for Rakshak.
 */

public class SettingsFragment extends BaseFragment {
    public SettingsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public FragmentIds getFragmentId() {
        return FragmentIds.SETTINGS;
    }

    @Override
    public FragmentIds getBackToFragmentId() {
        return FragmentIds.INBOX;
    }

}
