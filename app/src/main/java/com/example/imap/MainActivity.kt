package com.example.imap

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log.i
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.imap.FragmentMain.FragSign.Frag_Login
import com.example.imap.FragmentMain.FragSign.Frag_SignUp
import com.example.imap.FragmentMain.MapsHome
import com.example.imap.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

interface requetPermistion{
  fun  requestPermistion()

}
class MainActivity : AppCompatActivity() ,requetPermistion{
    private lateinit var _binding: ActivityMainBinding
    private lateinit var navController:NavController





    private lateinit var permisstionLaucher: ActivityResultLauncher<Array<String>>
    private var isReadPermissionGrand = false
    private var isLocationPermissionGrand = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
//        val fragments: ArrayList<Fragment> = arrayListOf(
//            MapsHome(), Frag_History(), Frag_Account()
//        )
            val  navHostFragments=supportFragmentManager.findFragmentById(R.id.frag_Container) as NavHostFragment
        navController=navHostFragments.navController
        NavigationUI.setupWithNavController(_binding.navMain,navController)




        permisstionLaucher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissons ->
                isReadPermissionGrand =
                    permissons[android.Manifest.permission.READ_EXTERNAL_STORAGE]
                        ?: isReadPermissionGrand
                isLocationPermissionGrand =
                    permissons[android.Manifest.permission.ACCESS_FINE_LOCATION]
                        ?: isLocationPermissionGrand


            }
        requestPermistion()
//        searchPlaces()
    }

    override fun requestPermistion() {
        isReadPermissionGrand = ContextCompat.checkSelfPermission(
            this,
            android.Manifest
                .permission.READ_EXTERNAL_STORAGE
        ) == PackageManager
            .PERMISSION_GRANTED
        isLocationPermissionGrand = ContextCompat.checkSelfPermission(
            this,
            android.Manifest
                .permission.ACCESS_FINE_LOCATION
        ) == PackageManager
            .PERMISSION_GRANTED
        val permissionRequest: MutableList<String> = ArrayList()
        if (!isReadPermissionGrand) {
            permissionRequest.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!isReadPermissionGrand) {
            permissionRequest.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (permissionRequest.isNotEmpty()) {
            permisstionLaucher.launch(permissionRequest.toTypedArray())
            ContextCompat.getDrawable(this, R.drawable.animated_check)

        }
    }

//




}