package com.example.patrycja.kotlincurrency

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.patrycja.kotlincurrency.R.id.list_currencies
import kotlinx.android.synthetic.main.activity_main.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var currencyAdapter : CurrencyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currencyAdapter = CurrencyAdapter()
        list_currencies.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        list_currencies.adapter = currencyAdapter

        val retrofit : Retrofit = Retrofit.Builder()
                .baseUrl("http://api.nbp.pl")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        val apiCurrency = retrofit.create(InterfaceCurrency::class.java)

        apiCurrency.getCurrency()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ currencyAdapter.setCurrency(it.rates) },
                        {
                            Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                        })
    }

    inner class CurrencyAdapter : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

        private val currencyList: MutableList<Rate> = mutableListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
            return CurrencyViewHolder(layoutInflater.inflate(R.layout.item, parent, false))
        }

        override fun getItemCount(): Int {
            return currencyList.size
        }

        override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
            holder.bindModel(currencyList[position])
        }

        fun setCurrency(rates: List<Rate>) {
            currencyList.addAll(rates)
            notifyDataSetChanged()
        }

        inner class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val currencyTxt : TextView = itemView.findViewById(R.id.currencytxt)
            val codeTxt : TextView = itemView.findViewById(R.id.codetxt)
            val midTxt : TextView = itemView.findViewById(R.id.midtxt)

            fun bindModel(currency : Rate) {
                currencyTxt.text = currency.currency
                codeTxt.text = currency.code
                midTxt.text = currency.mid.toString()
            }
        }
    }
}
