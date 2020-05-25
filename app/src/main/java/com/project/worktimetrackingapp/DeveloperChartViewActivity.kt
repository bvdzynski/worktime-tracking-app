package com.project.worktimetrackingapp

import android.content.Intent
import android.graphics.Color
import android.graphics.EmbossMaskFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.androidplot.pie.Segment
import com.androidplot.pie.SegmentFormatter
import kotlinx.android.synthetic.main.activity_developer_chart_view.*
import kotlinx.android.synthetic.main.content_developer_chart_view.*


class DeveloperChartViewActivity : AppCompatActivity() {
    var timeLogs: List<TimeLogInfo> = DataManager.timeLogs
    var projects: HashMap<String, ProjectInfo> = DataManager.getProjects("dev")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developer_chart_view)
        setSupportActionBar(toolbar)

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
        super.onBackPressed()
        val intent = Intent(this, DeveloperListAllTimeLogsActivity::class.java)
        this.startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.items, menu)
        return true
    }

}