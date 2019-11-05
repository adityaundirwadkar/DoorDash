package com.doordash.base.data;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

/**
 * Base UseCase class that allows for ViewModels to send and retrieve data to managers and repositories.
 * Execution is done through rx.Observable and threading is dictated by the provided Schedulers.
 * Generally these Schedulers are provided through injection from SchedulersModule with
 * background work occurring on Schedulers.io() and the Subscriber being invoked on
 * AndroidSchedulers.mainThread()
 *
 * @param <Result> Result data
 * @param <Input>  Provided data
 */
public abstract class UseCase<Result, Input> {

    private final Scheduler mSubscribeOn;
    private final Scheduler mObserveOn;
    private final CompositeDisposable mDisposables;

    public UseCase(@NonNull Scheduler subscribeOn, @NonNull Scheduler observeOn) {
        mSubscribeOn = subscribeOn;
        mObserveOn = observeOn;
        mDisposables = new CompositeDisposable();
    }

    public abstract Observable<Result> buildUseCaseObservable(Input data);

    /**
     * Execute with a observer to handle results
     *
     * @param data       The data used to create the Observable
     * @param subscriber The subscriber for the Observable
     */
    public final void execute(@Nullable Input data, @NonNull final DisposableObserver<Result> subscriber) {
        final Observable<Result> observable = buildUseCaseObservable(data)
                .subscribeOn(mSubscribeOn)
                .observeOn(mObserveOn)
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(this.getClass().getSimpleName(), throwable.toString());
                    }
                });
        mDisposables.add(observable.subscribeWith(subscriber));
    }

    /**
     * Clean up operations
     */
    public void dispose() {
        if (!mDisposables.isDisposed()) {
            mDisposables.dispose();
        }
    }

    /**
     * Stops current operations
     */
    public void clear() {
        mDisposables.clear();
    }

    /**
     * Execute the UseCase when we don't care about handling data output or errors
     *
     * @param data The data used to create the Observable
     * @return The Subscription return for subscribing to the Observable
     */
    public void execute(final Input data) {
        Disposable disposable = buildUseCaseObservable(data)
                .subscribeOn(mSubscribeOn)
                .observeOn(mObserveOn)
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.e(getClass().getSimpleName(), throwable.toString());
                    }
                })
                .subscribe(result -> { }, throwable -> Log.e(this.getClass().getSimpleName(), throwable.toString()));
        mDisposables.add(disposable);
    }
}