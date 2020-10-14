package com.example.rentalapp.ui.user

import android.app.AlertDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
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
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.rentalapp.MainActivity
import com.example.rentalapp.R
import com.example.rentalapp.data.Apartment
import com.example.rentalapp.data.Network
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_choice.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        val loginOffBtn: Button = view.findViewById(R.id.loginOffBtn)
        val myRentalsBtn: Button = view.findViewById(R.id.myRentalsBtn)
        val usernameView: TextView = view.findViewById(R.id.usernameTextView)
        val userImgView: ImageView = view.findViewById(R.id.userImageView)

        var username = arguments?.getString("username")
        var userImg = arguments?.getString("img")

        // try to use sharedPreferences to store the username and img

        // Login successfully
//        if (username != null && userImg != null) {
//
//            val pref: SharedPreferences = context?.getSharedPreferences("userInfo", MODE_PRIVATE)!!
//
//            pref.edit().apply{
//                putString("username", username)
//                putString("img", userImg)
//            }.commit()
//        } else {
            val pref: SharedPreferences = context?.getSharedPreferences("userInfo", MODE_PRIVATE)!!
            username = pref.getString("username", "Not yet login")!!
            userImg = pref.getString("img", null)
//        }

        usernameView.text = username
        if (userImg != null) {
            Picasso.get().load(userImg).resize(200, 200)
                .centerCrop().into(userImgView)
        }

        myRentalsBtn.setOnClickListener{
            if (username != "Not yet login") {

//                        val apartment = Gson().fromJson<List<Apartment>>(myRenalsJson, object :
//                            TypeToken<List<Apartment>>() {}.type)
//
//                        Log.d("myRentals", apartment.toString())

                        CoroutineScope(Dispatchers.Main).launch {
                            findNavController().navigate(R.id.action_userFragment_to_rentalFragment)
                        }
            } else {
                AlertDialog.Builder(context)
                    .setTitle("You are not logged in")
                    .setMessage("Please login first.")
                    .setNeutralButton("Ok", null)
                    .show()
            }
        }

        loginOffBtn.setOnClickListener{

            if (username != "Not yet login") {

                CoroutineScope(Dispatchers.IO).launch {

                    val code = Network.logout("user/logout")
                    Log.d("code", code.toString())

                    if (code != null) {

                        CoroutineScope(Dispatchers.Main).launch {

                            val pref: SharedPreferences = context?.getSharedPreferences(
                                "userInfo",
                                Context.MODE_PRIVATE
                            )!!

                            pref.edit().apply {
                                putString("username", "Not yet login")
                                putString("img", null)
                                putString("cookie", "")
                                putString("myRentalsJson", "")
                            }.commit()

                            Snackbar.make(view, "Logout successfully", Snackbar.LENGTH_LONG).show()
                            it.findNavController()
                                .navigate(R.id.action_userFragment_to_loginFragment)
                        }
                    } else {
                        Snackbar.make(view, "Fail to logout, please check your internet connection.", Snackbar.LENGTH_LONG).show()
                    }
                }
            } else {
                it.findNavController().navigate(R.id.action_userFragment_to_loginFragment)
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
         * @return A new instance of fragment UserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}