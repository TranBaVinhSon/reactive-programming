package com.example.sontbv.rxjavainstantsearch.view

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.sontbv.rxjavainstantsearch.R
import com.example.sontbv.rxjavainstantsearch.adapter.ContactsAdapter
import com.example.sontbv.rxjavainstantsearch.adapter.ContactsAdapterFilterable
import com.example.sontbv.rxjavainstantsearch.network.model.ApiClient
import com.example.sontbv.rxjavainstantsearch.network.model.ApiService
import com.example.sontbv.rxjavainstantsearch.network.model.Contact
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

import kotlinx.android.synthetic.main.activity_remote_search.*
import kotlinx.android.synthetic.main.content_local_search.*
import java.util.concurrent.TimeUnit

class RemoteSearchActivity : AppCompatActivity() {
    private var disposable: CompositeDisposable = CompositeDisposable()
    private var publishSubject: PublishSubject<String> = PublishSubject.create()
    private lateinit var apiService: ApiService
    private lateinit var adapter: ContactsAdapter
    private var contacts: MutableList<Contact> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remote_search)
        setSupportActionBar(toolbar)

        adapter = ContactsAdapter(this, contacts)
        var layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        recycler_view.layoutManager = layoutManager
        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recycler_view.adapter = adapter

        apiService = ApiClient.getClient().create(ApiService::class.java)

        var observer: DisposableObserver<List<Contact>> = getSearchObserver()

        disposable.add(
                publishSubject
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .distinctUntilChanged()
                        .switchMapSingle(object: Function<String, Single<List<Contact>>>{
                            override fun apply(t: String): Single<List<Contact>>? {
                                return apiService.getContacts(null, t)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                            }
                        })
                        .subscribeWith(observer)
        )

        disposable.add(
                RxTextView.textChangeEvents(input_search)
                        .skipInitialValue()
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(searchContactsTextWatcher())
        )

        disposable.add(observer)

        publishSubject.onNext("")

    }

    fun getSearchObserver(): DisposableObserver<List<Contact>> {
        return object: DisposableObserver<List<Contact>>() {
            override fun onComplete() {
            }

            override fun onNext(t: List<Contact>) {
                contacts.clear()
                contacts.addAll(t)
                adapter.notifyDataSetChanged()
            }

            override fun onError(e: Throwable) {
            }
        }
    }

    fun searchContactsTextWatcher(): DisposableObserver<TextViewTextChangeEvent> {
        return object: DisposableObserver<TextViewTextChangeEvent>() {
            override fun onComplete() {
            }

            override fun onNext(t: TextViewTextChangeEvent) {
                publishSubject.onNext(t.text().toString())
            }

            override fun onError(e: Throwable) {
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}