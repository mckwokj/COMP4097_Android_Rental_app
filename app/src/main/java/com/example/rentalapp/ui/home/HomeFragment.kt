package com.example.rentalapp.ui.home

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.rentalapp.R
import com.example.rentalapp.data.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException


//import com.example.rentalapp.ui.home.dummy.DummyContent

/**
 * A fragment representing a list of Items.
 */
class HomeFragment : Fragment() {

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
        val recyclerView = inflater.inflate(R.layout.fragment_home_list, null, false) as RecyclerView

        val swipeLayout = SwipeRefreshLayout(requireContext())
        swipeLayout.addView(recyclerView)
        swipeLayout.setOnRefreshListener {
            swipeLayout.isRefreshing = true
            reloadData(recyclerView)
            swipeLayout.isRefreshing = false
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
//        reloadData(recyclerView)
        CoroutineScope(Dispatchers.IO).launch{
            val dao = AppDatabase.getInstance(requireContext()).apartmentDao()
            val apartments = dao.findAllApartments()

            // when the app is newly installed
            Log.d("HomeFragment apartment size", apartments.size.toString())

            if (apartments.size == 0){
                reloadData(recyclerView)
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    recyclerView.adapter = HomeRecyclerViewAdapter(apartments)
                }
            }
        }

        return swipeLayout
    }

    private fun reloadData(recyclerView: RecyclerView){
        val URL = "property/json"
        CoroutineScope(Dispatchers.IO).launch{
            try {
                val json = Network.getTextFromNetwork(URL)

                Log.d("HomeFragment rawJson", json)
                // convert the string json into List<Apartment>
                val apartment = Gson().fromJson<List<Apartment>>(json, object:
                    TypeToken<List<Apartment>>() {}.type)

                Log.d("HomeFragment", apartment.toString())

                val dao = AppDatabase.getInstance(requireContext()).apartmentDao()
                val location_dao = AppDatabase.getInstance(requireContext()).locationDao()

                // delete all existing apartments
                dao.findAllApartments().forEach{
                    dao.delete(it)
                }

                apartment.forEach {
                    dao.insert(it)
//                    val coordinate = getLocationFromAddress(context, "${it.estate}, Hong Kong")
//                    Log.d("HomeFragmentCoor", coordinate.toString())
//                    if (coordinate != null) {
//                        dao.updateLatLong(it.id, coordinate.latitude, coordinate.longitude)
//                        location_dao.insert(Location(it.estate, coordinate.latitude, coordinate.longitude))
//                    }
                }
                    CoroutineScope(Dispatchers.Main).launch {
                        recyclerView.adapter = HomeRecyclerViewAdapter(apartment)
                    }

            } catch (e: Exception){
                Log.d("HomeFragment", "Error in loading data")
                Log.d("HomeFragment", e.toString())

                Snackbar.make(requireView(), "Fail to grab the latest data. Please check you internet connection.", Snackbar.LENGTH_LONG).show()

//                val apartment = listOf(Apartment(-1, "Cannot fetch apartments",
//                    "Please check your network connection", 0, 0,
//                    0, 0, false, null, null,
//                    ""))
//
//                CoroutineScope(Dispatchers.Main).launch {
//                    recyclerView.adapter = HomeRecyclerViewAdapter(apartment)
//                }
            }
        }
    }

    fun getLocationFromAddress(context: Context?, strAddress: String?): LatLng? {
        val coder = Geocoder(context)
        val address: List<Address>?
        var p1: LatLng? = null
        try {
            // May throw an IOException
            Log.d("strAddress", strAddress.toString())
            address = coder.getFromLocationName(strAddress, 5)
            Log.d("address", address.toString())
            if (address == null) {
                return null
            }
            val location = address[0]
            p1 = LatLng(location.latitude, location.longitude)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return p1
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}