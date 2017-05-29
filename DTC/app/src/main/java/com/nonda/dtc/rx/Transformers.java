package com.nonda.dtc.rx;

import android.support.annotation.NonNull;

import com.nonda.dtc.rx.transformers.ObserveForUITransformer;

/**
 * Created by whaley on 2017/5/29.
 */

public final class Transformers {
    private Transformers() {
    }

    @NonNull
    public static <T> ObserveForUITransformer<T> observerForUI() {
        return new ObserveForUITransformer<>();
    }
}
