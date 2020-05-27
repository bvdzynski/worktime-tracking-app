package com.project.worktimetrackingapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import kotlinx.android.synthetic.main.activity_developer_show_time_log.*
import kotlinx.android.synthetic.main.content_developer_show_time_log.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DeveloperShowTimeLogActivity : AppCompatActivity() {
    private val tag = this::class.simpleName
    private var timeLogId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developer_show_time_log)
        setSupportActionBar(toolbar)

        if (intent.extras != null) {
            timeLogId = intent.extras.getString(LOG_ID)
        }

        showTimeLog(DataManager.getTimeLogs(roleInTeam))

        timeLogShowCloseButton.setOnClickListener { view ->
            closeTimeLog(DataManager.getTimeLogs(roleInTeam))
            val intent = Intent(this, DeveloperListAllTimeLogsActivity::class.java)
            intent.putExtra(LOG_ID, timeLogId)
            this.startActivity(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(LOG_ID, timeLogId)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_show_time_log, menu)

        //if not dev, cannot edit time log
        if(roleInTeam != "dev"){
            val item = menu.findItem(R.id.action_edit)
            item.isVisible = false
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_edit -> {
                val intent = Intent(this, EditTimeLogActivity::class.java)
                intent.putExtra(LOG_ID, timeLogId)
                this.startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showTimeLog(timeLogs: List<TimeLogInfo>) {

        var chosenTimeLog = TimeLogInfo()

        timeLogs.forEach{
            if(it.timeLogId == timeLogId){
                chosenTimeLog = it
            }
        }

        timeLogShowProjectText.text = chosenTimeLog.project?.name.toString()
        timeLogShowTitleText.text = chosenTimeLog.title
        timeLogShowDescriptionText.setText(chosenTimeLog.description)
        timeLogShowWorkTimeText.text = chosenTimeLog.workTime
        timeLogShowLocationText.text = chosenTimeLog.location?.name.toString()
        timeLogShowCreatedAtText.text = chosenTimeLog.workTimeStart
        timeLogShowImageView.setImageURI(chosenTimeLog.imageURI)

        if(chosenTimeLog.status == "open"){
            if(timeLogShowCloseButton.visibility == View.INVISIBLE){
                timeLogShowCloseButton.visibility = View.VISIBLE
            }
        }
    }

    private fun closeTimeLog(timeLogs: List<TimeLogInfo>) {

        var chosenTimeLog = TimeLogInfo()
        timeLogs.forEach{
            if(it.timeLogId == timeLogId){
                chosenTimeLog = it
            }
        }

        chosenTimeLog.status = "closed"
        chosenTimeLog.workTimeStop = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
        chosenTimeLog.workTime = calculateWorkTime(chosenTimeLog.workTimeStart, chosenTimeLog.workTimeStop)

        val queue= Volley.newRequestQueue(this)
        val closeTimeLogRequest: StringRequest = object : StringRequest(Method.POST, CLOSE_TIME_LOG_URL,
                Response.Listener { response -> // response
                    Log.i("closed timeLog: ", chosenTimeLog.timeLogId)
                    Log.i("response from api: ", response.toString())
                },
                Response.ErrorListener {
                    Log.i("closing timeLog failed", "error")
                    Log.i("response from api: ", it.toString())
                }
        ) {
            override fun getParams(): Map<String, String> {
                var params: HashMap<String, String> = HashMap()
                params["timeLogId"] = chosenTimeLog.timeLogId
                params["status"] = chosenTimeLog.status
                params["workTimeStop"] = chosenTimeLog.workTimeStop.toString()
                params["workTime"] = chosenTimeLog.workTime.toString()
                return params
            }
        }

        queue.add(closeTimeLogRequest)
    }

    @SuppressLint("SimpleDateFormat")
    private fun calculateWorkTime(start: String? = null, stop: String? = null): String{
        val dateFormat: String = "yyyy/MM/dd HH:mm:ss"
        val startDate = SimpleDateFormat(dateFormat).parse(start)
        val endDate = SimpleDateFormat(dateFormat).parse(stop)
        val milliToHour : Double = 1000.0 * 60 * 60
        val calculatedHours : Double = ((endDate.time - startDate.time) / milliToHour)
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(calculatedHours)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.startActivity(Intent(this, DeveloperListAllTimeLogsActivity::class.java))
    }

    override fun onPause() {
        super.onPause()
    }

}











