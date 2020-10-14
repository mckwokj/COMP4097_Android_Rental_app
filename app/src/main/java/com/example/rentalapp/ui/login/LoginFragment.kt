package com.example.rentalapp.ui.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.example.rentalapp.R
import com.example.rentalapp.data.Apartment
import com.example.rentalapp.data.Network
import com.example.rentalapp.data.User
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val username: EditText = view.findViewById(R.id.usernameTextView)
        val password: EditText = view.findViewById(R.id.passwordTextView)
        val loginBtn: Button = view.findViewById(R.id.loginBtn)

        val url = "user/login"

        loginBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val loginJson = Network.login(url, username.text.toString(), password.text.toString())

                Log.d("LoginFragment json", loginJson.toString())

                val user = Gson().fromJson<User>(loginJson?.get(0), object:
                    TypeToken<User>() {}.type)

                // login successfully
                if (loginJson != null) {
                    val cookie = loginJson!!.get(1)
                    Log.d("LoginFragment cookie", cookie)
                    val myRentalsJson = loginJson!!.get(2)

                    CoroutineScope(Dispatchers.Main).launch {
//                        it.findNavController().navigate(R.id.action_loginFragment_to_userFragment,
//                        bundleOf(Pair("username", user.username), Pair("img", user.avatar)))

                        val pref: SharedPreferences = context?.getSharedPreferences("userInfo",
                            Context.MODE_PRIVATE
                        )!!

                        pref.edit().apply{
                            putString("username", user.username)
                            putString("img", user.avatar)
                            putString("cookie", cookie)
                            putString("myRentalsJson", myRentalsJson)
                        }.commit()

                        it.findNavController().navigate(R.id.action_loginFragment_to_userFragment,
                            bundleOf(Pair("username", user.username), Pair("img", user.avatar)))
                        Snackbar.make(view, "Welcome ${user.username}.", Snackbar.LENGTH_LONG).show()
                    }
                }
                // fail to login
                else {
                    Snackbar.make(view, "Fail to login, please try again.", Snackbar.LENGTH_LONG).show()
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
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}