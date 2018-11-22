package com.example.patrycja.kotlincurrency;

import com.example.patrycja.kotlincurrency.api.model.Rate;

//komunikacja miedzy JobService i AsyncTask
public interface DownloadDataTaskCallback {
    void taskFinished(Rate rate);
    void taskFailed();
}
