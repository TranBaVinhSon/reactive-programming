package com.example.sontbv.flighttickets.view

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.sontbv.flighttickets.R
import com.example.sontbv.flighttickets.app.Const
import com.example.sontbv.flighttickets.network.ApiClient
import com.example.sontbv.flighttickets.network.ApiService
import com.example.sontbv.flighttickets.network.model.Price
import com.example.sontbv.flighttickets.network.model.Ticket
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.Observable;
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import io.reactivex.ObservableSource
import io.reactivex.internal.operators.single.SingleInternalHelper.toObservable
import io.reactivex.observables.ConnectableObservable
import io.reactivex.observers.DisposableObserver
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private val TAG: String = MainActivity::class.java.simpleName
    private val from: String = "DEL"
    private val to: String = "HYD"

    private var disposable: CompositeDisposable = CompositeDisposable()
    private lateinit var apiService: ApiService
    private lateinit var adapter: TicketsAdapter
    private var tickets: MutableList<Ticket> = ArrayList()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        apiService = ApiClient.createService(ApiService::class.java)

        adapter = TicketsAdapter(this, tickets)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter

        var ticketsObservable: ConnectableObservable<MutableList<Ticket>>  = getTickets(from, to)!!.replay()

        disposable.add(
                ticketsObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableObserver<MutableList<Ticket>>() {
                            override fun onComplete() {

                            }

                            override fun onNext(t: MutableList<Ticket>) {
                                tickets.clear()
                                tickets.addAll(t)
                                adapter.notifyDataSetChanged()
                            }

                            override fun onError(e: Throwable) {
                            }
                        })
        )

        disposable.add(
                ticketsObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMap(object : Function<MutableList<Ticket>, ObservableSource<Ticket>> {
                            override fun apply(t: MutableList<Ticket>): ObservableSource<Ticket> {
                                return Observable.fromIterable(t)
                            }
                        })
                        .flatMap(object : Function<Ticket, ObservableSource<Ticket>> {
                            override fun apply(t: Ticket): ObservableSource<Ticket> {
                                return getPriceObservable(t)
                            }
                        })
                        .subscribeWith(object : DisposableObserver<Ticket>() {
                            override fun onComplete() {
                            }

                            override fun onNext(t: Ticket) {
                                var position = tickets.indexOf(t)
                                if(position == -1) {
                                    // TODO
                                    // Ticket not found in the list
                                    // This shouldn't happen
                                    return;
                                }
                            }

                            override fun onError(e: Throwable) {
                            }
                        })
        )




        ticketsObservable.connect()

    }


    private fun getTickets(from: String, to: String): Observable<MutableList<Ticket>>? {
        return apiService
                .searchTickets(from, to)
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
    // get price of flight
    private fun getPriceObservable(ticket: Ticket) : Observable<Ticket> {
        return apiService
                .getPrice(ticket.flightNumber, ticket.from, ticket.to)
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(object: Function<Price, Ticket> {
                    override fun apply(t: Price): Ticket {
                        ticket.price = t
                        Log.d(TAG, t.price.toString())
                        return ticket
                    }
                })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}
