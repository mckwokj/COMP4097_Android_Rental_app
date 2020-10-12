package com.example.rentalapp.ui.room

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.example.rentalapp.R
import com.example.rentalapp.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RoomFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RoomFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_room, container, false)

        val lessThanTwo: TextView = view.findViewById(R.id.conditionText1)
        val greaterThanThree: TextView = view.findViewById(R.id.conditionText2)

        lessThanTwo.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch{
                val dao = AppDatabase.getInstance(requireContext()).apartmentDao()
                val apartments = dao.findApartmentsLTBedroomNum(3)

                CoroutineScope(Dispatchers.Main).launch {
                    it.findNavController().navigate(
                        R.id.action_roomFragment_to_criteriaFragment,
                        bundleOf(Pair("apartments", apartments))
                    )
                }
            }
        }

        greaterThanThree.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch{
                val dao = AppDatabase.getInstance(requireContext()).apartmentDao()
                val apartments = dao.findApartmentsGTBedroomNum(2)

                CoroutineScope(Dispatchers.Main).launch {
                    it.findNavController().navigate(
                        R.id.action_roomFragment_to_criteriaFragment,
                        bundleOf(Pair("apartments", apartments))
                    )
                }
            }
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment roomFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RoomFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}