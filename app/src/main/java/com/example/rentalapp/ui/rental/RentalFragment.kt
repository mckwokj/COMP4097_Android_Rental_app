package com.example.rentalapp.ui.rental

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rentalapp.R
import com.example.rentalapp.data.Apartment
import com.example.rentalapp.ui.rental.dummy.DummyContent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * A fragment representing a list of Items.
 */
class RentalFragment : Fragment() {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rental_list, container, false)

        val pref: SharedPreferences = context?.getSharedPreferences(
            "userInfo",
            Context.MODE_PRIVATE
        )!!

        val myRenalsJson = pref.getString("myRentalsJson", "")

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }

                val apartment = Gson().fromJson<List<Apartment>>(myRenalsJson, object :
                    TypeToken<List<Apartment>>() {}.type)


                Log.d("RentalFragment", apartment.toString())

                adapter = RentalRecyclerViewAdapter(apartment)
            }
        }
        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            RentalFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}