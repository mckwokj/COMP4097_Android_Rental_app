package com.example.rentalapp.ui.choice

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import coil.load
import com.example.rentalapp.R
import com.example.rentalapp.data.Apartment
import com.example.rentalapp.data.AppDatabase
import com.example.rentalapp.data.Network
import com.example.rentalapp.data.User
import com.example.rentalapp.ui.load.LoadingDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import java.io.IOException


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
        val loadingDialog: LoadingDialog = LoadingDialog(context as Activity)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_choice, container, false)

        val apartmentImage: ImageView = view.findViewById(R.id.choiceHomeImage)
        val apartmentTitle: TextView = view.findViewById(R.id.choiceTitleText)
        val apartmentFirst: TextView = view.findViewById(R.id.firstLineText)
        val apartmentSecond: TextView = view.findViewById(R.id.secondLineText)
        val moveInBtn: Button = view.findViewById(R.id.moveInButton)
        val addressBtn: Button = view.findViewById(R.id.addressButton)

        val id = arguments?.getString("id")

        CoroutineScope(Dispatchers.IO).launch {
            val dao = AppDatabase.getInstance(requireContext()).apartmentDao()
            Log.d("ChoiceFragment id", id!!)
            val apartmentInfo = dao.findApartmentByID(id.toInt())
//            val img = Picasso.get().load(apartmentInfo.image_URL)

            CoroutineScope(Dispatchers.Main).launch {
//                img.into(apartmentImage)
                apartmentImage.load(apartmentInfo.image_URL) {
                    crossfade(true)
                }
                apartmentTitle.text = apartmentInfo.property_title
                apartmentFirst.text = "Estate: ${apartmentInfo.estate}, " +
                        "Bedrooms: ${apartmentInfo.bedrooms}"
                apartmentSecond.text = "Rent: $${apartmentInfo.rent}, " +
                        "Tenants: ${apartmentInfo.expected_tenants}, Area: ${apartmentInfo.gross_area}"

                addressBtn.setOnClickListener {

                    val isOnline = isOnline(requireContext())

                    if (isOnline!!) {
                        it.findNavController().navigate(
                            R.id.action_choiceFragment_to_mapsFragment,
                            bundleOf(
                                Pair("id", apartmentInfo.id),
                                Pair("estate", apartmentInfo.estate)
                            )
                        )
                    } else {
                        AlertDialog.Builder(context)
                            .setTitle("Network error")
                            .setMessage("Please enable your network connection")
                            .setNeutralButton("Ok", null)
                            .show()
                    }
                }

                val pref: SharedPreferences = context?.getSharedPreferences(
                    "userInfo",
                    Context.MODE_PRIVATE
                )!!

//                moveInBtn.text = pref.getString("moveBtn", "Move-in")

                var myRentalsJson = pref.getString("myRentalsJson", "")

                Log.d("BeforeMoveINOUT myRentals rawJson", myRentalsJson!!)

                val apartment = Gson().fromJson<List<Apartment>>(myRentalsJson, object :
                    TypeToken<List<Apartment>>() {}.type)

                val myRental = apartment?.filter { id.toInt() == it.id }

                Log.d("id.toInt()", id)

                Log.d("BeforeMoveINOUT myRental", myRental.toString())

                if (myRental?.size == 1) {
//                if (moveInBtn.text == "Move-out") {
                    val myRentalID = myRental?.get(0)?.id
                    Log.d("MyRentalID", myRentalID.toString())

                    moveInBtn.text = "Move-out"

                    moveInBtn.setOnClickListener {
//                        val pref: SharedPreferences = context?.getSharedPreferences(
//                            "userInfo",
//                            Context.MODE_PRIVATE
//                        )!!
                        val isOnline = isOnline(requireContext())

                        if (isOnline!!) {
                            val username = pref.getString("username", "Not yet login")!!

                            if (username != "Not yet login") {
                                try {
                                    AlertDialog.Builder(context)
                                        .setTitle("Are you sure?")
                                        .setMessage("to move out this apartment?")
                                        .setPositiveButton("Yes") { dialog, which ->
                                            loadingDialog.startLoadingDialog()
                                            CoroutineScope(Dispatchers.IO).launch {
//
                                                val cookie = pref.getString("cookie", "")

                                                if (cookie != null) {
                                                    Log.d("Choice cookie", cookie)
                                                }

                                                Log.d("Choice cookie null", cookie + "abc")

                                                if (cookie != "") {
                                                    val moveOutJson =
                                                        Network.moveOut(id.toInt(), cookie!!)
                                                    val moveOutResponseCode =
                                                        moveOutJson?.get(1)?.toInt()

                                                    if (moveOutResponseCode == 200) {
                                                        val myRentals = moveOutJson?.get(0)
//                                                    dao.updateOccupiedByID(id.toInt(), false)
                                                        CoroutineScope(Dispatchers.Main).launch {
                                                            loadingDialog.dismissDialog()
                                                            AlertDialog.Builder(context)
                                                                .setTitle("Move-out successfully.")
                                                                .setNeutralButton("Ok", null)
                                                                .show()

                                                            pref.edit().apply {
                                                                putString(
                                                                    "myRentalsJson",
                                                                    myRentals
                                                                )
                                                            }.apply()

                                                            it.findNavController()
                                                                .navigate(R.id.action_choiceFragment_to_homeFragment)
                                                        }
                                                    } else {
                                                        CoroutineScope(Dispatchers.Main).launch {
                                                            loadingDialog.dismissDialog()
                                                            AlertDialog.Builder(context)
                                                                .setTitle("Network error")
                                                                .setMessage("Please enable your network connection")
                                                                .setNeutralButton("Ok", null)
                                                                .show()
                                                        }
                                                    }
                                                }
                                            }
                                        } // A null listener allows the button to dismiss the dialog and take no further action.
                                        .setNegativeButton("No", null)
                                        .show()
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }

                            } else {
                                AlertDialog.Builder(context)
                                    .setTitle("You are not logged in")
                                    .setMessage("Please login first.")
                                    .setNeutralButton("Ok", null)
                                    .show()
                            }
                        } else {
                            AlertDialog.Builder(context)
                                .setTitle("Network error")
                                .setMessage("Please enable your network connection")
                                .setNeutralButton("Ok", null)
                                .show()
                        }
                    }

                } else {

                    moveInBtn.setOnClickListener {

                        val username = pref.getString("username", "Not yet login")!!

                        val isOnline = isOnline(requireContext())

                        if (isOnline!!) {

                            if (username != "Not yet login") {
                                try {
                                    AlertDialog.Builder(context)
                                        .setTitle("Are you sure?")
                                        .setMessage("to move in this apartment?")
                                        .setPositiveButton("Yes") { dialog, which ->

                                            val isOnline2 = isOnline(requireContext())

                                            if (isOnline!!) {
                                                loadingDialog.startLoadingDialog()
                                                CoroutineScope(Dispatchers.IO).launch {
//                                            val pref: SharedPreferences =
//                                                context?.getSharedPreferences(
//                                                    "userInfo",
//                                                    Context.MODE_PRIVATE
//                                                )!!

                                                    val cookie = pref.getString("cookie", "")

                                                    if (cookie != null) {
                                                        Log.d("Choice cookie", cookie)
                                                    }

                                                    Log.d("Choice cookie null", cookie + "abc")

                                                    if (cookie != "") {
                                                        val moveInJson =
                                                            Network.moveIn(id.toInt(), cookie!!)
                                                        if (moveInJson != null) {
                                                        val moveInResponseCode =
                                                            moveInJson?.get(1)?.toInt()

                                                        if (moveInResponseCode == 200) {
                                                            val myRentals = moveInJson?.get(0)
                                                            CoroutineScope(Dispatchers.Main).launch {
                                                                loadingDialog.dismissDialog()
                                                                AlertDialog.Builder(context)
                                                                    .setTitle("Move-in successfully.")
                                                                    .setNeutralButton("Ok", null)
                                                                    .show()

                                                                pref.edit().apply {
                                                                    putString(
                                                                        "myRentalsJson",
                                                                        myRentals
                                                                    )
                                                                }.commit()

                                                                moveInBtn.text = "Move-out"

                                                                it.findNavController()
                                                                    .navigate(R.id.action_choiceFragment_to_homeFragment)
                                                            }
                                                        } else if (moveInResponseCode == 422) {
                                                            loadingDialog.dismissDialog()
                                                            CoroutineScope(Dispatchers.Main).launch {
                                                                AlertDialog.Builder(context)
                                                                    .setTitle("Already full")
                                                                    .setNeutralButton("Ok", null)
                                                                    .show()
                                                            }
                                                        }
                                                    } else {
                                                            CoroutineScope(Dispatchers.Main).launch {
                                                                loadingDialog.dismissDialog()
                                                                AlertDialog.Builder(context)
                                                                    .setTitle("Error occured")
                                                                    .setNeutralButton("Ok", null)
                                                                    .show()
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                AlertDialog.Builder(context)
                                                    .setTitle("Network error")
                                                    .setMessage("Please enable your network connection")
                                                    .setNeutralButton("Ok", null)
                                                    .show()
                                            }
                                        } // A null listener allows the button to dismiss the dialog and take no further action.
                                        .setNegativeButton("No", null)
                                        .show()
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }

                            } else {
                                AlertDialog.Builder(context)
                                    .setTitle("You are not logged in")
                                    .setMessage("Please login first.")
                                    .setNeutralButton("Ok", null)
                                    .show()
                            }
                        } else {
                            AlertDialog.Builder(context)
                                .setTitle("Network error")
                                .setMessage("Please enable your network connection")
                                .setNeutralButton("Ok", null)
                                .show()
                        }
                    }
                }
            }
        }

        return view
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        Log.i("Internet", "Network failure")
        return false
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