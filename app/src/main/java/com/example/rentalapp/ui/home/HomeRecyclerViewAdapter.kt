package com.example.rentalapp.ui.home

import android.content.Intent
import android.util.Log
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.example.rentalapp.R
import com.example.rentalapp.data.Apartment
import com.example.rentalapp.data.Choice
import com.example.rentalapp.data.Home
import com.example.rentalapp.ui.choice.ChoiceFragment
//import com.example.rentalapp.ui.home.dummy.DummyContent.DummyItem
import com.squareup.picasso.Picasso

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class HomeRecyclerViewAdapter(
    private val values: List<Apartment>
) : RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_home_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]

        Picasso.get().load(item.img).into(holder.homeImage)

        val id: Int = item.id

        holder.id = id.toString()

        holder.homeTitle.text = item.title
        holder.homeEstate.text = item.estate
        holder.homePrice.text = "Rent: $"+item.rent
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val homeImage: ImageView = view.findViewById(R.id.homeImage)
        val homeTitle: TextView = view.findViewById(R.id.titleText)
        val homeEstate: TextView = view.findViewById(R.id.estateText)
        val homePrice: TextView = view.findViewById(R.id.priceText)

        var id: String = ""

        init {
            view.setOnClickListener{
                it.findNavController().navigate(R.id.action_homeFragment_to_choiceFragment,
                bundleOf(Pair("id", id)))
            }
        }

//        override fun toString(): String {
//            return super.toString() + " '" + contentView.text + "'"
//        }
    }
}