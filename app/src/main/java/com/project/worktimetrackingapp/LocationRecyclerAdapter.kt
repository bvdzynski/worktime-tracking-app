package com.project.worktimetrackingapp

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class LocationRecyclerAdapter(private val context: Context, private val locations: List<LocationInfo>) :
        RecyclerView.Adapter<LocationRecyclerAdapter.ViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_location_list, parent, false)
        return ViewHolder(itemView)
    }
    override fun getItemCount() = locations.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = locations[position]
        holder.textLocation?.text = location.name
        holder.textCoords?.text = "(" + location.coordinates + ")"
        holder.locationPosition = position
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val textLocation = itemView?.findViewById<TextView?>(R.id.listLocationTitle)
        val textCoords = itemView?.findViewById<TextView?>(R.id.listLocationCoords)
        var locationPosition = 0
        init {
            itemView?.setOnClickListener {
                if(roleInTeam == "dev"){
                    val intent = Intent(context, TeamLeaderViewActivity::class.java)
                    intent.putExtra(LOCATION_ID, locations[locationPosition].locationId)
                    context.startActivity(intent)
                } else{
                    val intent = Intent(context, EditLocationActivity::class.java)
                    intent.putExtra(LOCATION_ID, locations[locationPosition].locationId)
                    context.startActivity(intent)
                }
            }
        }
    }
}
