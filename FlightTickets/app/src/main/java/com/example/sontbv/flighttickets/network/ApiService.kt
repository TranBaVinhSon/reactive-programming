package com.example.sontbv.flighttickets.network

import com.example.sontbv.flighttickets.network.model.Price
import com.example.sontbv.flighttickets.network.model.Ticket
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("airline-tickets.php")
    fun searchTickets(@Query("from") from: String, @Query("to") to: String) : Single<MutableList<Ticket>>

    @GET("airline-tickets-price.php")
    fun getPrice(@Query("flight_number") flightNumber: String, @Query("from") from: String, @Query("to") to : String) : Single<Price>
}
