package com.sih.rakshak.features.notes;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by ManikantaInugurthi on 01-04-2017.
 */

public class RecyclerViewExtender extends RecyclerView {

    private Context mContext;
    private View view;

    public RecyclerViewExtender(Context context) {
        super(context);
        mContext = context;
    }

    public RecyclerViewExtender(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    public RecyclerViewExtender(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public void setAdapter(final Adapter adapter) {
        super.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new AdapterDataObserver() {
            @Override
            public void onChanged() {
                Log.d("item count:", adapter.getItemCount() + "");
                if (adapter.getItemCount() != 0) {
                    if (view != null) {
                        view.setVisibility(GONE);
                        setVisibility(VISIBLE);
                    }
                } else {
                    if (view != null) {
                        setVisibility(GONE);
                        view.setVisibility(VISIBLE);
                    }
                }
            }
        });
    }

    public void setEmptyView(View emptyView) {
        view = emptyView;
    }

}
