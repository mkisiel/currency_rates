package com.example.patrycja.kotlincurrency

import io.reactivex.Observable
import retrofit2.http.GET

interface InterfaceCurrency {

    @GET("/api/exchangerates/tables/a/?format=json")
    fun getCurrency() : Observable<List<Response>>
}