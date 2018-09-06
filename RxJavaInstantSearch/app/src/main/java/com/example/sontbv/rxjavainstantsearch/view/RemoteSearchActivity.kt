package com.example.sontbv.rxjavainstantsearch.view

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.example.sontbv.rxjavainstantsearch.R

import kotlinx.android.synthetic.main.activity_remote_search.*

class RemoteSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remote_search)
        setSupportActionBar(toolbar)
    }

}
