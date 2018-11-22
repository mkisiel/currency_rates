package com.example.patrycja.kotlincurrency.viewmodel;


import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.patrycja.kotlincurrency.api.CurrencyService;
import com.example.patrycja.kotlincurrency.api.RestClient;
import com.example.patrycja.kotlincurrency.api.model.Rate;
import com.example.patrycja.kotlincurrency.api.model.Table;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CurrencyViewModel extends ViewModel {

    private MutableLiveData<List<Rate>> rates = new MutableLiveData<>();

    public LiveData<List<Rate>> getRatesLiveData() {
        return rates;
    }

    @SuppressLint("CheckResult")
    public void fetch() {
        //TODO notify UI about loading
        CurrencyService currencyService = RestClient.getInstance().getCurrencyService();
        currencyService.getTableA()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseList -> {
                    if (responseList != null && !responseList.isEmpty()) {
                        rates.setValue(responseList.get(0).getRates());
                    } else {
                        //TODO notify UI about error
                    }
                }, throwable -> {
                    //TODO notify UI about error
                });
    }
}