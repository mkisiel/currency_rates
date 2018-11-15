package com.example.patrycja.kotlincurrency;

//komunikacja miedzy JobService i AsyncTask
public interface DownloadDataTaskCallback {
    void taskFinished(Rate rate);
    void taskFailed();
}
