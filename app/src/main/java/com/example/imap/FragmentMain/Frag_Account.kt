package com.example.imap.FragmentMain

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.imap.FragmentMain.FragSign.LogIn
import com.example.imap.R
import com.example.imap.databinding.FragmentFragAccountBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class Frag_Account : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    private lateinit var _binding: FragmentFragAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFragAccountBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()



        if (firebaseAuth.currentUser != null) {
            getData()
            _binding.btnLogout.text = "Logout"
            logOut()

        } else {
            _binding.btnLogout.text = "Login"
            _binding.btnLogout.setOnClickListener {
                val intent = Intent(requireContext(), LogIn::class.java)
                startActivity(intent)
            }
        }
        getData()


    }

    private fun getData() {
        if (firebaseAuth.currentUser != null) {
            dbRef = FirebaseDatabase.getInstance().getReference("Users")
            dbRef.child(firebaseAuth.currentUser!!.uid).get().addOnSuccessListener {
                if (it.exists()) {
                    val name = it.child("fullName").value
                    val mail = it.child("email").value
                    _binding.txtName.text = name.toString()
                    _binding.txtName1.text = "Full name: $name"
                    _binding.txtEmail.text = "Email: $mail"
                } else {

                }
            }.addOnFailureListener {

            }
        }
    }

    private fun logOut() {
        _binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            val intent=Intent(requireContext(),LogIn::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }


}