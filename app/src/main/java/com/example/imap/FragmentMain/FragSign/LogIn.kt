package com.example.imap.FragmentMain.FragSign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.imap.Adapter
import com.example.imap.databinding.ActivityLogInBinding
import com.google.android.material.tabs.TabLayoutMediator

interface ViewPagerScroll {
    fun setScroll(index: Int, anim: Boolean)
}

class LogIn : AppCompatActivity(), ViewPagerScroll {
    private lateinit var _binding: ActivityLogInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val fragments: ArrayList<Fragment> = arrayListOf(
            Frag_Login(), Frag_SignUp()
        )
        _binding.viewPgLogin.adapter= Adapter(fragments,this)
        TabLayoutMediator(_binding.tabLogin, _binding.viewPgLogin) { tab, index ->

                tab.text = when (index) {
                    0 -> {
                        "LogIn"
                    }
                    1 -> {
                        "SignUp"
                    }
                    else -> return@TabLayoutMediator
                }

        }.attach()

    }

    override fun setScroll(index: Int, anim: Boolean) {
        _binding.viewPgLogin.setCurrentItem(index, anim)

    }

}