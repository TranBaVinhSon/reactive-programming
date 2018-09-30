package com.example.sontbv.flighttickets.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sontbv.flighttickets.R
import com.example.sontbv.flighttickets.network.model.Ticket

class TicketsAdapter() : RecyclerView.Adapter<TicketsAdapter.ItemViewHolder>() {
    private lateinit var tickets: MutableList<Ticket>
    private lateinit var context: Context
    private val TAG: String = TicketsAdapter::class.java.simpleName

    constructor(context: Context, tickets: MutableList<Ticket>) : this() {
        this.context = context
        this.tickets = tickets
    }
    class ItemViewHolder(viewHolder: View) : RecyclerView.ViewHolder(viewHolder) {
        var airlineName = viewHolder.findViewById(R.id.airline_name) as TextView
        var logo = viewHolder.findViewById(R.id.logo) as ImageView
        var stops = viewHolder.findViewById(R.id.number_of_stops) as TextView
        var seats = viewHolder.findViewById(R.id.number_of_seats) as TextView
        var departure = viewHolder.findViewById(R.id.departure) as TextView
        var arrival = viewHolder.findViewById(R.id.arrival) as TextView
        var duration = viewHolder.findViewById(R.id.duration) as TextView
        var price = viewHolder.findViewById(R.id.price) as TextView
        var loader = viewHolder.findViewById(R.id.loader) as ProgressBar
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_ticket, p0, false)
        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return tickets.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, p1: Int) {
        val ticket = tickets[p1]
        Glide.with(context)
                .load(ticket.airline.logo)
                .apply(RequestOptions().circleCrop())
                .into(holder.logo)

        holder.airlineName.text = ticket.airline.name
        holder.departure.text = ticket.departure
        holder.duration.text = ticket.flightNumber
        holder.arrival.text = ticket.arrival
        holder.duration.append(", " + ticket.duration)
        holder.stops.text = ticket.numberOfStops.toString() + " Stops"
        if(!TextUtils.isEmpty(ticket.instructions)) {
            holder.duration.append(", " + ticket.instructions)
        }
        if(ticket.price != null){
            Log.d(TAG, ticket.price.price.toString())
            holder.price.text = "₹" + ticket.price.price.toString()
            holder.seats.text = ticket.price.seats + " Seats"
            holder.loader.visibility = View.INVISIBLE
        }else {
            holder.loader.visibility = View.VISIBLE
        }

    }
}