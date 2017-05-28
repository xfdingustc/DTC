package com.nonda.dtc.blelib;

public abstract class TimeoutCallback implements Runnable {
    public abstract void onTimeout();

    @Override
    public void run() {
        onTimeout();
    }
}