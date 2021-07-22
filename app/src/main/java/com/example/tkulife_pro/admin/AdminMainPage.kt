package com.example.tkulife_pro.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tkulife_pro.admin.fixReport.FixNotification
import com.example.tkulife_pro.databinding.ActivityAdminMainPageBinding

class AdminMainPage : AppCompatActivity() {
    private lateinit var binding:ActivityAdminMainPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAdminMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView(){
//        返回鍵
        binding.button13.setOnClickListener{
            super.onBackPressed()
        }

//        報修通知按鈕
        binding.imageButton7.setOnClickListener{
            Intent(this,FixNotification::class.java).apply{
                startActivity(this)
            }
        }
    }
}