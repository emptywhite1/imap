package com.example.imap.FragmentMain

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.imap.FragmentMain.FragSign.LogIn
import com.example.imap.R
import com.example.imap.data.MyPlaces
import com.example.imap.databinding.FragmentMapsHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.*
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.io.IOException


class MapsHome : Fragment(), OnMapReadyCallback ,OnMarkerClickListener{
    private lateinit var _binding: FragmentMapsHomeBinding
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mgoogleMap: GoogleMap
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var supportMapFragment: SupportMapFragment
    private var apiKey = "AIzaSyCU47D0gyMitDIPfeM0TXlllo9DSOLdejU"
    private lateinit var placesClient: PlacesClient
    private lateinit var listPosion:ArrayList<LatLng>
    private var x= 0


    private lateinit var dbReference: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsHomeBinding.inflate(inflater, container, false)
        return _binding.root
//        getDataPlaces()

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        onClickAddPlaces()
        firebaseAuth = FirebaseAuth.getInstance()
        _binding.btnDirec.isInvisible=true

        _binding.btnLocation.setOnClickListener(View.OnClickListener {
            setUpMap()
            getDataPlaces()
        })
        onClickAddPlaces()
//        getDataPlaces()
//        addMarker()
        searchPlaces()
        onClickMarkDirec()



    }

    private fun onChangeMapType() {


        _binding.btnLayer.setOnClickListener {

            if (x>=4){
                x=0
            }
            x += 1
            Log.d("hhhhhhhhhhhhh", "onChangeMapType: "+x)
            when(x){
                0-> mgoogleMap.mapType= MAP_TYPE_NORMAL
                1-> mgoogleMap.mapType= MAP_TYPE_TERRAIN
                2-> mgoogleMap.mapType= MAP_TYPE_SATELLITE
                3->mgoogleMap.mapType= MAP_TYPE_HYBRID

            }
        }



    }

    private fun onClickMarkDirec() {
        _binding.btnDirec.setOnClickListener {
            val gmmIntentUri = Uri.parse("google.navigation:q=${listPosion[0].latitude},${listPosion[0].longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)

        }

    }

    private fun searchPlaces() {
        with(_binding) {
            searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    var locationss = searchview.query.toString()
                    var listAddress: List<Address>? = null
                    if (locationss != null && locationss != "") {
                        var geocoder: Geocoder = Geocoder(requireActivity())
                        try {
                            listAddress = geocoder.getFromLocationName(locationss, 1)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                        if (listAddress != null) {
                            if (listAddress.isNotEmpty()) {
                                var address: Address = listAddress!![0]
                                var latLng: LatLng = LatLng(address.latitude, address.longitude)
                                mgoogleMap.addMarker(
                                    MarkerOptions().position(latLng).title(locationss)
                                )
                                mgoogleMap.animateCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        latLng,
                                        12f
                                    )
                                )
                            } else {
                                Toast.makeText(requireContext(), "Not Found", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    }

                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })

        }


    }


    private fun getDataPlaces() {
        var places = arrayListOf<MyPlaces>()
        dbReference = FirebaseDatabase.getInstance().getReference("Places")
        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (placesSnap in snapshot.children) {

                        val myPlacesData = placesSnap.getValue(MyPlaces::class.java)
                        places.add(myPlacesData!!)

                    }
                    Log.d("====", "onDataChange: " + places[1].namePlaces)
                    for (i in 0 until places.size) {
                        mgoogleMap.addMarker(
                            MarkerOptions().position(
                                LatLng(
                                    places[i].latitude!!,
                                    places[i].longitude!!
                                )
                            ).title(places[i].namePlaces + "//" + places[i].address)
                        )
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })


    }

    private fun onClickAddPlaces() {
        _binding.btnAddPlaces.setOnClickListener(View.OnClickListener {
            val currentUser = firebaseAuth.currentUser
            if (currentUser == null) {
                startActivity(Intent(requireContext(), LogIn::class.java))

            } else {
                view?.let { it1 ->
                    Navigation.findNavController(it1)
                        .navigate(R.id.action_mapsHome_to_frag_add_place)
                }
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun setUpMap() {
        mgoogleMap.isMyLocationEnabled = true
        mgoogleMap.uiSettings.isMyLocationButtonEnabled = false
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                lastLocation = location
                val currenLatLong = LatLng(location.latitude, location.longitude)
                mgoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currenLatLong, 16f))
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        mgoogleMap = p0
        onChangeMapType()
        setUpMap()
        getDataPlaces()
        mgoogleMap.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        listPosion= ArrayList()
        _binding.btnDirec.isInvisible=false
        listPosion.clear()
        Toast.makeText(requireActivity(), "" + p0.position, Toast.LENGTH_SHORT).show()
        Log.d("xxxxxxxx", "onMarkerClick: " + p0.position)
        val latLng=LatLng(p0.position.latitude,p0.position.longitude)
        listPosion.add(latLng)
        return false
    }


}



