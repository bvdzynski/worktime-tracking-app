package com.project.worktimetrackingapp

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView

class ProjectRecyclerAdapter(private val context: Context, private val projects: List<ProjectInfo>) :
        RecyclerView.Adapter<ProjectRecyclerAdapter.ViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View?
        if(roleInTeam == "dev"){
            itemView = layoutInflater.inflate(R.layout.item_project_list_dev, parent, false)
        } else{
            itemView = layoutInflater.inflate(R.layout.item_project_list_team_leader, parent, false)
        }

        return ViewHolder(itemView)
    }
    override fun getItemCount() = projects.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val project = projects[position]
        holder.textProjectName?.text = project.name
        holder.valProjectAssigned?.isChecked = project.assignedToDeveloper
        holder.projectPosition = position
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val textProjectName = itemView?.findViewById<TextView?>(R.id.listProjectTitle)
        val valProjectAssigned = itemView?.findViewById<CheckBox?>(R.id.listProjectAssigned)
        var projectPosition = 0
        init {
            itemView?.setOnClickListener {
                if(roleInTeam == "dev"){
                    val intent = Intent(context, DeveloperListFilteredTimeLogsActivity::class.java)
                    intent.putExtra(PROJECT_ID, projects[projectPosition].projectId)
                    context.startActivity(intent)
                } else{
                    val intent = Intent(context, EditProjectActivity::class.java)
                    intent.putExtra(PROJECT_ID, projects[projectPosition].projectId)
                    context.startActivity(intent)
                }
            }
        }
    }
}