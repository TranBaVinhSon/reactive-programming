package com.example.sontbv.rxjavainstantsearch.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sontbv.rxjavainstantsearch.R
import com.example.sontbv.rxjavainstantsearch.network.model.Contact

class ContactsAdapter(): RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {
    private lateinit var context: Context
    private lateinit var contacts: MutableList<Contact>
    val TAG = "ContactsAdapter"

    constructor(context: Context, contacts: MutableList<Contact>): this() {
        this.contacts = contacts
        this.context = context
    }

    class ViewHolder(viewHolder: View): RecyclerView.ViewHolder(viewHolder) {
        var name: TextView = viewHolder.findViewById(R.id.name)
        var phone: TextView = viewHolder.findViewById(R.id.phone)
        var thumbnail: ImageView = viewHolder.findViewById(R.id.thumbnail)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(p0.context)
                .inflate(R.layout.item_contact, p0, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact: Contact = contacts.get(position)
        holder.name.text = contact.name
        holder.phone.text = contact.phone

        Glide.with(context)
                .load(contact.image)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail)
    }

}