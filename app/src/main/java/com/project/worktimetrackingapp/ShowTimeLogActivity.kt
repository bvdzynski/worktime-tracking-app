package com.project.worktimetrackingapp

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_show_time_log.*
import kotlinx.android.synthetic.main.content_show_time_log.*

class ShowTimeLogActivity : AppCompatActivity() {
    private val tag = this::class.simpleName
    private var timeLogPosition = POSITION_NOT_SET

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_time_log)
        setSupportActionBar(toolbar)

        timeLogPosition = savedInstanceState?.getInt(LOG_POSITION, POSITION_NOT_SET) ?:
                intent.getIntExtra(LOG_POSITION, POSITION_NOT_SET)

        showTimeLog()

        Log.d(tag, "onCreate")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(LOG_POSITION, timeLogPosition)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_show_time_log, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_next -> {
                if(timeLogPosition < DataManager.timeLogs.lastIndex) {
                    moveNext()
                } else {
                    val message = "No more time logs"
                    showMessage(message)
                }
                true
            }
            R.id.action_edit -> {
                val intent = Intent(this, EditTimeLogActivity::class.java)
                intent.putExtra(LOG_POSITION, timeLogPosition)
                this.startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showTimeLog() {
        if(timeLogPosition > DataManager.timeLogs.lastIndex) {
            showMessage("Time log not found")
            Log.e(tag, "Invalid time log position $timeLogPosition, max valid position ${DataManager.timeLogs.lastIndex}")
            return
        }

        Log.i(tag, "Displaying time log for position $timeLogPosition")
        val choosenTimeLog = DataManager.timeLogs[timeLogPosition]

        timeLogShowProjectText.text = choosenTimeLog.project?.name.toString()
        timeLogShowTitleText.text = choosenTimeLog.title
        timeLogShowDescriptionText.setText(choosenTimeLog.description)
        timeLogShowWorkTimeText.text = choosenTimeLog.workTime.toString()
        timeLogShowLocationText.text = choosenTimeLog.location?.name.toString()
        timeLogShowCreatedAtText.text = choosenTimeLog.createdAt
        timeLogShowImageText.text = choosenTimeLog.imageUrl

    }

    private fun showMessage(message: String) {
        Snackbar.make(timeLogShowTitleText, message, Snackbar.LENGTH_LONG).show()
    }

    private fun moveNext() {
        ++timeLogPosition
        showTimeLog()
        invalidateOptionsMenu()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.startActivity(Intent(this, DeveloperViewActivity::class.java))
    }

    override fun onPause() {
        super.onPause()
        Log.d(tag, "onPause")
    }

}











