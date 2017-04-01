package com.sih.rakshak.base;

import android.support.v4.app.Fragment;

import com.sih.rakshak.features.FragmentIds;

/**
 * Created by Batdroid on 1/4/17 for Rakshak.
 */

public abstract class BaseFragment extends Fragment{
    abstract public FragmentIds getFragmentId();

    abstract public FragmentIds getBackToFragmentId();

}
