package com.example.tkulife_pro.student.reminder

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tkulife_pro.*
import com.example.tkulife_pro.databinding.ActivityPushNotificationBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


class PushNotification : AppCompatActivity(),ReminderAdapter.OnItemClick {
    private lateinit var binding: ActivityPushNotificationBinding
    private lateinit var trashIntent:Intent
    private val viewAdapter=ReminderAdapter(this)
    private val hour=0
    private val minute=1

    private var trashReminder by Delegates.notNull<Boolean>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPushNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }
    private fun initView() {
//        設定BAR
        BarTool(this).setBundle("垃圾車提醒",R.color.barBlue)
//        取得開關狀態表
        val timerXML = SharedXML(this).getXML("timer")!!


//        到垃圾廣播意圖
        trashIntent=Intent(this,TrashReceiver::class.java)
        trashIntent.action="trash"


//        設定鬧鐘紐
        binding.button4.setOnClickListener {
            val c = Calendar.getInstance()
            val hourNow = c.get(Calendar.HOUR_OF_DAY)
            val minuteNow = c.get(Calendar.MINUTE)
            TimePickerDialog(this, { _, hour, minute ->
                val data = arrayOf(hour, minute, System.currentTimeMillis().toInt())
                addSQLTimer(data)
                upDateRecycler(getSQLTimer())

//              設定循環鬧鐘廣播，間隔1天
                setAlarm(AlarmManager.RTC_WAKEUP,hour,minute,86400000,data[2],trashIntent)

            }, hourNow, minuteNow, false).show()
        }
//        初始化RecyclerView
        upDateRecycler(getSQLTimer())


//        垃圾車提醒開關
        binding.switch1.apply {
            isChecked = timerXML.getBoolean("trashReminder", isChecked)
            trashReminder = isChecked
            setOnCheckedChangeListener { _, b ->
                trashReminder = isChecked
                timerXML.edit().putBoolean("trashReminder", b).apply()
                isChecked = timerXML.getBoolean("trashReminder", isChecked)
            }

        }

        setRecyclerView()
    }

    private fun emptyImg(){
        if (getSQLTimer().size==0){
            binding.imageView2.visibility= View.VISIBLE
            binding.textView8.visibility = View.VISIBLE
        }else{
            binding.imageView2.visibility= View.GONE
            binding.textView8.visibility = View.GONE
        }
    }

//    設定RecyclerView內容
    private fun setRecyclerView(){
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.RecyclerView.apply {

//              設定分隔線
            addItemDecoration(
                DividerItemDecoration(
                    this@PushNotification,
                    DividerItemDecoration.VERTICAL
                )
            )

            setHasFixedSize(true)
            setLayoutManager(layoutManager)
            adapter=viewAdapter
        }
    }
    private fun upDateRecycler(data: ArrayList<Array<Int>>){
        //設定傳入recyclerview參數
        viewAdapter.dataSet=data

        //    這裡沒有東西
        emptyImg()

    }

//    取得SQL資料
    private fun getSQLTimer(): ArrayList<Array<Int>> {
        //get sqlite table time
        val db=Sqlite(this)
        return db.getTimer()
    }

//    添加SQL資料
    private fun addSQLTimer(time:Array<Int>){
        //set sqlite table time
        val db=Sqlite(this)
        db.addTimer(time)
    }

//    覆寫RecyclerView的點擊監聽
    override fun onItemClick(position: Int) {
        val timerList=getSQLTimer()[position]
        println(position)
        val createAt=timerList[2]
        fun updateTimer(){
            TimePickerDialog(this,{
                    _,hour,minute->
                Sqlite(this).updateTimer(createAt,hour,minute)
                upDateRecycler(getSQLTimer())
            },timerList[hour],timerList[minute],false).show()

        }

        val alter=AlertDialog.Builder(this)
        alter.setTitle("提示")
        alter.setMessage("選擇修改或是刪除提醒")
        alter.setPositiveButton("修改"
        ) { _, _ -> updateTimer() }
        alter.setNegativeButton("刪除"
        ) { _, _ -> Sqlite(this).delTimer(createAt);upDateRecycler(getSQLTimer());cancelAlarm(createAt,trashIntent) }
        alter.setNeutralButton("取消",null)
        alter.show()

    }

//    overload SetAlarm
    private fun setAlarm(type: Int, hour: Int, minute: Int, intervalMillis: Long, requestCode: Int, intent: Intent) {
        val now = Calendar.getInstance()
        val targetTime = now.clone() as Calendar
        targetTime.set(Calendar.HOUR_OF_DAY, hour)
        targetTime.set(Calendar.MINUTE, minute)
        targetTime.set(Calendar.SECOND, 0)
        targetTime.set(Calendar.MILLISECOND, 0)
        if (targetTime.before(now))
            targetTime.add(Calendar.DATE, 1)
        setAlarm(type, targetTime.timeInMillis, intervalMillis, requestCode, intent)

    }

//    設定鬧鐘
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun setAlarm(type: Int, triggerAtMillis: Long, intervalMillis: Long, requestCode: Int, intent: Intent) {
        val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(type, triggerAtMillis, intervalMillis, PendingIntent.getBroadcast(this,
            requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT))
        Log.d("alarm","alarm set")
    }

//    取消鬧鐘
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun cancelAlarm(requestCode: Int, intent: Intent) {
        val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(
            PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT))
        Log.d("alarm","alarm cancel")
    }



//    廣播接收
    class TrashReceiver:BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {


//                發送提醒
            TkuNotification(p0!!, "垃圾車提醒", "垃圾車提醒").build("垃圾車提醒", "該倒垃圾啦(⁎⁍̴̛ᴗ⁍̴̛⁎)").show(R.string.trashReminder)
            val wakelock = p0.getSystemService(POWER_SERVICE) as PowerManager
//                兩個Flag缺一不可，都存在才可以點亮螢幕
            val newWake = wakelock.newWakeLock(
                PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_DIM_WAKE_LOCK,
                "MyApp::trash"
            )
//                自動熄滅時間
            newWake.acquire(5000 /*5 second*/)

        }
    }
}