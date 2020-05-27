package com.project.worktimetrackingapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.androidplot.pie.Segment
import com.androidplot.pie.SegmentFormatter
import kotlinx.android.synthetic.main.activity_developer_view.*
import kotlinx.android.synthetic.main.app_bar_dev_chart_view.*
import kotlinx.android.synthetic.main.content_developer_chart_view.*


class DeveloperChartViewActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var timeLogs: List<TimeLogInfo> = DataManager.timeLogs
    var projects: HashMap<String, ProjectInfo> = DataManager.getProjects("dev")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developer_chart_view)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        var colors: Array<String> = arrayOf("#99b433", "#1e7145", "#ff0097", "#7e3878", "#603cba", "#00aba9", "#ffc40d", "#da532c", "#b91d47", "#eff4ff")
        var colorNumber = 0

        developerChartViewChart.legend.isVisible = true

        var hoursInTotal = 0.0

        projects.forEach{
            if(colorNumber == 10){
                colorNumber = 0
            }
            var projectId = it.value.projectId
            var sumHoursForProject: Double = 0.0
            timeLogs.forEach{
                if(it.project?.projectId == projectId && it.status == "closed"){
                    sumHoursForProject += it.workTime?.toDouble()!!
                    hoursInTotal += it.workTime?.toDouble()!!
                }
            }
            if(sumHoursForProject > 0){
                developerChartViewChart.addSegment(Segment(it.value.name, sumHoursForProject), SegmentFormatter(Color.parseColor(colors.get(colorNumber))))
                colorNumber++
            }
        }

        developerChartViewHours.setText("Total time: " + hoursInTotal.toString()+"h")

        developerChartViewChart.backgroundPaint.color = Color.TRANSPARENT;
        developerChartViewChart.getBackgroundPaint().setColor(Color.TRANSPARENT);
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
            this.startActivity(Intent(this, StartActivity::class.java))
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.items, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_timeLogs -> {
                this.startActivity(Intent(this, DeveloperListAllTimeLogsActivity::class.java))
                return true
            }
            R.id.nav_projects -> {
                this.startActivity(Intent(this, DeveloperListAllTimeLogsActivity::class.java))
                return true
            }
            R.id.nav_charts -> {
                this.startActivity(Intent(this, DeveloperChartViewActivity::class.java))
                return true
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

}