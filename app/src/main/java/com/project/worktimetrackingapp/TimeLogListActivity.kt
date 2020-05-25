package com.project.worktimetrackingapp
//
//import android.content.Intent
//import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
//import android.support.v7.widget.LinearLayoutManager
//
//import kotlinx.android.synthetic.main.activity_time_log_list.*
//import kotlinx.android.synthetic.main.content_time_log_list.*
//
//class TimeLogListActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_time_log_list)
//        setSupportActionBar(toolbar)
//
//        addTimeLogButton.setOnClickListener { view ->
//            val activityIntent = Intent(this, EditTimeLogActivity::class.java)
//            startActivity(activityIntent)
//        }
//
//        listItems.layoutManager = LinearLayoutManager(this)
//
//        listItems.adapter = TimeLogRecyclerAdapter(this, DataManager.timeLogs)
//
//    }
//
//    override fun onResume() {
//        super.onResume()
//        listItems.adapter.notifyDataSetChanged()
//    }
//}
//
//
//
//
//

