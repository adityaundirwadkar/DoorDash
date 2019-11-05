package com.doordash.base.presentation.recyclerview;

import androidx.annotation.CallSuper;
import androidx.lifecycle.ViewModel;

import java.lang.ref.WeakReference;

/**
 *
 */


public class RecyclerViewViewModel<Interactor> extends ViewModel {

    private Class<? extends ViewHolder> mViewHolderClass;

    protected WeakReference<Interactor> mInteractor;

    protected RecyclerViewAdapter mAdapter;

    private int mLayoutPosition;

    private int mViewHolderCount;


    public RecyclerViewViewModel() {
        this(ViewHolder.class);
    }

    public RecyclerViewViewModel(Class<? extends ViewHolder> viewType) {
        mViewHolderClass = viewType;
    }

    public Class getViewHolderClass() {
        return mViewHolderClass;
    }


    @CallSuper
    public void onDetachedFromAdapter() {
        mAdapter = null;
    }

    public void onBindToNewViewHolder(int layoutPosition) {
        mViewHolderCount++;
        mLayoutPosition = layoutPosition;
        if (mViewHolderCount == 1 && mAdapter.isVisible()) {
            onBecameVisible();
        }
    }

    public void onRecycled() {
        mViewHolderCount--;
        if (mViewHolderCount == 0) {
            onHidden();
        }
    }

    public final void onViewPaused() {
        if (isVisible()) {
            onHidden();
        }
    }

    public final void onViewActive() {
        if (isVisible()) {
            onBecameVisible();
        }
    }

    protected void onBecameVisible() {
        // can be overridden as necessary
    }

    protected void onHidden() {
        // can be overridden as necessary
    }

    public void onViewHolderClicked() {
        // can be overridden as necessary
    }

    public int getLayoutPosition() {
        return mLayoutPosition;
    }

    public boolean isItemTheSame(RecyclerViewViewModel newViewModel) {
        return newViewModel == this;
    }

    public void setAdapter(RecyclerViewAdapter adapter) {
        mAdapter = adapter;
    }

    public boolean isVisible() {
        return mViewHolderCount > 0;
    }

    public void setInteractor(Interactor interactor) {
        mInteractor = new WeakReference<>(interactor);
    }

    protected Interactor getInteractor() {
        return mInteractor.get();
    }
}
