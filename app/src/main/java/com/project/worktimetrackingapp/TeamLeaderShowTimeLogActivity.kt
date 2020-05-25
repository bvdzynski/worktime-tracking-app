package com.project.worktimetrackingapp

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_team_leader_show_time_log.*
import kotlinx.android.synthetic.main.content_developer_show_time_log.*
import kotlinx.android.synthetic.main.content_team_leader_show_time_log.*
import kotlinx.android.synthetic.main.content_team_leader_show_time_log.timeLogShowCreatedAtText
import kotlinx.android.synthetic.main.content_team_leader_show_time_log.timeLogShowDescriptionText
import kotlinx.android.synthetic.main.content_team_leader_show_time_log.timeLogShowImageView
import kotlinx.android.synthetic.main.content_team_leader_show_time_log.timeLogShowLocationText
import kotlinx.android.synthetic.main.content_team_leader_show_time_log.timeLogShowProjectText
import kotlinx.android.synthetic.main.content_team_leader_show_time_log.timeLogShowTitleText
import kotlinx.android.synthetic.main.content_team_leader_show_time_log.timeLogShowWorkTimeText

class TeamLeaderShowTimeLogActivity : AppCompatActivity() {
    private val tag = this::class.simpleName
    private var timeLogId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_leader_show_time_log)
        setSupportActionBar(toolbar)

        if (intent.extras != null) {
            timeLogId = intent.extras.getString(LOG_ID)
        }

        showTimeLog(DataManager.getTimeLogs(roleInTeam))
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showTimeLog(timeLogs: List<TimeLogInfo>) {

        var chosenTimeLog = TimeLogInfo()

        timeLogs.forEach{
            Log.i(tag, "timelogid: ${it.timeLogId}")
            if(it.timeLogId == timeLogId){
                chosenTimeLog = it
            }
        }

        timeLogShowProjectText.text = chosenTimeLog.project?.name.toString()
        timeLogShowTitleText.text = chosenTimeLog.title
        timeLogShowDescriptionText.setText(chosenTimeLog.description)
        timeLogShowWorkTimeText.text = chosenTimeLog.workTime
        timeLogShowLocationText.text = chosenTimeLog.location?.name.toString()
        timeLogShowCoordinatesText.text = chosenTimeLog.coords
        timeLogShowCreatedAtText.text = chosenTimeLog.workTimeStart
        timeLogShowImageView.setImageURI(chosenTimeLog.imageURI)

    }

    private fun showMessage(message: String) {
        Snackbar.make(timeLogShowTitleText, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.startActivity(Intent(this, TeamLeaderViewActivity::class.java))
    }

    override fun onPause() {
        super.onPause()
        Log.d(tag, "onPause")
    }

}











