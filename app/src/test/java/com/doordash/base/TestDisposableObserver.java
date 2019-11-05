package com.doordash.base;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

/**
 * Taken from
 * https://github.com/ReactiveX/RxJava/blob/2.x/src/test/java/io/reactivex/observers/DisposableObserverTest.java
 *
 * Used to unit test UseCases. This gets passed into the execute(DATA, Observer) as the Observer.
 *
 * @param <T> Type of data this observer is observing
 */
public class TestDisposableObserver<T> extends DisposableObserver<T> {
    private int start;
    private final List<T> values = new ArrayList<T>();
    private final List<Throwable> errors = new ArrayList<>();
    private int completions;

    @Override
    protected void onStart() {
        super.onStart();
        start++;
    }

    @Override
    public void onNext(T value) {
        values.add(value);
    }

    @Override
    public void onError(Throwable e) {
        errors.add(e);
    }

    @Override
    public void onComplete() {
        completions++;
    }

    public int getStarts() {
        return start;
    }

    public List<T> getValues() {
        return values;
    }

    public List<Throwable> getErrors() {
        return errors;
    }

    public int getCompletions() {
        return completions;
    }
}