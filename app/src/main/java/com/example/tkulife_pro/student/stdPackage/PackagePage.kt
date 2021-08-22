package com.example.tkulife_pro.student.stdPackage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tkulife_pro.R
import com.example.tkulife_pro.databinding.ActivityPackagePageBinding
import com.example.tkulife_pro.OkHttpUtil
import com.example.tkulife_pro.OkHttpUtil.Companion.mOkHttpUtil
import com.example.tkulife_pro.admin.fixReport.FixNotification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class PackagePage : AppCompatActivity() ,PackageAdapter.OnItemClick {
    private lateinit var binding: ActivityPackagePageBinding
    private lateinit var uid : String
    private val viewAdapter = PackageAdapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPackagePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onResume() {
        super.onResume()
        //        取得目前登入的使用者
        val user = Firebase.auth.currentUser


//        分割EMAIL取得學號
        uid = user?.email?.split('@')?.get(0).toString()

//        向server請求包裹清單
        mOkHttpUtil.getAsync(
            "https://tkudorm.site/pklist/${uid}",
            object : OkHttpUtil.ICallback {
                override fun onResponse(response: Response) {
                    try {
                        val value = response.body?.string()
                        val list = JSONArray(value)
                        val untaken = ArrayList<JSONObject>().also { untaken ->
                            for (i in 0 until list.length()) {
                                val pk = list.get(i) as JSONObject
                                if (pk["taken"] == false) {
                                    untaken.add(pk)
                                }
                            }
                        }
                        runOnUiThread {
                            setRecyclerView(JSONArray(untaken))
//                    關閉loading圖示
                            binding.progressBar4.isVisible = false
                        }
                    }catch (e:Exception){
                        runOnUiThread {
                            Toast.makeText(this@PackagePage,"您尚未登入",Toast.LENGTH_LONG).show()
                            onBackPressed()
                        }
                    }


                }

                override fun onFailure(e: IOException) {

                }

            })

    }
    private fun initView() {
//        開啟loading圖示
        binding.progressBar4.isVisible = true

    }

    //    設定recyclerView
    private fun setRecyclerView(packageList: JSONArray) {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.packageRecycler.apply {
            if (getLayoutManager() == null) {
                addItemDecoration(
                    DividerItemDecoration(
                        this@PackagePage,
                        DividerItemDecoration.VERTICAL
                    )
                )
            }
            setHasFixedSize(true)
            setLayoutManager(layoutManager)
            adapter = viewAdapter
        }
        viewAdapter.packageList = packageList
    }

    override fun onItemClick(position: Int) {
        val confirm = AlertDialog.Builder(this)
        confirm.setMessage("確認要簽收此包裹嗎?")
        confirm.setTitle("確認視窗")
        confirm.setNegativeButton("取消", null)
        confirm.setPositiveButton("確定") { _, _ ->
//                請求附加資料
            val body = JSONObject("{'index':'${position}'}")
//                管理員put請求(broken)
            mOkHttpUtil.put(
                "https://tkudorm.site/pklist/${uid}",
                body,
                object : OkHttpUtil.ICallback {
                    //                        非同步線程
                    override fun onResponse(response: Response) {
                        val res = response.body?.string()
                        runOnUiThread {
//                                UI線程

//                                顯示server回傳
                            Toast.makeText(
                                this@PackagePage,
                                JSONObject(res!!)["msg"].toString(),
                                Toast.LENGTH_LONG
                            ).show()
                            onResume() //重整畫面
                        }
                    }

                    override fun onFailure(e: IOException) {

                    }
                }
            )
        }.show()
    }
}