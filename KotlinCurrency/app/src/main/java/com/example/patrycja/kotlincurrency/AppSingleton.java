package com.example.patrycja.kotlincurrency;

import com.example.patrycja.kotlincurrency.api.InterfaceCurrency;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppSingleton {

    private static AppSingleton instance = null;
    private InterfaceCurrency interfaceCurrency;
    public static final String BASE_URL = "http://api.nbp.pl";

    public static AppSingleton getInstance() {
        if (instance == null) {
            instance = new AppSingleton();
        }
        return instance;
    }

    private AppSingleton() {
        buildRetrofit();
    }

    private void buildRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        this.interfaceCurrency = retrofit.create(InterfaceCurrency.class);
    }

    public InterfaceCurrency getInterfaceCurrency() {
        return this.interfaceCurrency;
    }

}
