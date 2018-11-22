package com.example.patrycja.kotlincurrency.api

import com.example.patrycja.kotlincurrency.api.model.Table
import io.reactivex.Observable
import retrofit2.http.GET

interface CurrencyService {

    @GET("/api/exchangerates/tables/a/?format=json")
    fun getTableA() : Observable<List<Table>>
}