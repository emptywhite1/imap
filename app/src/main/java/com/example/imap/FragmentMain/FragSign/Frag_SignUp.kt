package com.example.imap.FragmentMain.FragSign

import android.annotation.SuppressLint
import android.app.ProgressDialog.show
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatDrawableManager

import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import com.example.imap.MainActivity
import com.example.imap.R
import com.example.imap.User
import com.example.imap.databinding.FragmentFragSignUpBinding
import com.github.razir.progressbutton.*
import com.google.android.gms.tasks.OnCompleteListener

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class Frag_SignUp : Fragment() {
    private lateinit var _binding: FragmentFragSignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var rePassword: String
    private lateinit var fullName: String
    private lateinit var database: FirebaseDatabase
//    private var checkmail=""
//    private var checkPass=""
//    private var checkRePass=""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFragSignUpBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        emailFocusListenner()
        passwordFocusListenner()
        namelFocusListenner()
        re_passwordFocusListenner()
        _binding.btnSignup.attachTextChangeAnimator()
        bindProgressButton(_binding.btnSignup)
        onClickSignUp()


    }




    private fun onClickSignUp() {

        _binding.btnSignup.setOnClickListener {

            val animatedDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.animated_check)!!
            //Defined bounds are required for your drawable
            val drawableSize = resources.getDimensionPixelSize(R.dimen.doneSize)
            animatedDrawable.setBounds(0, 0, drawableSize, drawableSize)
            _binding.btnSignup.showProgress {
                buttonTextRes = R.string.signing
                progressColor = Color.WHITE
            }
            _binding.btnSignup.isEnabled = false

            email = _binding.edtEmail.text.toString().trim()
            password = _binding.edtPassword.text.toString().trim()
            rePassword = _binding.edtRepass.text.toString().trim()
            fullName = _binding.edtName.text.toString().trim()
            if (valid()) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { it ->
//
                        if (it.isSuccessful) {
                            val databaseRef = database.reference.child("Users")
                                .child(firebaseAuth.currentUser!!.uid)
                            val users = User(fullName, email, firebaseAuth.currentUser!!.uid)
                            databaseRef.setValue(users).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Handler().postDelayed({
                                    _binding.btnSignup.isEnabled = true

                                    _binding.btnSignup.showDrawable(animatedDrawable) {
                                        buttonTextRes = R.string.signed
                                    }

                                    Handler().postDelayed({
                                        _binding.edtName.text!!.clear()
                                        _binding.edtEmail.text!!.clear()
                                        _binding.edtPassword.text!!.clear()
                                        _binding.edtRepass.text!!.clear()
                                        _binding.btnSignup.hideDrawable(R.string.sign_up)

                                        if (requireActivity() is ViewPagerScroll) {
                                            (requireActivity() as ViewPagerScroll).setScroll(0, true)
                                        }
                                    }, 1000)
                                    }, 1000)




                                } else {
                                    Handler().postDelayed({
                                        _binding.btnSignup.isEnabled = true

                                        _binding.btnSignup.showDrawable(animatedDrawable) {
                                            buttonTextRes = R.string.failer
                                        }

                                        Handler().postDelayed({

                                            _binding.btnSignup.hideDrawable(R.string.sign_up)

                                            if (requireActivity() is ViewPagerScroll) {
                                                (requireActivity() as ViewPagerScroll).setScroll(0, true)
                                            }
                                        }, 1000)
                                    }, 1000)
                                }
                            }

                        } else Toast.makeText(
                            requireContext(),
                            it.exception.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

            }


        }

    }

    private fun valid(): Boolean {
        return validEmail() == null && validPassword() == null && validRe_Password() == null && validName() == null

    }

    private fun emailFocusListenner() {
        _binding.edtEmail.setOnFocusChangeListener { v, focused ->
            if (!focused) {
                _binding.emailContainer.helperText = validEmail()
            }
        }
    }

    private fun namelFocusListenner() {
        _binding.edtName.setOnFocusChangeListener { v, focused ->
            if (!focused) {
                _binding.nameContainer.helperText = validName()
            }
        }
    }

    private fun validEmail(): String? {
        email = _binding.edtEmail.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Invalid e-mail address"
        } else
            return null

    }

    private fun validName(): String? {
        fullName = _binding.edtName.text.toString()
        if (fullName.isEmpty()) {
            return "Name is empty"
        } else
            return null

    }

    private fun passwordFocusListenner() {
        _binding.edtPassword.setOnFocusChangeListener { v, focused ->
            if (!focused) {
                _binding.passContainer.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String? {
        password = _binding.edtPassword.text.toString()
        rePassword = _binding.edtRepass.text.toString()

        if (password.length < 8) {
            return "Minimum 8 character password !"
        }
        if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&*()><?!/+=])(?=\\S+\$).{8,}\$".toRegex())) {
            return "Must contain 1 special character(@#\$%^&*()><?!/+=)"
        }
        return null
    }

    private fun re_passwordFocusListenner() {
        _binding.edtRepass.setOnFocusChangeListener { v, focused ->
            if (!focused) {
                _binding.repassContainner.helperText = validRe_Password()
            }
        }
    }

    private fun validRe_Password(): String? {
        password = _binding.edtPassword.text.toString()
        rePassword = _binding.edtRepass.text.toString()

        if (!rePassword.equals(password)) {
            return "Password  does'nt match"
        } else return null
    }


    private fun showMixed(button: Button) {
        val animatedDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.animated_check)!!
        //Defined bounds are required for your drawable
        val drawableSize = resources.getDimensionPixelSize(R.dimen.doneSize)
        animatedDrawable.setBounds(0, 0, drawableSize, drawableSize)

        button.showProgress {
            buttonTextRes = R.string.signing
            progressColor = Color.WHITE
        }
        button.isEnabled = false


        Handler().postDelayed({
            button.isEnabled = true

            button.showDrawable(animatedDrawable) {
                buttonTextRes = R.string.signed
            }
            Handler().postDelayed({
                button.hideDrawable(R.string.sign_up)
            }, 2000)
        }, 3000)
    }
}











