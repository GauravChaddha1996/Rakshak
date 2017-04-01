package com.sih.rakshak.features.inbox;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by gaurav on 27/8/16.
 */
public class InboxItemTouchCallback extends ItemTouchHelper.SimpleCallback {
    //Recycler View Adpater to handle the drag and swipe cases
    private RVAdapter adapter;
    private DeleteInterface deleteInterface;

    public InboxItemTouchCallback(RVAdapter adapter, DeleteInterface deleteInterface) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
        this.deleteInterface = deleteInterface;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        deleteInterface.deleteMail(adapter.getItem(viewHolder.getAdapterPosition()));
        adapter.remove(viewHolder.getAdapterPosition());
    }
}
