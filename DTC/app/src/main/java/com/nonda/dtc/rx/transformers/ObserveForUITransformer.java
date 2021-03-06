package com.nonda.dtc.rx.transformers;

import com.nonda.dtc.utils.ThreadUtils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by whaley on 2017/5/29.
 */

public final class ObserveForUITransformer<T> implements Observable.Transformer<T, T> {
    @Override
    public Observable<T> call(Observable<T> tObservable) {
        return tObservable.flatMap(new Func1<T, Observable<T>>() {
            @Override
            public Observable<T> call(T t) {
                if (ThreadUtils.isMainThread()) {
                    return Observable.just(t).observeOn(Schedulers.immediate());
                } else {
                    return Observable.just(t).observeOn(AndroidSchedulers.mainThread());
                }
            }
        });
    }
}