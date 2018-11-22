package com.example.patrycja.kotlincurrency;

import android.os.AsyncTask;
import android.util.Log;

import com.example.patrycja.kotlincurrency.api.CurrencyService;
import com.example.patrycja.kotlincurrency.api.RestClient;
import com.example.patrycja.kotlincurrency.api.model.Rate;
import com.example.patrycja.kotlincurrency.api.model.Table;

import java.util.List;

import io.reactivex.Observable;

public class DownloadDataTask extends AsyncTask<Void, Void, Rate> {

    private static final String TAG = DownloadDataTask.class.getSimpleName();
    private static final String USD_CODE = "USD";

    private CurrencyService apiCurrency;
    private DownloadDataTaskCallback taskCallback;

    //konstruktor i instancja retrofita
    public DownloadDataTask(DownloadDataTaskCallback taskCallback) {
        Log.d(TAG, "DownloadDataTask: ");
        apiCurrency = RestClient.getInstance().getCurrencyService();
        this.taskCallback = taskCallback;
    }

    @Override
    protected Rate doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground: ");
        //wywołanie retrofit synchronic request
        //jeśli ok - zwrócenie Rate
        //jeśli nie - return null
        Observable<List<Table>> call = apiCurrency.getTableA();
        List<Table> responseList = call.blockingFirst();
        if (responseList == null) {
            return null;
        }
        for (Table response : responseList) {
            for (Rate rate : response.getRates()) {
                if (rate.getCode().equals(USD_CODE)) {
                    return rate;
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Rate rate) {
        super.onPostExecute(rate);
        Log.d(TAG, "onPostExecute: ");
        if (rate != null) {
            taskCallback.taskFinished(rate);
        } else {
            taskCallback.taskFailed();
        }
    }
}
