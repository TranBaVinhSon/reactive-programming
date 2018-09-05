package com.example.sontbv.rxjavainstantsearch.view

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.example.sontbv.rxjavainstantsearch.R
import com.example.sontbv.rxjavainstantsearch.adapter.ContactsAdapterFilterable
import com.example.sontbv.rxjavainstantsearch.network.model.ApiService
import com.example.sontbv.rxjavainstantsearch.network.model.Contact
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_local_search.*

class LocalSearchActivity : AppCompatActivity() {

    private var disposable: CompositeDisposable = CompositeDisposable()
    private lateinit var apiService: ApiService
    private lateinit var adapter: ContactsAdapterFilterable
    private var contacts: MutableList<Contact> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_search)
        setSupportActionBar(toolbar)
        whiteNotificationBar(toolbar )

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    fun searchContacts(): DisposableObserver<TextViewTextChangeEvent> {
        return object: DisposableObserver<TextViewTextChangeEvent>() {
            override fun onComplete() {
            }

            override fun onNext(t: TextViewTextChangeEvent) {
                adapter.filter.filter(t.text())
            }

            override fun onError(e: Throwable) {
            }
        }
    }

    fun fetchContacts(source: String){
        disposable.add(apiService
                .getContacts(source, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<MutableList<Contact>>(){
                    override fun onSuccess(t: MutableList<Contact>) {
                        contacts.clear()
                        contacts.addAll(t)
                        adapter.notifyDataSetChanged()
                    }

                    override fun onError(e: Throwable) {
                    }
                }))

    }

    fun whiteNotificationBar(view: View) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            view.systemUiVisibility = flags
            window.statusBarColor = Color.WHITE
        }
    }

}
