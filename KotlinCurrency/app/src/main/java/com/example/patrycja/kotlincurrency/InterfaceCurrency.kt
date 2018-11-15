package com.example.patrycja.kotlincurrency

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET

interface InterfaceCurrency {

    @GET("/api/exchangerates/tables/a/?format=json")
    fun getTableA() : Observable<List<Table>>

    @GET("/api/exchangerates/tables/a/?format=json")
    fun getTableASynchronous() : Call<List<Table>>
}