package com.example.rentalapp

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.rentalapp.data.AppDatabase
import com.example.rentalapp.data.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException


class MapsFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */


//        val locationData = arguments?.apply{
//            getDouble("latitude")
//            getDouble("longitude")
//            getString("estate")
//        }

        try {
            val apartmentInfo = arguments?.apply {
                getInt("id")
                getString("estate")
            }

            val isOnline = isOnline(requireContext())
            Log.d("isOnline", isOnline.toString())

            if (apartmentInfo != null) {
                val id = apartmentInfo?.getInt("id")
                val estate = apartmentInfo?.getString("estate")
//
                CoroutineScope(Dispatchers.IO).launch {
                val dao = AppDatabase.getInstance(requireContext()).locationDao()
                var location = dao.findLocationByEstate(estate!!)
                    var coordinate: LatLng? = null

                if (location != null) {
                    coordinate = LatLng(location.latitude, location.longitude)
                    Log.d("MapsFragment not null", coordinate.toString())

                    CoroutineScope(Dispatchers.Main).launch {
                        googleMap.addMarker(MarkerOptions().position(coordinate!!).title(estate))
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate))
                        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15F))
                    }
                }
                else {
                    coordinate = getLocationFromAddress(
                        requireContext(),
                        estate + ", Hong Kong", id
                    )

                    Log.d("MapsFragment null", coordinate.toString())
                    if (coordinate != null) {
                        dao.insert(Location(estate, coordinate.latitude, coordinate.longitude))

                        CoroutineScope(Dispatchers.Main).launch {
                            googleMap.addMarker(MarkerOptions().position(coordinate!!).title(estate))
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate))
                            googleMap.moveCamera(CameraUpdateFactory.zoomTo(15F))
                        }
                    }
                }
//                }

//                if (apartment.latitude == null) {
//
//                   addr = getLocationFromAddress(requireContext(),
//                        estate + ", Hong Kong")!!
//                    dao.updateLatLong(id, addr.latitude, addr.longitude)
//                    Log.d("MapsFragmantSave", apartment.latitude.toString()+""+apartment.longitude.toString())
//                } else {
//                    Log.d("MapsFragmantLoadFromDB", apartment.latitude.toString()+""+apartment.longitude.toString())
//                    addr = LatLng(apartment.latitude!!, apartment.longitude!!)
//                }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
//            view?.findNavController()?.navigate(R.id.action_mapsFragment_to_choiceFragment,
//                bundleOf(Pair("id", id)))
        }
    }

    fun getLocationFromAddress(context: Context?, strAddress: String?, id: Int): LatLng? {
        val coder = Geocoder(context)
        val address: List<Address>?
        var p1: LatLng? = null
        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5)
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}