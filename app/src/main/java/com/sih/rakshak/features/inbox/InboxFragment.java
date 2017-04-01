package com.sih.rakshak.features.inbox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.sih.rakshak.R;
import com.sih.rakshak.base.BaseFragment;
import com.sih.rakshak.features.FragmentIds;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Store;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Batdroid on 1/4/17 for Rakshak.
 */

public class InboxFragment extends BaseFragment implements InboxVI, DeleteInterface {

    final String TAG = "InboxFragment";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.prevButton)
    Button prevButton;
    @BindView(R.id.nextButton)
    Button nextButton;
    @BindView(R.id.content)
    RelativeLayout content;
    @BindView(R.id.loading)
    RelativeLayout loading;
    @BindView(R.id.error)
    RelativeLayout error;
    private RVAdapter adapter;
    private List<Message> data = new ArrayList<>();
    private InboxItemTouchCallback itemTouchCallback;
    private InboxPresenter presenter;
    private Store store;
    private Folder inbox;
    private Folder bin;

    private int pageNumber = 1;

    public InboxFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);
        ButterKnife.bind(this, view);
        presenter = new InboxPresenter(this);
        setRecyclerView();
        fetchData();
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

    @Override
    public void deleteMail(Message message) {
        Log.d(TAG, "deleteMail");
        presenter.deleteObservable(message)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(s -> Snackbar.make(content, s, Snackbar.LENGTH_LONG).show(),
                        throwable -> Snackbar.make(content, throwable.getMessage(),
                                Snackbar.LENGTH_LONG).show());
    }

    @OnClick(R.id.nextButton)
    void nextPage() {
        Log.d(TAG, "nextpage");

    }

    @OnClick(R.id.prevButton)
    void prevPage() {
        Log.d(TAG, "prevPage");

    }

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    private void fetchData() {
        Log.d(TAG, "fetchData");
        loading(content, loading, error);
        presenter.fetchObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::setData
                        , throwable -> {
                            throwable.printStackTrace();
                            error(content, loading, error);
                        });
    }


    private void setRecyclerView() {
        Log.d(TAG, "setRecyclerView");
        adapter = new RVAdapter(getActivity(), getData());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        itemTouchCallback = new InboxItemTouchCallback(adapter, this);
        new ItemTouchHelper(itemTouchCallback).attachToRecyclerView(recyclerView);
    }

    public List<Message> getData() {
        Log.d(TAG, "getData");
        return data;
    }


    private void setData(List<Message> mailModels) {
        Log.d(TAG, "setData");
        content(content, error, loading);
        data = mailModels;
        adapter.setData(data);
    }

}
