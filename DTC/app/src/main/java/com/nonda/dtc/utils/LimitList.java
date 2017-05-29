package com.nonda.dtc.utils;

import java.util.ArrayList;

/**
 * Created by whaley on 2017/5/29.
 */

public class LimitList<T> extends ArrayList<T> {
    private final int limit;

    public LimitList(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean add(T t) {

        if (size() == limit) {
            remove(0);
        }
        return super.add(t);
    }
}
