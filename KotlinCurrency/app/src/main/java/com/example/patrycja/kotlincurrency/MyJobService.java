package com.example.patrycja.kotlincurrency;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class MyJobService extends JobService {

    private static final String TAG = MyJobService.class.toString();
    private static final double MAX_RATE = 3.9;
    private static final double MIN_RATE = 3.7;
    private JobParameters params;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: ");
        new DownloadDataTask(callback).execute();
        this.params = params;
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "onStopJob: ");
        this.params = null;
        return false;
    }

    DownloadDataTaskCallback callback = new DownloadDataTaskCallback() {
        @Override
        public void taskFinished(Rate rate) {
            Log.d(TAG, "taskFinished: ");
            //sprawdzenie poziom mid
            //jeśli warunek spełniony - tworzenie notyfikacji
            //jeśli nie - finishJob

            if(rate.getMid()>MAX_RATE || rate.getMid()<MIN_RATE){
                Log.d(TAG, "taskFinished: nice currency ");
                NotificationHelper notification = new NotificationHelper(getApplicationContext());
                notification.createNotification("Warning", "Recorded limit value for USD");
            }

            jobFinished(params, false);
        }

        @Override
        public void taskFailed() {
            Log.d(TAG, "taskFailed: ");
            jobFinished(params, true);
        }
    };
}

