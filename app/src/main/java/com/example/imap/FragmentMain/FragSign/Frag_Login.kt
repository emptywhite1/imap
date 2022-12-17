package com.example.imap.FragmentMain.FragSign

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.imap.MainActivity
import com.example.imap.databinding.FragmentFragLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Frag_Login : Fragment() {
    private lateinit var _binding: FragmentFragLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var email: String
    private lateinit var passWord: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFragLoginBinding.inflate(inflater, container, false)
        return _binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        onClickSignIn()

    }

    private fun onClickSignIn() {



        _binding.btnLogin.setOnClickListener {
            email = _binding.edtEmail.text.toString().trim()
            passWord = _binding.edtPass.text.toString().trim()
            Log.d("vvvvvvvvvvv", "onClickSignIn: $email $passWord")
           firebaseAuth.signInWithEmailAndPassword(email,passWord).addOnCompleteListener {
               if (it.isSuccessful){

                   val intent=Intent(requireContext(),MainActivity::class.java)
                   startActivity(intent)
                   activity?.finish()
               }
           }
        }
    }

}