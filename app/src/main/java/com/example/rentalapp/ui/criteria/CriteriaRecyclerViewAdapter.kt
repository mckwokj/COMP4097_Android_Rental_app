package com.example.rentalapp.ui.criteria

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.example.rentalapp.R
import com.example.rentalapp.data.Apartment

import com.example.rentalapp.ui.criteria.dummy.DummyContent.DummyItem

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class CriteriaRecyclerViewAdapter(
    private val values: List<Apartment>
) : RecyclerView.Adapter<CriteriaRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_criteria_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.titleTextView.text = item.property_title
        holder.estateTextView.text = item.estate
        holder.id = item.id

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.criterialTitle)
        val estateTextView: TextView = view.findViewById(R.id.criterialEstate)
        var id = 0

        init {
            view.setOnClickListener{
                it.findNavController().navigate(R.id.action_criteriaFragment_to_choiceFragment,
                bundleOf(Pair("id", id.toString())))
            }
        }

//        override fun toString(): String {
//            return super.toString() + " '" + contentView.text + "'"
//        }
    }
}