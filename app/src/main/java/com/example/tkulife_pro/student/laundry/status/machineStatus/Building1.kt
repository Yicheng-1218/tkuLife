package com.example.tkulife_pro.student.laundry.status.machineStatus

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tkulife_pro.databinding.FragmentBuilding1Binding
import com.example.tkulife_pro.student.laundry.status.SharedViewModel

class Building1(val selectFloor : String,val machineType:String) : Fragment(){

    private lateinit var binding: FragmentBuilding1Binding
    private lateinit var viewModel: SharedViewModel
    private var viewAdapter = StatusAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentBuilding1Binding.inflate(layoutInflater)
        return binding.root
    }

//    設定recyclerView
    private fun setRecyclerView(adapterData:ArrayList<HashMap<*,*>>) {
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recycleView1.apply {
            if (getLayoutManager()==null){
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
            }
            setHasFixedSize(true)
            setLayoutManager(layoutManager)

            adapter = viewAdapter //只建立一次FloorAdapter
        }
        viewAdapter.floor = "一館-${selectFloor}F"
        viewAdapter.machineData = adapterData
        viewAdapter.machineType = machineType
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        取得viewModel
        viewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
//        viewModel資料監聽
        viewModel.getRealtimeData().observe(viewLifecycleOwner,{ data->
            val type=data[machineType] as HashMap<*,*>
            val machineList = type["A-0${selectFloor}"] as ArrayList<HashMap<*,*>>
            setRecyclerView(machineList)
        })
    }



}