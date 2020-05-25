package com.project.worktimetrackingapp

//api consts
const val ROOT_URL = "http://192.168.1.23/worktime-tracking-app-backend/v1/"

const val ADD_TIME_LOG_URL = ROOT_URL + "timelog/addTimeLog.php"
const val EDIT_TIME_LOG_URL = ROOT_URL + "timelog/editTimeLog.php"
const val CLOSE_TIME_LOG_URL = ROOT_URL + "timelog/closeTimeLog.php"
const val DELETE_TIME_LOG_URL = ROOT_URL + "timelog/deleteTimeLog.php"
const val GET_TIME_LOGS_URL = ROOT_URL + "timelog/getTimeLogs.php"

const val ADD_PROJECT_URL = ROOT_URL + "project/addProject.php"
const val EDIT_PROJECT_URL = ROOT_URL + "project/editProject.php"
const val DELETE_PROJECT_URL = ROOT_URL + "project/deleteProject.php"
const val GET_PROJECTS_URL = ROOT_URL + "project/getProjects.php"

const val ADD_LOCATION_URL = ROOT_URL + "location/addLocation.php"
const val EDIT_LOCATION_URL = ROOT_URL + "location/editLocation.php"
const val DELETE_LOCATION_URL = ROOT_URL + "location/deleteLocation.php"
const val GET_LOCATIONS_URL = ROOT_URL + "location/getLocations.php"

//data consts
const val LOG_ID = "LOG_ID"
const val PROJECT_ID = "PROJECT_ID"
const val LOCATION_ID = "LOCATION_ID"
const val POSITION_NOT_SET = -1

//global variables
var roleInTeam : String = ""
var currentCoordinates : String = ""