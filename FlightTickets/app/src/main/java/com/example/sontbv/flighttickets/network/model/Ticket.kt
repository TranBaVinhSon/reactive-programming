package com.example.sontbv.flighttickets.network.model

import com.google.gson.annotations.SerializedName

data class Ticket(
    var from: String,
    var to: String,
    @SerializedName("flight_number")
    var flightNumber: String,
    var departure: String,
    var arrival: String,
    var duration: String,
    var instructions: String,
    @SerializedName("stops")
    var numberOfStops: Int,
    var airline: Airline,
    var price: Price

) {
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}