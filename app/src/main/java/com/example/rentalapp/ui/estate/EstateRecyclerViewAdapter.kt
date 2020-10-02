package com.example.rentalapp.ui.estate

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.rentalapp.R
import com.example.rentalapp.data.Apartment
import com.example.rentalapp.data.Home

import com.example.rentalapp.ui.estate.dummy.DummyContent.DummyItem

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class EstateRecyclerViewAdapter(
    private val values: List<String>
) : RecyclerView.Adapter<EstateRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_estate_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.estateText.text = item
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val estateText: TextView = view.findViewById(R.id.estateText)

//        override fun toString(): String {
//            return super.toString() + " '" + contentView.text + "'"
//        }
    }
}