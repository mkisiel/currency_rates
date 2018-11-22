package com.example.patrycja.kotlincurrency.service;


import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.example.patrycja.kotlincurrency.NotificationHelper;
import com.example.patrycja.kotlincurrency.api.RestClient;
import com.example.patrycja.kotlincurrency.api.model.Rate;
import com.example.patrycja.kotlincurrency.api.model.Table;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MyJobService extends JobService {

    private static final String TAG = MyJobService.class.getSimpleName();
    private static final double MAX_RATE = 3.9;
    private static final double MIN_RATE = 3.7;
    private static final String USD_CODE = "USD";

    @SuppressLint("CheckResult")
    @Override
    public boolean onStartJob(final JobParameters params) {
        Log.d(TAG, "onStartJob: ");
        RestClient.getInstance().getCurrencyService().getTableA()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseList -> {
                    Log.d(TAG, "taskFinished: ");
                    //sprawdzenie poziom mid
                    //jeśli warunek spełniony - tworzenie notyfikacji
                    //jeśli nie - finishJob
                    if (responseList != null) {
                        for (Table response : responseList) {
                            for (Rate rate : response.getRates()) {
                                if (rate.getCode().equals(USD_CODE)) {
                                    if (rate.getMid() > MAX_RATE) {
                                        Log.d(TAG, "taskFinished: nice currency ");
                                        NotificationHelper notification = new NotificationHelper(getApplicationContext());
                                        notification.createNotification("Warning", "The USD/PLN rate has exceeded the maximum limit");
                                    } else if (rate.getMid() < MIN_RATE) {
                                        Log.d(TAG, "taskFinished: nice currency ");
                                        NotificationHelper notification = new NotificationHelper(getApplicationContext());
                                        notification.createNotification("Warning", "The USD/PLN rate has exceeded the minimum limit");
                                    }

                                    jobFinished(params, false);
                                    return;
                                }
                            }
                        }
                    }
                    jobFinished(params, true);

                }, throwable -> {
                    Log.d(TAG, "taskFailed: ");
                    jobFinished(params, true);
                });
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "onStopJob: ");
        return false;
    }

}

