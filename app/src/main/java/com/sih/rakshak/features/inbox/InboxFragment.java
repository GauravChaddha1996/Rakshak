package com.sih.rakshak.features.inbox;

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

public class InboxFragment extends BaseFragment {

    public InboxFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public FragmentIds getFragmentId() {
        return FragmentIds.INBOX;
    }

    @Override
    public FragmentIds getBackToFragmentId() {
        return null;
    }
}
