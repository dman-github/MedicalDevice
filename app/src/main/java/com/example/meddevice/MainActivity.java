package com.example.meddevice;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import java.io.FileOutputStream;
import java.io.InputStream;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    DeviceUserManager deviceUserManager;
    private Disposable mDisposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Resources res = getResources();
            // Read the text files from R.raw resources
            InputStream inputStreamDevice = res.openRawResource(R.raw.deviceuserlist);
            InputStream inputStreamPortal = res.openRawResource(R.raw.portaluserlist);
            String outPutFileName = "DeviceUserListUpdated.txt";
            FileOutputStream outputStream = openFileOutput(outPutFileName, Context.MODE_PRIVATE);
            deviceUserManager = new DeviceUserManager(inputStreamDevice,
                    inputStreamPortal,
                    outputStream);
        } catch (Exception e) {
            System.out.printf("Unable to parse, reason: %s",e.getLocalizedMessage());
        }
        deviceUserManager.task
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        System.out.printf("********Processing DONE for %s Users *********", s);
                        System.out.printf("Thread %d  %s%n", Thread.currentThread().getId(),Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}