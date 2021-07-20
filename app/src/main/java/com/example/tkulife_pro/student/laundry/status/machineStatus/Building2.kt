package com.example.tkulife_pro.student.laundry.status.machineStatus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.tkulife_pro.R
import com.example.tkulife_pro.databinding.FragmentBuilding2Binding
import com.example.tkulife_pro.student.laundry.status.SharedViewModel

class Building2 : Fragment() {

    private lateinit var binding: FragmentBuilding2Binding
    private lateinit var viewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentBuilding2Binding.inflate(layoutInflater)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        viewModel.getRealtimeData().observe(viewLifecycleOwner,{ data->
//            TODO UPDATE UI ON HERE

        })
    }

}