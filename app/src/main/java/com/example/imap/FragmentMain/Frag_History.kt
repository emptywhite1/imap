package com.example.imap.FragmentMain

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.imap.databinding.FragmentFragHistoryBinding


class Frag_History : Fragment() {

    private lateinit var _binding: FragmentFragHistoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFragHistoryBinding.inflate(inflater, container, false)
        return _binding.root
    }


}