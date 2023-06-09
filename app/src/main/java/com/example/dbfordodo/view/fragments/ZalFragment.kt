package com.example.dbfordodo.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dbfordodo.databinding.FragmentZalBinding


class ZalFragment : Fragment() {

    private var _binding : FragmentZalBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentZalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.container1.setOnClickListener {
            val action =
              ZalFragmentDirections.actionZalFragmentToNavigationHome(null,

                    binding.locationName.toString()
              )
            findNavController().navigate(action)
        }

        binding.container2.setOnClickListener {
            val action =
                ZalFragmentDirections.actionZalFragmentToNavigationHome(
                    null,
                    binding.locationName2.toString()
                )
            findNavController().navigate(action)
        }
    }

}