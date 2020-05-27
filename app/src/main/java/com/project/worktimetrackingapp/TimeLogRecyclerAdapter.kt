package com.project.worktimetrackingapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class TimeLogRecyclerAdapter(private val context: Context, private val timeLogs: List<TimeLogInfo>) :
        RecyclerView.Adapter<TimeLogRecyclerAdapter.ViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)
    private val tag = this::class.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_time_log_list, parent, false)
        return ViewHolder(itemView)
    }
    override fun getItemCount() = timeLogs.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val timeLog = timeLogs[position]
        holder.textTitle?.text = timeLog.title
        holder.textStatus?.text = timeLog.status
        holder.textProject?.text = timeLog.project?.name
        if(timeLog.status == "closed"){
            holder.textWorkTime?.text = " (" + timeLog.workTime + "h)"
            holder.textStatus?.setTextColor(Color.RED)
        }else{
            holder.textWorkTime?.text = ""
            holder.textStatus?.setTextColor(Color.GREEN)
        }

        holder.timeLogId = timeLog.timeLogId.toString()
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val textTitle = itemView?.findViewById<TextView?>(R.id.listTimeLogTitle)
        val textProject = itemView?.findViewById<TextView?>(R.id.listTimeLogProject)
        val textWorkTime = itemView?.findViewById<TextView?>(R.id.listTimeLogWorkTime)
        val textStatus = itemView?.findViewById<TextView?>(R.id.listTimeLogStatus)
        var timeLogId: String = ""
        init {
            itemView?.setOnClickListener {
                var intent = Intent(context, DeveloperShowTimeLogActivity::class.java)

                if(roleInTeam == "teamLead"){
                    intent = Intent(context, TeamLeaderShowTimeLogActivity::class.java)
                }

                intent.putExtra(LOG_ID, timeLogId)
                context.startActivity(intent)
            }
        }
    }
}