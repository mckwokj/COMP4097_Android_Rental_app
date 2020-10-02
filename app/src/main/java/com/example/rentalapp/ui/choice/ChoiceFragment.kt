package com.example.rentalapp.ui.choice

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.example.rentalapp.R
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChoiceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChoiceFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_choice, container, false)

        val homeId = resources.getStringArray(R.array.homeId)
        val homeImage = resources.getStringArray(R.array.homeImage)
        val homeTitle = resources.getStringArray(R.array.homeTitle)
        val homeEstate = resources.getStringArray(R.array.homeEstate)
        val homePrice = resources.getStringArray(R.array.homePrice)
        val homeBedroom = resources.getStringArray(R.array.homeBedroom)
        val homeTenants = resources.getStringArray(R.array.homeTenants)
        val homeArea = resources.getStringArray(R.array.homeArea)
        val latArr = resources.getStringArray(R.array.latitude)
        val longArr = resources.getStringArray(R.array.longitude)

        val choiceImage: ImageView = view.findViewById(R.id.choiceHomeImage)
        val choiceTitle: TextView = view.findViewById(R.id.choiceTitleText)
        val choiceFirst: TextView = view.findViewById(R.id.firstLineText)
        val choiceSecond: TextView = view.findViewById(R.id.secondLineText)
        val moveInBtn: Button = view.findViewById(R.id.moveInButton)
        val addressBtn: Button = view.findViewById(R.id.addressButton)


        val id = arguments?.getString("id")

        var lat: Double? = null
        var long: Double? = null

        var target: Int? = null

        for (idx in 0..homeId.size-1) {
            if (homeId[idx] == id) {
                Picasso.get().load(homeImage[idx]).into(choiceImage)
                choiceTitle.text = homeTitle[idx]
                choiceFirst.text = "Estate: ${homeEstate[idx]}, Bedrooms: ${homeBedroom[idx]}"
                choiceSecond.text = "Rent: $${homePrice[idx]}, Tenants: ${homeTenants[idx]}, Area: ${homeArea[idx]}"

                lat = latArr[idx].toDouble()
                long = longArr[idx].toDouble()

                target = idx

                break
            }
        }

        addressBtn.setOnClickListener{
            it.findNavController().navigate(R.id.action_choiceFragment_to_mapsFragment,
            bundleOf(Pair("latitude", lat), Pair("longitude", long), Pair("estate", homeEstate[target!!])))
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
         * @return A new instance of fragment ChoiceFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChoiceFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}