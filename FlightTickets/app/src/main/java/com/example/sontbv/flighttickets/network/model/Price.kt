package com.example.sontbv.flighttickets.network.model

import com.google.gson.annotations.SerializedName

data class Price(
        var price: Float,
        var seats: String,
        var currency: String,
        @SerializedName("flight_number")
        var flightNumber: String,
        var from: String,
        var to: String
)