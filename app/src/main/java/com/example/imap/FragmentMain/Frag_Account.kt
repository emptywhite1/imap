package com.example.imap.FragmentMain

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.imap.R
import com.example.imap.databinding.FragmentFragAccountBinding
import com.google.firebase.auth.FirebaseAuth


class Frag_Account : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var _binding:FragmentFragAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentFragAccountBinding.inflate(inflater,container,false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth=FirebaseAuth.getInstance()


        logOut()
    }

    private fun logOut() {
        _binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()
        }
    }


}