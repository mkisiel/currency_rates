package com.example.patrycja.kotlincurrency;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DownloadDataTask extends AsyncTask<Void, Boolean, Rate> {

    private static final String TAG = DownloadDataTask.class.toString();
    private static final String USD_CODE = "USD";

    private  InterfaceCurrency apiCurrency;
    private DownloadDataTaskCallback taskCallback;

    //konstruktor i instancja retrofita
    public DownloadDataTask(DownloadDataTaskCallback taskCallback) {
        Log.d(TAG, "DownloadDataTask: ");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.nbp.pl")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.apiCurrency = retrofit.create(InterfaceCurrency.class);

        this.taskCallback = taskCallback;
    }

    @Override
    protected Rate doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground: ");
        //wywołanie retrofit synchronic request
        //jeśli ok - zwrócenie Rate
        //jeśli nie - return null
        Call<List<Table>> call = apiCurrency.getTableASynchronous();
        try {
            List<Table> responseList = call.execute().body();
            if(responseList == null){
                return null;
            }
            for(Table response:responseList){
                for(Rate rate: response.getRates()){
                    if(rate.getCode().equals(USD_CODE)){
                        return rate;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Rate rate) {
        super.onPostExecute(rate);
        Log.d(TAG, "onPostExecute: ");
        if(rate != null){
            taskCallback.taskFinished(rate);
        }else{
            //próbuje wykonać się jeszcze raz
            taskCallback.taskFailed();
        }
    }
}
