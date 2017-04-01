package com.sih.rakshak.features.notes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sih.rakshak.R;

import io.realm.RealmResults;

/**
 * Created by ManikantaInugurthi on 01-04-2017.
 */

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.MyViewHolder> {

    final Context c;
    private LayoutInflater inflater;
    RealmResults<NotesItem> notes;

    public NotesListAdapter(Context c,RealmResults<NotesItem> notes){
        this.c=c;
        this.notes = notes;
    }

    public RealmResults<NotesItem> getNotes() {
        return notes;
    }

    public void setNotes(RealmResults<NotesItem> notes) {
        this.notes = notes;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate a new card view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(notes.get(position).getTitle());
        holder.desc.setText(notes.get(position).getDescription());
        holder.lastViewed.setText(notes.get(position).getLastViewed());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc;
        TextView lastViewed;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.notes_title);
            desc = (TextView) itemView.findViewById(R.id.notes_desc);
            lastViewed =(TextView) itemView.findViewById(R.id.notes_LastViewed);
        }
    }
}
