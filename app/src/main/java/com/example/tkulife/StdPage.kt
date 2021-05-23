package com.example.tkulife

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_std_page.*
import java.util.function.LongFunction
import kotlin.math.log

class StdPage : AppCompatActivity(),AdapterView.OnItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_std_page)

        button4.setOnClickListener {
            startActivity(Intent(this,WashingMachine::class.java))
        }

        button14.setOnClickListener {
            startActivity(Intent(this,testFragment::class.java))
        }

        val data = listOf("test1", "test2", "test3")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, data)
        spinner.adapter = adapter
        spinner.onItemSelectedListener=this

        val testspinner = listOf("data1","data2","data3","data4")
        val adapter2=ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,testspinner)
        spinner3.adapter=adapter2
        spinner3.onItemSelectedListener=this


    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        val result=parent.getItemAtPosition(pos).toString()
        Log.d("DEBUG",result)

        when(result){
            "test1"->{
                val manager = supportFragmentManager
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.fragment,BlankFragment()).commit()

            }
            "test2"->{
                val manager = supportFragmentManager
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.fragment,BlankFragment2()).commit()
            }
            "test3"-> {
                val manager = supportFragmentManager
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.fragment, BlankFragment3()).commit()
            }
            "data1"->{
                Toast.makeText(this,"data1",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}