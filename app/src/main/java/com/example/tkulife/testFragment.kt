package com.example.tkulife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_test_fragment.*

class testFragment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_fragment)

        button13.setOnClickListener {
            val manager=supportFragmentManager
            val Transaction = manager.beginTransaction()
            Transaction.replace(R.id.fragment2,BlankFragment()).commit()
        }

        button12.setOnClickListener {
            val manager=supportFragmentManager
            val Transaction=manager.beginTransaction()
            Transaction.replace(R.id.fragment2,BlankFragment2()).commit()
        }

        button9.setOnClickListener {
            val manager=supportFragmentManager
            val Transaction=manager.beginTransaction()
            Transaction.replace(R.id.fragment2,BlankFragment3()).commit()
        }
    }
}