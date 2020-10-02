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
import com.example.rentalapp.data.AppDatabase
import com.example.rentalapp.data.SampleData
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        val apartmentImage: ImageView = view.findViewById(R.id.choiceHomeImage)
        val apartmentTitle: TextView = view.findViewById(R.id.choiceTitleText)
        val apartmentFirst: TextView = view.findViewById(R.id.firstLineText)
        val apartmentSecond: TextView = view.findViewById(R.id.secondLineText)
        val moveInBtn: Button = view.findViewById(R.id.moveInButton)
        val addressBtn: Button = view.findViewById(R.id.addressButton)


        val id = arguments?.getString("id")


        CoroutineScope(Dispatchers.IO).launch{
//            val apartmentInfo = SampleData.APARTMENT.filter{
//                it.id.toString() == id
//            }
            val dao = AppDatabase.getInstace(requireContext()).apartmentDao()
            val apartmentInfo = dao.findApartmentByID(id?.toInt()!!)

            CoroutineScope(Dispatchers.Main).launch{
                Picasso.get().load(apartmentInfo.img).into(apartmentImage)
                apartmentTitle.text = apartmentInfo.title
                apartmentFirst.text = "Estate: ${apartmentInfo.estate}, " +
                        "Bedrooms: ${apartmentInfo.bedrooms}"
                apartmentSecond.text =  "Rent: $${apartmentInfo.rent}, " +
                        "Tenants: ${apartmentInfo.tenants}, Area: ${apartmentInfo.area}"

                addressBtn.setOnClickListener{
                    it.findNavController().navigate(R.id.action_choiceFragment_to_mapsFragment,
                        bundleOf(
                            Pair("latitude", apartmentInfo.latitude),
                            Pair("longitude", apartmentInfo.longitude),
                            Pair("estate", apartmentInfo.estate)))
                }
            }
        }

//        Picasso.get().load(apartmentInfo[0].img).into(apartmentImage)
//        apartmentTitle.text = apartmentInfo[0].title
//        apartmentFirst.text = "Estate: ${apartmentInfo[0].estate}, " +
//                "Bedrooms: ${apartmentInfo[0].bedrooms}"
//        apartmentSecond.text =  "Rent: $${apartmentInfo[0].rent}, " +
//                "Tenants: ${apartmentInfo[0].tenants}, Area: ${apartmentInfo[0].area}"
//
//        addressBtn.setOnClickListener{
//            it.findNavController().navigate(R.id.action_choiceFragment_to_mapsFragment,
//            bundleOf(
//                Pair("latitude", apartmentInfo[0].latitude),
//                Pair("longitude", apartmentInfo[0].longitude),
//                Pair("estate", apartmentInfo[0].estate)))
//        }

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