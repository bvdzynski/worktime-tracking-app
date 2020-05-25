package com.project.worktimetrackingapp

import android.content.Intent
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_start.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class StartActivity : AppCompatActivity() {
    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        fullscreen_content.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
    private val mShowPart2Runnable = Runnable {
        // Delayed display of UI elements
        supportActionBar?.show()
    }
    private var mVisible: Boolean = false
    private val mHideRunnable = Runnable { hide() }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private val mDelayHideTouchListener = View.OnTouchListener { _, _ ->
        if (AUTO_HIDE) {
            delayedHide(AUTO_HIDE_DELAY_MILLIS)
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        LocationHelper().startListeningUserLocation(this , object : LocationHelper.MyLocationListener {
            override fun onLocationChanged(location: Location) {
                Log.d("current location: ", location.latitude.toString() + "," + location.longitude.toString())
                currentCoordinates = location.latitude.toString() + "," + location.longitude.toString()
            }
        })

        Snackbar.make(findViewById(R.id.startActivityConstraintLayout), "Getting data...", Snackbar.LENGTH_LONG).show()
        val queue= Volley.newRequestQueue(this)
        projectsDatabaseRequest(queue)

        mVisible = true
    }

    fun projectsDatabaseRequest(queue: RequestQueue){
        //getProjects from DB
        Snackbar.make(findViewById(R.id.startActivityConstraintLayout), "Loading Projects...", Snackbar.LENGTH_LONG).show()
        val projectsRequest = StringRequest(Request.Method.GET, GET_PROJECTS_URL,
                Response.Listener<String> { response ->
                    DataManager.getProjectsFromDB(response.toString())
                    locationsDatabaseRequest(queue)
                },
                Response.ErrorListener {
                    Snackbar.make(findViewById(R.id.startActivityConstraintLayout), "Projects - That didn't work!", Snackbar.LENGTH_LONG)
                })
        queue.add(projectsRequest)
    }

    fun locationsDatabaseRequest(queue: RequestQueue){
        //getLocations from DB
        Snackbar.make(findViewById(R.id.startActivityConstraintLayout), "Loading Locations...", Snackbar.LENGTH_LONG).show()
        val locationsRequest = StringRequest(Request.Method.GET, GET_LOCATIONS_URL,
                Response.Listener<String> { response ->
                    DataManager.getLocationsFromDB(response.toString())
                    timeLogsDatabaseRequest(queue)
                },
                Response.ErrorListener {
                    Snackbar.make(findViewById(R.id.startActivityConstraintLayout), "Locations - That didn't work!", Snackbar.LENGTH_LONG)
                })

        queue.add(locationsRequest)
    }

    fun timeLogsDatabaseRequest(queue: RequestQueue){
        //getTimeLogs from DB
        Snackbar.make(findViewById(R.id.startActivityConstraintLayout), "Loading TimeLogs...", Snackbar.LENGTH_LONG).show()
        val timeLogsRequest = StringRequest(Request.Method.GET, GET_TIME_LOGS_URL,
                Response.Listener<String> { response ->
                    DataManager.getTimeLogsFromDB(response.toString(), this)
                    buttonDeveloper.setOnClickListener { view ->
                        roleInTeam = "dev"
                        startActivity(Intent(this, DeveloperListAllTimeLogsActivity::class.java))
                    }

                    buttonTeamLeader.setOnClickListener { view ->
                        roleInTeam = "teamLead"
                        startActivity(Intent(this, TeamLeaderViewActivity::class.java))
                    }
                    Snackbar.make(findViewById(R.id.startActivityConstraintLayout), "You can start now!", Snackbar.LENGTH_LONG).show()
                },
                Response.ErrorListener {
                    Snackbar.make(findViewById(R.id.startActivityConstraintLayout), "TimeLogs - That didn't work!", Snackbar.LENGTH_LONG)
                })
        queue.add(timeLogsRequest)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        delayedHide(100)
    }

    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
        mVisible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private val UI_ANIMATION_DELAY = 300
    }
}
