package com.example.rentalapp

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.rentalapp.data.Apartment
import com.example.rentalapp.data.AppDatabase
import com.example.rentalapp.data.Network
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.homeFragment, R.id.estateFragment, R.id.roomFragment, R.id.userFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        Log.d("onCreate", "onCreate")

//        if (isOnline(this@MainActivity)) {
            CoroutineScope(Dispatchers.IO).launch {
                val dao = AppDatabase.getInstance(this@MainActivity).apartmentDao()

                val URL = "property/json"
                val json = Network.getTextFromNetwork(URL)

                if (json != "") {
                    Log.d("HomeFragment rawJson", json)
                    // convert the string json into List<Apartment>
                    val apartment = Gson().fromJson<List<Apartment>>(json, object :
                        TypeToken<List<Apartment>>() {}.type)

                    dao.findAllApartments().forEach {
                        dao.delete(it)
                    }

                    apartment.forEach {
                        dao.insert(it)
                    }
                } else {
                    Snackbar.make(findViewById(R.id.homeFragment), "Fail to grab the latest data. Please check you internet connection.", Snackbar.LENGTH_LONG).show()
                }
            }
//        } else {
//            Snackbar.make(findViewById(R.id.homeFragment), "Fail to grab the latest data. Please check you internet connection.", Snackbar.LENGTH_LONG).show()
//        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController(R.id.nav_host_fragment).popBackStack()
        return true
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
}