package com.example.rentalapp.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.rentalapp.R
import com.example.rentalapp.data.AppDatabase
import com.example.rentalapp.data.Home
import com.example.rentalapp.data.SampleData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        val view = inflater.inflate(R.layout.fragment_home_list, null, false) as RecyclerView

        val swipeLayout = SwipeRefreshLayout(requireContext())
        swipeLayout.addView(view)
        swipeLayout.setOnRefreshListener {
            swipeLayout.isRefreshing = true
//                ...
            swipeLayout.isRefreshing = false
        }

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }

                CoroutineScope(Dispatchers.IO).launch{
                    val dao = AppDatabase.getInstace(context).apartmentDao()
                    val apartments = dao.findAllApartments()

                    CoroutineScope(Dispatchers.Main).launch{
                        adapter = HomeRecyclerViewAdapter(apartments)
                    }
                }
//                    adapter = HomeRecyclerViewAdapter(SampleData.APARTMENT)

            }
        }

        return swipeLayout
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