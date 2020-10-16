package com.example.rentalapp.ui.estate

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.example.rentalapp.R
import com.example.rentalapp.data.Apartment

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

        init {
            estateText.setOnClickListener{
                it.findNavController().navigate(R.id.action_estateFragment_to_criteriaFragment,
                bundleOf(Pair("estate", estateText.text)))

//                it.findNavController().navigate(R.id.action_estateFragment_to_criteriaFragment)
            }
        }

//        override fun toString(): String {
//            return super.toString() + " '" + contentView.text + "'"
//        }
    }
}