package com.project.worktimetrackingapp


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_edit_time_log.*
import kotlinx.android.synthetic.main.content_edit_time_log.*
import java.io.ByteArrayOutputStream


class EditTimeLogActivity : AppCompatActivity() {
    private var timeLogId: String = ""
    var timeLogs: List<TimeLogInfo> = DataManager.timeLogs
    var projects: HashMap<String, ProjectInfo> = DataManager.getProjects("dev")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_time_log)
        setSupportActionBar(toolbar)

        if (intent.extras != null) {
            timeLogId = intent.extras.getString(LOG_ID)
        }

        timeLogEditSaveButton.setOnClickListener { view ->
            saveTimeLog()
            val intent = Intent(this, DeveloperShowTimeLogActivity::class.java)
            intent.putExtra(LOG_ID, timeLogId)
            this.startActivity(intent)
        }

        timeLogEditImagePickButton.setOnClickListener {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, 1001);
                }
                else{
                    //permission already granted
                    pickImageFromGallery();
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }

        val adapterProjects = ArrayAdapter<ProjectInfo>(this,
                android.R.layout.simple_spinner_item,
                projects.values.toList())
        adapterProjects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        timeLogEditProjectSpinner.adapter = adapterProjects

        val adapterLocations = ArrayAdapter<LocationInfo>(this,
                android.R.layout.simple_spinner_item,
                DataManager.locations.values.toList())
        adapterLocations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        timeLogEditLocationSpinner.adapter = adapterLocations

        if(timeLogId == "NEW"){
            var createdTimeLog :TimeLogInfo = createNewTimeLog()
            timeLogId = createdTimeLog.timeLogId

            val queue= Volley.newRequestQueue(this)
            val addTimeLogRequest: StringRequest = object : StringRequest(Method.POST, ADD_TIME_LOG_URL,
                    Response.Listener { response -> // response
                        Log.i("ok, created in app new timeLogId: ", timeLogId)
                        Log.i("response from api: ", response.toString())
                    },
                    Response.ErrorListener {
                        Log.i("creating timeLog failed", "error")
                        Log.i("response from api: ", it.toString())
                    }
            ) {
                override fun getParams(): Map<String, String> {
                    var params: HashMap<String, String> = HashMap()
                    params["timeLogId"] = createdTimeLog.timeLogId
                    params["status"] = createdTimeLog.status
                    params["coords"] = createdTimeLog.coords.toString()
                    params["workTimeStart"] = createdTimeLog.workTimeStart
                    params["workTime"] = "0"
                    return params
                }
            }

            queue.add(addTimeLogRequest)
        }

        editTimeLog(timeLogs, projects)
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1000)
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1000){

            timeLogEditImageView.setImageURI(data?.data)

            var chosenTimeLog = TimeLogInfo()

            timeLogs.forEach{
                if(it.timeLogId == timeLogId){
                    chosenTimeLog = it
                }
            }

            chosenTimeLog.imageURI = data?.data!!
        }
    }

    private fun createNewTimeLog(): TimeLogInfo {
        return DataManager.addNewTimeLog(currentCoordinates)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, DeveloperShowTimeLogActivity::class.java)
        intent.putExtra(LOG_ID, timeLogId)
        this.startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(LOG_ID, timeLogId)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_edit_time_log, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_delete -> {
                deleteTimeLog()
                val intent = Intent(this, DeveloperListAllTimeLogsActivity::class.java)
                this.startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun editTimeLog(timeLogs: List<TimeLogInfo>, projects: HashMap<String, ProjectInfo>) {

        var chosenTimeLog = TimeLogInfo()

        timeLogs.forEach{
            if(it.timeLogId == timeLogId){
                chosenTimeLog = it
            }
        }

        timeLogEditTitleText.setText(chosenTimeLog.title)
        timeLogEditDescriptionText.setText(chosenTimeLog.description)
        if(chosenTimeLog.imageURI == Uri.EMPTY){
            timeLogEditImageView.setImageResource(R.drawable.ic_block_black_24dp)
            timeLogEditImageView.setTag(Uri.EMPTY.toString())
        } else{
            timeLogEditImageView.setImageURI(chosenTimeLog.imageURI)
            timeLogEditImageView.setTag(chosenTimeLog.imageURI)
        }

        timeLogEditProjectSpinner.setSelection(projects.values.indexOf(chosenTimeLog.project))

        val locationPosition = DataManager.locations.values.indexOf(chosenTimeLog.location)
        timeLogEditProjectSpinner.setSelection(locationPosition)
    }

    private fun saveTimeLog() {

        var chosenTimeLog = TimeLogInfo()
        timeLogs.forEach{
            if(it.timeLogId == timeLogId){
                chosenTimeLog = it
            }
        }

        chosenTimeLog.title = timeLogEditTitleText.text.toString()
        chosenTimeLog.project = timeLogEditProjectSpinner.selectedItem as ProjectInfo
        chosenTimeLog.description = timeLogEditDescriptionText.text.toString()
        chosenTimeLog.location = timeLogEditLocationSpinner.selectedItem as LocationInfo
        if(timeLogEditImageView.getTag().toString() != Uri.EMPTY.toString()){
            chosenTimeLog.imageURI = Uri.parse(timeLogEditImageView.getTag().toString())
        }
        var timeLogPhoto = MediaStore.Images.Media.getBitmap(this.contentResolver, chosenTimeLog.imageURI)
        val queue= Volley.newRequestQueue(this)
        val editTimeLogRequest: StringRequest = object : StringRequest(Method.POST, EDIT_TIME_LOG_URL,
                Response.Listener { response -> // response
                    Log.i("edited timelog: ", chosenTimeLog.timeLogId)
                    Log.i("response from api: ", response.toString())
                },
                Response.ErrorListener {
                    Log.i("editing timeLog failed", "error")
                    Log.i("response from api: ", it.toString())
                }
        ) {
            override fun getParams(): Map<String, String> {
                var params: HashMap<String, String> = HashMap()
                params["timeLogId"] = chosenTimeLog.timeLogId
                params["title"] = chosenTimeLog.title.toString()
                params["projectId"] = chosenTimeLog.project!!.projectId
                params["description"] = chosenTimeLog.description.toString()
                params["locationId"] = chosenTimeLog.location!!.locationId
                if(chosenTimeLog.imageURI != Uri.EMPTY){
                    var byteArrayOutputStream = ByteArrayOutputStream()
                    timeLogPhoto.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream)
                    var encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
                    params["encodedImage"] = encodedImage
                }
                return params
            }
        }

        queue.add(editTimeLogRequest)
    }

    private fun deleteTimeLog() {
        var chosenTimeLog = TimeLogInfo()

        timeLogs.forEach{
            if(it.timeLogId == timeLogId){
                chosenTimeLog = it
            }
        }

        DataManager.timeLogs.remove(chosenTimeLog)

        val queue= Volley.newRequestQueue(this)
        val deleteTimeLogRequest: StringRequest = object : StringRequest(Method.POST, DELETE_TIME_LOG_URL,
                Response.Listener { response ->
                    Log.i("deleted timelog: ", chosenTimeLog.timeLogId)
                    Log.i("response from api: ", response.toString())
                },
                Response.ErrorListener {
                    Log.i("deleting timeLog failed", "error")
                    Log.i("response from api: ", it.toString())
                }
        ) {
            override fun getParams(): Map<String, String> {
                var params: HashMap<String, String> = HashMap()
                params["timeLogId"] = chosenTimeLog.timeLogId
                return params
            }
        }

        queue.add(deleteTimeLogRequest)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when(requestCode){
            1001 -> {
                if (grantResults.size >0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

}