package com.doordash.base.presentation.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.ButterKnife;

/**
 * Base ViewHolder used to display UI in RecyclerViews. This gets constructed by the
 * RecyclerViewAdapter on an as needed basis. Each ViewHolder is powered by an individual
 * RecyclerViewViewModel.
 *
 * @param <VM> The type of RecyclerViewViewModel used to power this ViewHolder
 */

public class ViewHolder<VM extends RecyclerViewViewModel> extends RecyclerView.ViewHolder {

    protected VM mViewModel;

    public ViewHolder(ViewGroup parent, @LayoutRes int layoutRes) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false));
        ButterKnife.bind(this, itemView);
        setClickListener();
    }

    public Context getContext() {
        return itemView.getContext();
    }

    @CallSuper
    public void bindViewModel(VM viewModel) {
        mViewModel = viewModel;
        mViewModel.onBindToNewViewHolder(getLayoutPosition());
    }

    @CallSuper
    public void onRecycled() {
        if (mViewModel != null) {
            mViewModel.onRecycled();
        }
        mViewModel = null;
    }

    private void setClickListener() {
        itemView.setOnClickListener(v -> {
            if (mViewModel != null) {
                mViewModel.onViewHolderClicked();
            }
        });
    }
}
