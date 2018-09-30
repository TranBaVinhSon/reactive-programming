package com.example.sontbv.flighttickets.network

import com.example.sontbv.flighttickets.app.Const
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private var retrofit: Retrofit? = null

    private val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                        .build()
                chain.proceed(request)
            }
    private val okHttpClient = okHttpClientBuilder.build()

    fun <T> createService(serviceClass: Class<T>): T {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(Const.BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return retrofit!!.create(serviceClass)
    }
}