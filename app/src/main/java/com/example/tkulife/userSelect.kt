package com.example.tkulife

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.text.set
import androidx.core.view.get
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_user_select.*
import java.util.*
import kotlin.properties.Delegates

class userSelect : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var index=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_select)
        auth = Firebase.auth
        switch1.setOnCheckedChangeListener { switch1, isChecked ->
            var stat=switch1.isChecked
            if (stat){
                Toast.makeText(this,"開啟提醒",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"關閉提醒",Toast.LENGTH_SHORT).show()
            }

        }
        button3.setOnClickListener {
            val c = Calendar.getInstance()
            var hour = c.get(Calendar.HOUR_OF_DAY)
            var minute = c.get(Calendar.MINUTE)
            TimePickerDialog(this,{
                _,hour,minute->textView8.text="$hour:$minute"
            },hour,minute,false).show()
//            TODO 待完成項目
//            FIXME 待維修項目
        }
        imageButton.setOnClickListener {
            if (auth.currentUser.email=="t@t.tt"){
                AlertDialog.Builder(this).setMessage("前往管理頁面").setTitle("提示").setPositiveButton("OK", null).show()
            }else{
                AlertDialog.Builder(this).setMessage("您非管理員，無法訪問").setTitle("提示").setPositiveButton("OK", null).show()
            }
        }
        var switch_list= arrayListOf<Switch>(switch2,switch5,switch6)

        button8.setOnClickListener {
            switch_list.get(index).text="提醒時間:${textView8.text}"
            index++
            if (index>=switch_list.size){
                index=0
            }
        }

        imageButton2.setOnClickListener {
            startActivity(Intent(this,StdPage::class.java))
        }



    }
}