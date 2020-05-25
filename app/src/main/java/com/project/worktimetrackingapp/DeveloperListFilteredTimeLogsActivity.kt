package com.project.worktimetrackingapp

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_developer_view.*
import kotlinx.android.synthetic.main.app_bar_dev_time_logs.*
import kotlinx.android.synthetic.main.content_time_logs.*

class DeveloperListFilteredTimeLogsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val timeLogLayoutManager by lazy {
        LinearLayoutManager(this)
    }
    private val timeLogRecyclerAdapter by lazy {
        TimeLogRecyclerAdapter(this, DataManager.getTimeLogsByProjectId(intent.getStringExtra(PROJECT_ID)).reversed())
    }

    private val projectLayoutManager by lazy {
        LinearLayoutManager(this)
    }
    private val projectRecyclerAdapter by lazy {
        ProjectRecyclerAdapter(this, DataManager.getProjects(roleInTeam).values.toList())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developer_view)
        setSupportActionBar(toolbar)

        addTimeLogButton.setOnClickListener { view ->
            val intent = Intent(this, EditTimeLogActivity::class.java)
            intent.putExtra(LOG_ID, "NEW")
            this.startActivity(intent)
        }

        displayTimeLogs()

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun displayTimeLogs() {
        listItems.layoutManager = timeLogLayoutManager
        listItems.adapter = timeLogRecyclerAdapter

        nav_view.menu.findItem(R.id.nav_timeLogs).isChecked = true
    }

    private fun displayProjects() {
        listItems.layoutManager = projectLayoutManager
        listItems.adapter = projectRecyclerAdapter

        nav_view.menu.findItem(R.id.nav_projects).isChecked = true
    }

    override fun onResume() {
        super.onResume()
        listItems.adapter?.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
            this.startActivity(Intent(this, StartActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_timeLogs -> {
                displayTimeLogs()
            }
            R.id.nav_projects -> {
                displayProjects()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun handleSelection(message: String) {
        Snackbar.make(listItems, message, Snackbar.LENGTH_LONG).show()
    }
}
