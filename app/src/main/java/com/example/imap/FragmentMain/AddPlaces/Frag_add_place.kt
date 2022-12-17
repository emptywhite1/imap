package com.example.imap.FragmentMain.AddPlaces

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.imap.R
import com.example.imap.data.MyPlaces
import com.example.imap.databinding.FragmentFragAddPlaceBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class frag_add_place : Fragment() {
    private lateinit var _binding: FragmentFragAddPlaceBinding
    private lateinit var dataBaseRef: DatabaseReference
    private lateinit var myData:DatabaseReference
    private val REQUEST_CODE = 100
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var longitude: Double = 0.0
    private var latitude: Double = 0.0
    private lateinit var address: String

    private lateinit var firebaseAuth: FirebaseAuth
    private var uId: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFragAddPlaceBinding.inflate(inflater, container, false)
        return _binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        onClickAdd()
        getLocation()
        onClickFinish()
    }
    @SuppressLint("MissingPermission")
    private fun getLocation(){
        fusedLocationProviderClient?.lastLocation
            ?.addOnSuccessListener(OnSuccessListener<Location> { location ->
                if (location != null) {
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    var addresses: List<Address>? = null

                        addresses =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        longitude = addresses[0].longitude
                        latitude = addresses[0].latitude
                        address = addresses[0].getAddressLine(0)
                        Log.d("===========", "getLocation: " + longitude+latitude+address)


                }
            })
//        return Triple(longitude, latitude,address)
    }

    private fun onClickFinish() {
        _binding.btnFinish.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_frag_add_place_to_mapsHome)
        }
    }


    private fun onClickAdd() {
        _binding.btnAdd.setOnClickListener {
            insertData()


        }
    }


    private fun insertData() {
        val placesName = _binding.edtPlace.text.toString().trim()
        val type = _binding.edtType.text.toString().trim()
        if (inputCheck(placesName, type)) {

            firebaseAuth = FirebaseAuth.getInstance()
            uId = firebaseAuth.currentUser?.uid.toString()

            dataBaseRef = FirebaseDatabase.getInstance().getReference("Places")
//            val latLng=LatLng(latitude,longitude)
            val myPlaces = MyPlaces(address, latitude,longitude,placesName,type)

            dataBaseRef.child(placesName).setValue(myPlaces).addOnCompleteListener {
                _binding.edtType.text?.clear()
                _binding.edtPlace.text?.clear()
                Toast.makeText(requireContext(), "Successfully added", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun inputCheck(placesName: String, type: String): Boolean {
        return !(TextUtils.isEmpty(placesName) && TextUtils.isEmpty(type))
    }

}