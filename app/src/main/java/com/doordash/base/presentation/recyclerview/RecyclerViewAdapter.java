package com.doordash.base.presentation.recyclerview;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.doordash.utils.ListUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;

/**
 * Custom RecyclerViewAdapter to enable data binding.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> implements LifecycleObserver {

    private static final String TAG = RecyclerViewAdapter.class.getSimpleName();

    protected final List<RecyclerViewViewModel> mViewModels = new CopyOnWriteArrayList<>();

    protected final Map<Class, Integer> mViewHolderToItemViewNumber = new HashMap<>();
    protected final Map<Integer, Class> mItemViewNumberToViewHolder = new HashMap<>();
    protected final Map<Class, Constructor> mViewHolderConstructors = new HashMap<>();

    private boolean mUseDiffUtil = true;
    private int mMiddle;
    private boolean mIsVisible = true;


    @Inject
    public RecyclerViewAdapter(LifecycleOwner lifecycleOwner) {
        super();
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Class viewHolderClass = mItemViewNumberToViewHolder.get(viewType);
        Constructor constructor = mViewHolderConstructors.get(viewHolderClass);
        if (constructor == null) {
            try {
                constructor = viewHolderClass.getConstructor(ViewGroup.class);
                mViewHolderConstructors.put(viewHolderClass, constructor);
            } catch (NoSuchMethodException exception) {
                Log.e(TAG, exception.getMessage());
            }
        }
        if (constructor == null) {
            try {
                return ViewHolder.class.getConstructor(ViewGroup.class).newInstance(parent);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        ViewHolder viewHolder = null;
        try {
            viewHolder = (ViewHolder) constructor.newInstance(parent);
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException exception) {
            Log.e(TAG, exception.toString());
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindViewModel(getViewModel(position));
    }

    @Override
    public int getItemCount() {
        if (ListUtils.isEmpty(mViewModels)) {
            return 0;
        }
        return mViewModels.size();
    }

    @Override
    public void onViewRecycled(ViewHolder viewHolder) {
        super.onViewRecycled(viewHolder);
        viewHolder.onRecycled();
    }

    @Override
    public int getItemViewType(int position) {
        int index;
        Class<? extends ViewHolder> provider = getViewModel(position).getViewHolderClass();
        if (mViewHolderToItemViewNumber.get(provider) == null) {
            index = mViewHolderToItemViewNumber.size();
            mViewHolderToItemViewNumber.put(provider, index);
            mItemViewNumberToViewHolder.put(index, provider);
        } else {
            index = mViewHolderToItemViewNumber.get(provider);
        }
        return index;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onBecameVisible() {
        if (isVisible() && !ListUtils.isEmpty(mViewModels)) {
            for (int i = 0, size = mViewModels.size(); i < size; i++) {
                mViewModels.get(i).onViewActive();
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onHidden() {
        if (!ListUtils.isEmpty(mViewModels)) {
            for (int i = 0, size = mViewModels.size(); i < size; i++) {
                mViewModels.get(i).onViewPaused();
            }
        }
    }

    public <T extends RecyclerViewViewModel> void addViewModel(@NonNull T viewModel) {
        viewModel.setAdapter(this);
        mViewModels.add(viewModel);
    }

    public <T extends RecyclerViewViewModel> void addViewModels(@NonNull List<T> viewModels) {
        for (T viewModel : viewModels) {
            viewModel.setAdapter(this);
        }
        mViewModels.addAll(viewModels);
    }

    public <T extends RecyclerViewViewModel> void setViewModels(List<T> cells) {
        if (mUseDiffUtil) {
            List<RecyclerViewViewModel> compareList = new ArrayList<>(cells);
            // Diff calculation may take enough time for user input to sneak in between view model swap and
            // update dispatch, potentially causing an IndexOutOfBounds error when the adapter attempts to access
            // a view model at an index available in the old dataset but not the new. Mitigate this by replacing
            // the view models AFTER the DiffUtil calculation, and right before the update dispatch.
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new RecyclerViewAdapterDiffCallback(mViewModels, compareList));
            replaceViewModels(cells);
            result.dispatchUpdatesTo(this);
        } else {
            replaceViewModels(cells);
            notifyDataSetChanged();
        }
    }

    public List<RecyclerViewViewModel> getViewModels() {
        return mViewModels;
    }

    /**
     * Common view model-replacement code.
     */
    private <T extends RecyclerViewViewModel> void replaceViewModels(List<T> cells) {
        // detach viewmodels that are in the current list of viewmodels, but not in the new list.
        if (!ListUtils.isEmpty(mViewModels)) {
            boolean hasNewViewModels = !ListUtils.isEmpty(cells);
            for (RecyclerViewViewModel viewModel : mViewModels) {
                if (!hasNewViewModels || !cells.contains(viewModel)) {
                    detachViewModel(viewModel);
                }
            }
        }
        mViewModels.clear();
        if (!ListUtils.isEmpty(cells)) {
            for (int i = 0, size = cells.size(); i < size; i++) {
                addViewModel(cells.get(i));
            }
        }
        updateMiddle();
    }

    private void updateMiddle() {
        if (mViewModels.size() == 0) {
            mMiddle = 0;
        } else {
            mMiddle = mViewModels.size() / 2;
        }
    }

    public int getMiddle() {
        return mMiddle;
    }

    public RecyclerViewViewModel getViewModel(int position) {
        if (position < 0) {
            Log.d("ASDF", "RV position less than 0: " + position);
            position = 0;
        }
        return mViewModels.get(position);
    }

    public int getPosition(RecyclerViewViewModel viewModel) {
        for (int i = 0; i < mViewModels.size(); i++) {
            if (mViewModels.get(i).isItemTheSame(viewModel)) {
                return i + 1;
            }
        }
        return -1;
    }

    private void detachViewModel(RecyclerViewViewModel viewModel) {
        viewModel.onViewPaused();
        viewModel.onDetachedFromAdapter();
    }

    public void setIsVisible(boolean visible) {
        boolean didVisibilityChange = mIsVisible != visible;
        if (didVisibilityChange) {
            mIsVisible = visible;
            if (visible) {
                onBecameVisible();
            } else {
                onHidden();
            }
        }
    }

    public boolean isVisible() {
        return mIsVisible;
    }
}
