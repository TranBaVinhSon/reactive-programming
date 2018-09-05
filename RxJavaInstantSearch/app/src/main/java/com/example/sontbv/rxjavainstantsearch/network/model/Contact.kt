package com.example.sontbv.rxjavainstantsearch.network.model

import com.google.gson.annotations.SerializedName

data class Contact(
        val name: String,
        @SerializedName("image") val image: String,
        val phone: String,
        val email: String
)