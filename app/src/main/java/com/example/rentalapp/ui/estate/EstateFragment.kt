package com.example.rentalapp.ui.estate

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rentalapp.R
import com.example.rentalapp.data.AppDatabase
import com.example.rentalapp.data.Home
import com.example.rentalapp.ui.estate.dummy.DummyContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of Items.
 */
class EstateFragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_estate_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }

//                val estateId = resources.getStringArray((R.array.homeId))
//                val estateText = resources.getStringArray(R.array.homeEstate)
//                val estate = mutableListOf<Home>()
//
//                for (i in 0..(estateText.size-1)){
//                    estate.add(Home(estateId[i], null, null, estateText[i], null))
//                }

                CoroutineScope(Dispatchers.IO).launch{
                    val dao = AppDatabase.getInstace(requireContext()).apartmentDao()
                    val estates = dao.findAllEstateName()

                    CoroutineScope(Dispatchers.Main).launch{
                        adapter = EstateRecyclerViewAdapter(estates)
                    }
                }
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
            EstateFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}