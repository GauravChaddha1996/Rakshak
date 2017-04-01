package com.sih.rakshak.features.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sih.rakshak.R;
import com.sih.rakshak.base.BaseFragment;
import com.sih.rakshak.features.FragmentIds;
import com.sih.rakshak.features.HomeActivity;

import java.util.Random;

import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Batdroid on 1/4/17 for Rakshak.
 */

public class NotesFragment extends BaseFragment {

    private FloatingActionButton plusFab;
    private RecyclerViewExtender recyclerView;
    private Realm realm;
    NotesListAdapter notesListAdapter;
    RealmResults<NotesItem> notes;
    TextView emptyText;

    public NotesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes,container,false);
        ButterKnife.bind(this,view);
        plusFab = (FloatingActionButton) view.findViewById(R.id.addNotesFab);
        recyclerView = (RecyclerViewExtender) view.findViewById(R.id.recycler);
        emptyText = (TextView) view.findViewById(R.id.emptyText);
        recyclerView.setEmptyView(emptyText);
        realm = Realm.getDefaultInstance();
        notes = realm.where(NotesItem.class).findAll();
        plusFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callEditNotes(0L);
            }
        });
        setUpRecycler();
        return view;
    }

    private void setUpRecycler() {
        if (notes.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        notesListAdapter = new NotesListAdapter(getContext(), notes);
        recyclerView.setAdapter(notesListAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                callEditNotes(notes.get(position).getId());
            }
        });
    }

    private void callEditNotes(Long s) {
        NotesItem note;
        if(s==0){
            Random rand = new Random();
            Long temp=rand.nextLong();
            note = realm.createObject(NotesItem.class,temp);
        }
        else {
             note = realm.where(NotesItem.class).equalTo("id",s).findFirst();
        }
        ((HomeActivity) getActivity()).setNotesItem(note);
        ((HomeActivity) getActivity()).setFragment(FragmentIds.NOTESDETAIL);
    }

    @Override
    public FragmentIds getFragmentId() {
        return FragmentIds.NOTES;
    }

    @Override
    public FragmentIds getBackToFragmentId() {
        return FragmentIds.INBOX;
    }



}
