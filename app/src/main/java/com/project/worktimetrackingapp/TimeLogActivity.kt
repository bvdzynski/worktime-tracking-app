package com.project.worktimetrackingapp

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter

import kotlinx.android.synthetic.main.activity_edit_time_log.*
import kotlinx.android.synthetic.main.content_main.*

class TimeLogActivity : AppCompatActivity() {
    private val tag = this::class.simpleName
    private var timeLogPosition = POSITION_NOT_SET

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_time_log)
        setSupportActionBar(toolbar)

        val adapterProjects = ArrayAdapter<ProjectInfo>(this,
                android.R.layout.simple_spinner_item,
                DataManager.projects.values.toList())
        adapterProjects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerProjects.adapter = adapterProjects

        timeLogPosition = savedInstanceState?.getInt(LOG_POSITION, POSITION_NOT_SET) ?:
                intent.getIntExtra(LOG_POSITION, POSITION_NOT_SET)

        if(timeLogPosition != POSITION_NOT_SET)
            displayTimeLog()
        else {
            createNewTimeLog()
        }
        Log.d(tag, "onCreate")
    }

    private fun createNewTimeLog() {
        DataManager.timeLogs.add(TimeLogInfo())
        timeLogPosition = DataManager.timeLogs.lastIndex
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(LOG_POSITION, timeLogPosition)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
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
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun displayTimeLog() {
        if(timeLogPosition > DataManager.timeLogs.lastIndex) {
            showMessage("Time log not found")
            Log.e(tag, "Invalid time log position $timeLogPosition, max valid position ${DataManager.timeLogs.lastIndex}")
            return
        }

        Log.i(tag, "Displaying time log for position $timeLogPosition")
        val timeLog = DataManager.timeLogs[timeLogPosition]
        textTimeLogTitle.setText(timeLog.header)
        textTimeLogDescription.setText(timeLog.description)

        val projectPosition = DataManager.projects.values.indexOf(timeLog.project)
        spinnerProjects.setSelection(projectPosition)
    }

    private fun showMessage(message: String) {
        Snackbar.make(textTimeLogTitle, message, Snackbar.LENGTH_LONG).show()
    }


    private fun moveNext() {
        ++timeLogPosition
        displayTimeLog()
        invalidateOptionsMenu()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if(timeLogPosition >= DataManager.timeLogs.lastIndex) {
            val menuItem = menu?.findItem(R.id.action_next)
            if(menuItem != null) {
                menuItem.icon = getDrawable(R.drawable.ic_block_white_24dp)
            }
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onPause() {
        super.onPause()
        saveTimeLog()
        Log.d(tag, "onPause")
    }

    private fun saveTimeLog() {
        val timeLog = DataManager.timeLogs[timeLogPosition]
        timeLog.header = textTimeLogTitle.text.toString()
        timeLog.description = textTimeLogDescription.text.toString()
        timeLog.project = spinnerProjects.selectedItem as ProjectInfo
    }
}











