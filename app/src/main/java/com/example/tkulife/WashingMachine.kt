package com.example.tkulife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest

class WashingMachine : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_washing_machine)
        val url = "https://jsonplaceholder.typicode.com/users"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("DEBUG","code 200")

                val layoutManager = LinearLayoutManager(this)
                layoutManager.orientation = LinearLayoutManager.VERTICAL
                val dataList = findViewById<RecyclerView>(R.id.data_list)
                dataList.layoutManager = layoutManager
                dataList.adapter = RecyclerAdapter(response)
            },
            { error ->

                Log.d("DEBUG",error.toString())
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest)

    }
}