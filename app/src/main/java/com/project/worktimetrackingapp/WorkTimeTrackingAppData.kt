package com.project.worktimetrackingapp

import android.net.Uri
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

data class TimeLogInfo(
        val timeLogId: String = UUID.randomUUID().toString(),
        var title: String? = null,
        var project: ProjectInfo? = null,
        var status: String = "open",
        var description: String? = null,
        var location: LocationInfo? = null,
        var coords: String? = null,
        var workTimeStart: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")),
        var workTimeStop: String? = null,
        var workTime: String? = null,
//                "0",
        var imageURI: Uri = Uri.EMPTY
)

data class ProjectInfo(
        val projectId: String = UUID.randomUUID().toString(),
        var name: String = "",
        var assignedToDeveloper: Boolean = true
) {
    @Override
    override fun toString(): String {
        return name
    }
}

data class LocationInfo(
        val locationId: String = UUID.randomUUID().toString(),
        var name: String = "",
        var coordinates: String = ""
){
    @Override
    override fun toString(): String {
        return name
    }
}

