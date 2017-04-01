package com.sih.rakshak.features.inbox;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.sih.rakshak.R;
import com.sih.rakshak.base.BaseFragment;
import com.sih.rakshak.features.FragmentIds;
import com.sih.rakshak.features.HomeActivity;
import com.sih.rakshak.features.inbox.helper.DeleteInterface;
import com.sih.rakshak.features.inbox.helper.EndlessRecyclerOnScrollListener;
import com.sih.rakshak.features.inbox.helper.InboxItemTouchCallback;
import com.sih.rakshak.features.inbox.helper.ItemClickSupport;
import com.sih.rakshak.features.inbox.helper.RVAdapter;
import com.sih.rakshak.features.sendmail.SendMailActivity;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;

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
    @BindView(R.id.content)
    RelativeLayout content;
    @BindView(R.id.loading)
    RelativeLayout loading;
    @BindView(R.id.error)
    RelativeLayout error;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.sendFab)
    FloatingActionButton sendFab;
    private RVAdapter adapter;
    private List<Message> data = new ArrayList<>();
    private InboxItemTouchCallback itemTouchCallback;
    private InboxPresenter presenter;

    private int pageNumber = 1;
    private LinearLayoutManager layoutManager;

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
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new RVAdapter(getActivity(), getData());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        itemTouchCallback = new InboxItemTouchCallback(adapter, this);
        new ItemTouchHelper(itemTouchCallback).attachToRecyclerView(recyclerView);
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView1, position, v) -> {
            ((HomeActivity) getActivity()).setMessage(adapter.getItem(position));
            ((HomeActivity) getActivity()).setFragment(FragmentIds.MAIL);
        });
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                pageNumber++;
                presenter.fetchMoreObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(messages -> {
                            int pos = adapter.getItemCount();
                            adapter.addData(messages);
                            hideView(progressBar);
                            recyclerView.smoothScrollToPosition(pos + 1);
                        }, throwable -> {
                            throwable.printStackTrace();
                            hideView(progressBar);
                        });
            }
        });
    }

    @Override
    public void showProgressBar() {
        new Handler(Looper.getMainLooper()).post(() -> showView(progressBar));
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

    @OnClick(R.id.sendFab)
    void sendEmail() {
        startActivity(new Intent(getActivity(), SendMailActivity.class));
    }

}
