package com.nonda.dtc.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.nonda.dtc.R;
import com.nonda.dtc.model.ObdData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by whaley on 2017/6/1.
 */

public class SettingActivity extends BaseActivity {
    private static final String TAG = SettingActivity.class.getSimpleName();
    public MaterialDialog mLoadingDialog;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, SettingActivity.class);
        activity.startActivity(intent);
    }

    @OnClick(R.id.log)
    public void onBtnLogClicked() {
        mLoadingDialog = new MaterialDialog.Builder(this)
                .progress(true, 100)
                .theme(Theme.DARK)
                .progressIndeterminateStyle(false)
                .canceledOnTouchOutside(false)
                .content(R.string.dumping)
                .show();

        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(doDumpLog());
            }
        })
                .compose(this.<Boolean>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                            mLoadingDialog.dismiss();
                        }

                        Toast.makeText(SettingActivity.this, R.string.dump_finished, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private Boolean doDumpLog() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String filename = format.format(System.currentTimeMillis()) + ".txt";
        File storage = Environment.getExternalStorageDirectory();
        File dir = new File(storage + "/dtc/log/");

        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fullPath = dir.toString() + "/" + filename;
        Log.d(TAG, fullPath);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fullPath);
            List<ObdData> obdDataList =  ObdData.getObdDataList();
            for (ObdData obdData : obdDataList) {
                fileOutputStream.write(obdData.toString().getBytes());
            }

            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;


    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        setContentView(R.layout.activity_setting);
    }
}
