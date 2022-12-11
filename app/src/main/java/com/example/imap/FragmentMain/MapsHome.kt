package com.example.imap.FragmentMain
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.imap.FragmentMain.FragSign.LogIn
import com.example.imap.MainActivity2
import com.example.imap.R
import com.example.imap.databinding.FragmentMapsHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MapsHome : Fragment(), OnMapReadyCallback {
    private lateinit var _binding: FragmentMapsHomeBinding
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mgoogleMap: GoogleMap
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsHomeBinding.inflate(inflater, container, false)
        return _binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(  this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        onClickAddPlaces()
        firebaseAuth=FirebaseAuth.getInstance()


        _binding.btnLocation.setOnClickListener( View.OnClickListener {
            setUpMap()
        })
        onClickAddPlaces()
    }

    private fun onClickAddPlaces() {
        _binding.btnAddPlaces.setOnClickListener(View.OnClickListener {
            val currentUser = firebaseAuth.currentUser
            if (currentUser == null) {

            } else{
            val intent = Intent(requireContext(), MainActivity2::class.java)
            startActivity(intent)

        }

        })
    }

    @SuppressLint("MissingPermission")
    private fun setUpMap() {

        mgoogleMap.isMyLocationEnabled =true
        mgoogleMap.uiSettings.isMyLocationButtonEnabled =false
        fusedLocationClient.lastLocation.addOnSuccessListener {location ->
            if (location!=null){
                lastLocation=location
                val currenLatLong=LatLng(location.latitude,location.longitude)
                mgoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currenLatLong,16f))
            }
        }

    }

    override fun onMapReady(p0: GoogleMap) {
        mgoogleMap=p0
        setUpMap()
    }




}