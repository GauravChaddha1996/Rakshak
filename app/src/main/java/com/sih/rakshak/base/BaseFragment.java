package com.sih.rakshak.base;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.sih.rakshak.features.FragmentIds;

/**
 * Created by Batdroid on 1/4/17 for Rakshak.
 */

public abstract class BaseFragment extends Fragment {
    abstract public FragmentIds getFragmentId();

    abstract public FragmentIds getBackToFragmentId();


    public void loading(View content, View loading, View error) {
        Log.d("TAG", "loading");
        hideView(content);
        hideView(error);
        showView(loading);
    }

    public void error(View content, View loading, View error) {
        Log.d("TAG", "error");
        hideView(content);
        hideView(loading);
        showView(error);
    }

    public void content(View content, View loading, View error) {
        Log.d("TAG", "content");
        hideView(loading);
        hideView(error);
        showView(content);

    }

    public void hideView(View view) {
        view.setVisibility(View.GONE);
    }

    public void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }
}
