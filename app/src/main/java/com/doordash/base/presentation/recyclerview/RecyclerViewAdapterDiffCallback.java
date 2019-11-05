package com.doordash.base.presentation.recyclerview;


import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

/**
 * Generic DiffUtil callback used to compare memory addresses of objects
 * in RecyclerViewAdapters
 */
public class RecyclerViewAdapterDiffCallback extends DiffUtil.Callback {
    private List<RecyclerViewViewModel> mOldViewModels;
    private List<RecyclerViewViewModel> mNewViewModels;

    RecyclerViewAdapterDiffCallback(List<RecyclerViewViewModel> oldPresenters,
                                    List<RecyclerViewViewModel> newPresenters) {
        mOldViewModels = oldPresenters;
        mNewViewModels = newPresenters;
    }

    @Override
    public int getOldListSize() {
        return mOldViewModels.size();
    }

    @Override
    public int getNewListSize() {
        return mNewViewModels.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        RecyclerViewViewModel oldPresenter = mOldViewModels.get(oldItemPosition);
        RecyclerViewViewModel newPresenter = mNewViewModels.get(newItemPosition);
        return oldPresenter.isItemTheSame(newPresenter);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return null;
    }
}
