package com.example.imap.FragmentMain.FragSign

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        email = _binding.edtEmail.text.toString().trim()
        passWord = _binding.edtPass.text.toString().trim()

        _binding.btnLogin.setOnClickListener {
            firebaseAuth.signInWithEmailAndPassword(email, passWord).addOnCompleteListener {
                if (it.isSuccessful) {
                    val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
                    firebaseUser?.sendEmailVerification()
                    if (firebaseUser != null) {
                        if (firebaseUser.isEmailVerified) {
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        } else {
                            firebaseAuth.signOut()
                            firebaseUser.sendEmailVerification()
                            val builder = AlertDialog.Builder(requireContext())

                            builder.setTitle("Email verification")
                            builder.setMessage("You have not verified your email. Please check your Email !!!")

                            builder.setPositiveButton(
                                "YES"
                            ) { dialog, which -> // Do nothing but close the dialog

                                dialog.dismiss()
                            }
                            val alert = builder.create()
                            alert.show()
                        }
                    }

                } else {

                }
            }
        }
    }

}