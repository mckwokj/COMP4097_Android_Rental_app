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

        CoroutineScope(Dispatchers.IO).launch {

            val dao = AppDatabase.getInstance(requireContext()).apartmentDao()
            val apartment = dao.findAllApartments()

            if (apartment.size != 0) {
                CoroutineScope(Dispatchers.Main).launch {
                    recyclerView.adapter = HomeRecyclerViewAdapter(apartment)
                    Log.d("HomeFragment", "sent already")
                }
            } else {
                val apartment = listOf(
                    Apartment(
                        -1, "Cannot fetch apartments",
                        "Please check your network connection", 0, 0,
                        0, 0, false, null, null,
                        ""
                    )
                )
                CoroutineScope(Dispatchers.Main).launch {
                    recyclerView.adapter = HomeRecyclerViewAdapter(apartment)
                }
            }
        }
//        reloadData(recyclerView)
//        CoroutineScope(Dispatchers.IO).launch{
//            val dao = AppDatabase.getInstance(requireContext()).apartmentDao()
//            val apartments = dao.findAllApartments()
//
//            // when the app is newly installed
//            Log.d("HomeFragment apartment size", apartments.size.toString())
//
//            if (apartments.size == 0){
//                reloadData(recyclerView)
//            } else {
//                CoroutineScope(Dispatchers.Main).launch {
//                    recyclerView.adapter = HomeRecyclerViewAdapter(apartments)
//                }
//            }
//        }

        return swipeLayout
    }

    private fun reloadData(recyclerView: RecyclerView){
        val URL = "property/json"
        CoroutineScope(Dispatchers.IO).launch{
            try {
                val json = Network.getTextFromNetwork(URL)

                if (json != "") {

                    Log.d("HomeFragment rawJson", json)
                    // convert the string json into List<Apartment>
                    val apartment = Gson().fromJson<List<Apartment>>(json, object :
                        TypeToken<List<Apartment>>() {}.type)

                    Log.d("HomeFragment", apartment.toString())

                    val dao = AppDatabase.getInstance(requireContext()).apartmentDao()
                    Log.d("HomeFragment", "ckpt1")
                    val location_dao = AppDatabase.getInstance(requireContext()).locationDao()
                    Log.d("HomeFragment", "ckpt2")

                    val previousApartments = dao.findAllApartments()
                    Log.d("HomeFragment", "ckpt3")

//                    Log.d("HomeFragment", "chpt3a"+(previousApartments.size!=0).toString())

                    // delete all existing apartments
                    dao.findAllApartments().forEach{
                        dao.delete(it)
                    }

                    Log.d("HomeFragment", "ckpt5")

                    Log.d("HomeFragment", dao.findAllApartments().toString())

                    apartment.forEach {
                        Log.d("HomeFragment", it.toString())
                        dao.insert(it)
                    }

                    CoroutineScope(Dispatchers.Main).launch {
                        recyclerView.adapter = HomeRecyclerViewAdapter(apartment)
                        Log.d("HomeFragment", "ckpt7")
                    }
                } else {
                    val dao = AppDatabase.getInstance(requireContext()).apartmentDao()
                    if (dao.findAllApartments().size == 0) {

                        val apartment = listOf(
                            Apartment(
                                -1, "Cannot fetch apartments",
                                "Please check your network connection", 0, 0,
                                0, 0, false, null, null,
                                ""
                            )
                        )
                        CoroutineScope(Dispatchers.Main).launch {
                            recyclerView.adapter = HomeRecyclerViewAdapter(apartment)
                        }
                    } else {
                        CoroutineScope(Dispatchers.Main).launch {
                            Snackbar.make(
                                requireView(),
                                "Fail to grab the latest data. Please check you internet connection.",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }

            } catch (e: Exception){
                Log.d("HomeFragment", "Error in loading data")
                Log.d("HomeFragment", e.toString())

                CoroutineScope(Dispatchers.Main).launch {
                    Snackbar.make(
                        requireView(),
                        "Fail to grab the latest data. Please check you internet connection.",
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                val dao = AppDatabase.getInstance(requireContext()).apartmentDao()

                if (dao.findAllApartments().size == 0) {
                    val apartment = listOf(
                        Apartment(
                            -1, "Cannot fetch apartments",
                            "Please check your network connection", 0, 0,
                            0, 0, false, null, null,
                            ""
                        )
                    )
                    CoroutineScope(Dispatchers.Main).launch {
                        recyclerView.adapter = HomeRecyclerViewAdapter(apartment)
                    }
                }
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