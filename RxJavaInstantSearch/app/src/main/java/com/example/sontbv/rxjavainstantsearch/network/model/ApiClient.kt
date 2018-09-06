package com.example.sontbv.rxjavainstantsearch.network.model

import com.example.sontbv.rxjavainstantsearch.app.Const
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {
        private var retrofit: Retrofit? = null
        private var okHttpClient: OkHttpClient? = null

        fun getClient(): Retrofit {
            if(okHttpClient == null)
                initOkHttp()
            if(retrofit == null){
                retrofit = Retrofit.Builder()
                        .baseUrl(Const.BASE_URL)
                        .client(okHttpClient)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit!!
        }

        fun initOkHttp() {
            val httpClient: OkHttpClient.Builder = OkHttpClient().newBuilder()

            var interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            httpClient.addInterceptor(interceptor)
            httpClient.addInterceptor { chain ->
                val request = chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Request-Type", "Android")
                        .addHeader("Content-Type", "application/json")
                        .build()
                chain.proceed(request)
            }
            okHttpClient = httpClient.build()
        }

        public fun resetApiClient() {
            retrofit = null
            okHttpClient = null
        }
    }
}