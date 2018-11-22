package com.example.patrycja.kotlincurrency

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.arch.lifecycle.Observer
import android.content.ComponentName
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import android.arch.lifecycle.ViewModelProviders
import com.example.patrycja.kotlincurrency.viewmodel.CurrencyViewModel
import com.example.patrycja.kotlincurrency.service.MyJobService
import com.example.patrycja.kotlincurrency.api.model.Rate
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var currencyAdapter : CurrencyAdapter
    private val MY_JOB_ID = 100

    private val SECOND_IN_MILLISECONDS = 1000
    private val MINUTE_IN_MILLIS = 60 * SECOND_IN_MILLISECONDS
    private val HOUR_IN_MILLIS = 60 * MINUTE_IN_MILLIS
    private val DAY_IN_MILLS = 24 * HOUR_IN_MILLIS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currencyAdapter = CurrencyAdapter()
        list_currencies.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        list_currencies.adapter = currencyAdapter

        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(JobInfo.Builder(MY_JOB_ID,
                ComponentName(this, MyJobService::class.java))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(DAY_IN_MILLS.toLong())
                .setBackoffCriteria((10 * MINUTE_IN_MILLIS).toLong(), JobInfo.BACKOFF_POLICY_LINEAR)
                .build())

        val currencyViewModel = ViewModelProviders.of(this).get(CurrencyViewModel::class.java)
        currencyViewModel.ratesLiveData.observe(this, Observer<List<Rate>>{ rates ->
            currencyAdapter.setCurrency(rates)
        })
        currencyViewModel.fetch()
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

        fun setCurrency(rates: List<Rate>?) {
            currencyList.clear()
            if (rates != null) {
                currencyList.addAll(rates)
            }
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
