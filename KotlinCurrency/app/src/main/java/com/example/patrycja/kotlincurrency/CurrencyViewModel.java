package com.example.patrycja.kotlincurrency;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.patrycja.kotlincurrency.api.CurrencyService;
import com.example.patrycja.kotlincurrency.api.RestClient;
import com.example.patrycja.kotlincurrency.model.Table;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CurrencyViewModel extends ViewModel {

    private MutableLiveData<List<Table>> table;
    public LiveData<List<Table>> getTable(){
        if(table == null){
            new MutableLiveData<List<Table>>();
            loadTable();
        }
        return table;
    }

    private void loadTable(){
        CurrencyService currencyService = RestClient.getInstance().getCurrencyService();
        currencyService.getTableA()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Table>>() {
                    @Override
                    public void accept(List<Table> tables) throws Exception {
                    }
                });
    }
}