package com.nonda.dtc.blelib;


import com.nonda.dtc.blelib.constant.ConnectError;

public abstract class ConnectCallback {
    public abstract void onConnectSuccess();

    public abstract void onConnectFailed(ConnectError error);
}