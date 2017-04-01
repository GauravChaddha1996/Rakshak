package com.sih.rakshak.features.inbox;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sih.rakshak.R;

import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gaurav on 1/4/17 for Rakshak.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RVViewHolder> {
    private final Context context;
    private List<Message> items;

    public RVAdapter(Context context, List<Message> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RVViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        return new RVViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inbox_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RVViewHolder holder, int position) {
        Message item = items.get(position);
        try {
            holder.emailSubject.setText(item.getSubject());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public void setData(List<Message> data) {
        items = data;
        notifyDataSetChanged();
    }

    public Message getItem(int pos) {
        return items.get(pos);
    }

    public void remove(int pos) {
        items.remove(pos);
        notifyDataSetChanged();
    }

    class RVViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.email_subject)
        TextView emailSubject;

        RVViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}