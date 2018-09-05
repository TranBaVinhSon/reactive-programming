package com.example.sontbv.rxjavainstantsearch.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sontbv.rxjavainstantsearch.R
import com.example.sontbv.rxjavainstantsearch.network.model.Contact

class ContactsAdapterFilterable(): RecyclerView.Adapter<ContactsAdapterFilterable.ViewHolder>(), Filterable {
    private lateinit var context: Context
    private lateinit var contacts: MutableList<Contact>
    private lateinit var contactsFiltered: MutableList<Contact>

    constructor(context: Context, contacts: MutableList<Contact>, contactsFiltered: MutableList<Contact>): this() {
        this.contacts = contacts
        this.context = context
        this.contactsFiltered = contactsFiltered
    }

    class ViewHolder(viewHolder: View): RecyclerView.ViewHolder(viewHolder) {
        var name: TextView = viewHolder.findViewById(R.id.name)
        var phone: TextView = viewHolder.findViewById(R.id.phone)
        var thumbnail: ImageView = viewHolder.findViewById(R.id.thumbnail)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_contact, p0, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact: Contact = contactsFiltered.get(position)
        holder.name.text = contact.name
        holder.phone.text = contact.phone

        Glide.with(context)
                .load(contact.image)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail)
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                contactsFiltered = p1!!.values as MutableList<Contact>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString = charSequence.toString()
                if(charString.isEmpty()){
                    contactsFiltered = contacts
                }else {
                    var filteredList: MutableList<Contact> = ArrayList()
                    for(contact in contacts) {
                        if(contact.name.toLowerCase().contains(charString.toLowerCase()) || contact.phone.contains(charString)){
                            filteredList.add(contact)
                        }
                    }
                    contactsFiltered = filteredList
                }

                var filterResult: FilterResults = FilterResults()
                filterResult.values = contactsFiltered
                return filterResult
            }

        }
    }

    interface ContactsAdapterListener {
        fun onContactSelected(contact: Contact)
    }
}