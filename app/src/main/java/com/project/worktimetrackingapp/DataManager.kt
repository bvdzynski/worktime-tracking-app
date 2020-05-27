package com.project.worktimetrackingapp

import android.content.Context
import android.net.Uri
import android.util.Base64
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import java.io.File
import java.io.FileOutputStream
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object DataManager {
    var timeLogs = ArrayList<TimeLogInfo>()
    val projects = HashMap<String, ProjectInfo>()
    val locations = HashMap<String, LocationInfo>()

    fun addNewTimeLog(currentCoordinates: String): TimeLogInfo {
        val timeLog = TimeLogInfo(coords = currentCoordinates)
        timeLogs.add(timeLog)
        return timeLog
    }

    fun findTimeLog(project: ProjectInfo, timeLogTitle: String, timeLogText: String): TimeLogInfo? {
        for (timeLog in timeLogs)
            if (project == timeLog.project &&
                    timeLogTitle == timeLog.title &&
                    timeLogText == timeLog.description)
                return timeLog

        return null
    }

    fun getTimeLogs(roleInTeam: String): List<TimeLogInfo>{
        var toReturn: List<TimeLogInfo> = ArrayList<TimeLogInfo>()
        when(roleInTeam){
            "dev" -> {
                toReturn = timeLogs.filter { it.project?.assignedToDeveloper == true }
            }
            "teamLead" -> {
                toReturn = timeLogs
            }
        }
        return toReturn
    }

    fun getTimeLogsByProjectId(projectFilter: String? = null): List<TimeLogInfo>{
        var toReturn: List<TimeLogInfo> = ArrayList<TimeLogInfo>()

        toReturn = timeLogs.filter { it.project?.assignedToDeveloper == true }
        if(projectFilter != null){
            toReturn = toReturn.filter { it.project?.projectId == projectFilter }
        }

        return toReturn
    }

    fun addNewProject(): ProjectInfo {
        val project = ProjectInfo()
        projects.set(project.projectId, project)
        return project
    }

    fun getProjects(roleInTeam: String): HashMap<String, ProjectInfo>{
        var toReturn = HashMap<String, ProjectInfo>()

        when(roleInTeam){
            "dev" -> {
                for ((key, value) in projects) {
                    if(value.assignedToDeveloper!!){
                        toReturn[key] = value
                    }
                }
            }
            "teamLead" -> {
                toReturn = projects
            }
        }

        return toReturn
    }

    fun addNewLocation(): LocationInfo {
        val location = LocationInfo()
        locations.set(location.locationId, location)
        return location
    }

    fun getLocations(roleInTeam: String): HashMap<String, LocationInfo>{
        var toReturn = HashMap<String, LocationInfo>()

        when(roleInTeam){
            "teamLead" -> {
                toReturn = locations
            }
        }

        return toReturn
    }

    fun getTimeLogsFromDB(response: String, context: Context){
        val jsonString = StringBuilder(response)
        val parser = Parser.default()
        val result = parser.parse(jsonString) as JsonArray<JsonObject>

        result?.forEach{
            var timeLogId = it.string("timeLogId").toString()
            var flag = true
            timeLogs.forEach{
                if(it.timeLogId == timeLogId){
                    flag = false
                }
            }
            if(flag){
                //getImage
                var imageURI = Uri.EMPTY
                if(it.string("encodedImage") != null){
                    val decodedImage = Base64.decode(it.string("encodedImage"), Base64.DEFAULT)
                    //create a file to write decodedImage data
                    var imageFile = File(context.getCacheDir(), it.string("timeLogId") + "_image");
                    imageFile.createNewFile();
                    imageURI = Uri.parse(imageFile.absolutePath)
                    //write the bytes in file
                    var fos = FileOutputStream(imageFile);
                    fos.write(decodedImage);
                    fos.flush();
                    fos.close();
                }

                //save timeLog
                timeLogs.add(TimeLogInfo(
                        timeLogId = it.string("timeLogId").toString(),
                        title = it.string("title"),
                        project = projects.get(it.string("projectId")),
                        status = it.string("status").toString(),
                        description = it.string("description"),
                        location = locations.get(it.string("locationId")),
                        coords = it.string("coords"),
                        workTimeStart = it.string("workTimeStart").toString(),
                        workTimeStop = it.string("workTimeStop"),
                        workTime = it.string("workTime"),
                        imageURI = imageURI
                ))
            }
        }
    }

    fun getProjectsFromDB(response: String){
        val jsonString = StringBuilder(response)
        val parser = Parser.default()
        val result = parser.parse(jsonString) as JsonArray<JsonObject>
        result.forEach{
            projects.set(
                    it.string("projectId").toString(),
                    ProjectInfo(
                            projectId = it.string("projectId").toString(),
                            name = it.string("name").toString(),
                            assignedToDeveloper = it.string("assignedToDeveloper") == "1"
                    )
            )
        }
    }

    fun getLocationsFromDB(response: String){
        val jsonString = StringBuilder(response)
        val parser = Parser.default()
        val result = parser.parse(jsonString) as JsonArray<JsonObject>
        result.forEach{
            locations.set(
                    it.string("locationId").toString(),
                    LocationInfo(
                            locationId = it.string("locationId").toString(),
                            name = it.string("name").toString(),
                            coordinates = it.string("coordinates").toString()
                    )
            )
        }
    }
}
