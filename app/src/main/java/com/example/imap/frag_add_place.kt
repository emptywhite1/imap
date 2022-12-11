package com.example.imap

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.example.imap.data.Places
import com.example.imap.databinding.FragmentFragAddPlaceBinding
import com.example.imap.data.ViewModelPlaces as ViewModelPlaces1


class frag_add_place : Fragment() {
    private lateinit var _binding:FragmentFragAddPlaceBinding
    private lateinit var mPlacesViewModel:ViewModelPlaces1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentFragAddPlaceBinding.inflate(inflater,container,false)
        onClickFinish( )

        return _binding.root

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mPlacesViewModel= ViewModelProvider(this).get(ViewModelPlaces1 ::class.java)
        onClickFinish( )

    }



    private fun onClickFinish() {
        _binding.btnFinish.setOnClickListener {
            insertDatabase()
         }
    }

    private fun insertDatabase() {
        val placesName=_binding.edtPlace.text.toString().trim()
        val x=_binding.edtX.text.toString().trim()
        val y=_binding.edtY.text.toString().trim()
        val type=_binding.edtType.text.toString().trim()
        if (inputCheck(placesName,x,y,type)){

            val places=Places(0,type,placesName,"nomame,",x.toDouble(),y.toDouble())
            mPlacesViewModel.addPlaces(places)
            Toast.makeText(requireContext(),"Successfully added",Toast.LENGTH_SHORT).show()
        }

    }

    private fun inputCheck(placesName: String, x: String, y: String,type:String): Boolean {
        return !(TextUtils.isEmpty(placesName)&&TextUtils.isEmpty(x)&&TextUtils.isEmpty(y)&&TextUtils.isEmpty(type))
    }

}