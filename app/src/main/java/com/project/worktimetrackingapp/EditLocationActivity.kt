package com.project.worktimetrackingapp

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import kotlinx.android.synthetic.main.activity_edit_location.*
import kotlinx.android.synthetic.main.content_edit_location.*

class EditLocationActivity : AppCompatActivity() {
    private var locationId: String = ""
    var locations: HashMap<String, LocationInfo> = DataManager.getLocations(roleInTeam)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_location)
        setSupportActionBar(toolbar)

        if (intent.extras != null) {
            locationId = intent.extras.getString(LOCATION_ID)
        }

        locationEditSaveButton.setOnClickListener { view ->
            saveLocation()
            this.startActivity(Intent(this, TeamLeaderViewActivity::class.java))
        }

        if(locationId == "NEW"){
            var createdLocation :LocationInfo = createNewLocation()
            locationId = createdLocation.locationId

            val queue= Volley.newRequestQueue(this)
            val addLocationRequest: StringRequest = object : StringRequest(Method.POST, ADD_LOCATION_URL,
                    Response.Listener { response -> // response
                        Log.i("created in app new locationId: ", createdLocation.locationId)
                        Log.i("response from api: ", response.toString())
                    },
                    Response.ErrorListener {
                        Log.i("creating location failed", "error")
                        Log.i("response from api: ", it.toString())
                    }
            ) {
                override fun getParams(): Map<String, String> {
                    var params: HashMap<String, String> = HashMap()
                    params["locationId"] = createdLocation.locationId
                    return params
                }
            }

            queue.add(addLocationRequest)
        }

        editLocation(locations)
    }

    private fun createNewLocation(): LocationInfo {
        return DataManager.addNewLocation()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(LOCATION_ID, locationId)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_edit_location, menu)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.startActivity(Intent(this, TeamLeaderViewActivity::class.java))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_delete -> {
                deleteLocation()
                val intent = Intent(this, TeamLeaderViewActivity::class.java)
                this.startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun editLocation(locations: HashMap<String, LocationInfo>) {

        var chosenLocation = LocationInfo()

        locations.forEach{
            if(it.value.locationId == locationId){
                chosenLocation = it.value
            }
        }

        locationEditNameText.setText(chosenLocation.name)
        locationEditCoordinatesText.setText(chosenLocation.coordinates)
    }

    private fun showMessage(message: String) {
        Snackbar.make(locationEditNameText, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onPause() {
        super.onPause()
    }

    private fun saveLocation() {

        var chosenLocation = LocationInfo()

        locations.forEach{
            if(it.value.locationId == locationId){
                chosenLocation = it.value
            }
        }

        chosenLocation.name = locationEditNameText.text.toString()
        chosenLocation.coordinates = locationEditCoordinatesText.text.toString()

        val queue= Volley.newRequestQueue(this)
        val editLocationRequest: StringRequest = object : StringRequest(Method.POST, EDIT_LOCATION_URL,
                Response.Listener { response -> // response
                    Log.i("edited location: ", chosenLocation.locationId)
                    Log.i("response from api: ", response.toString())
                },
                Response.ErrorListener {
                    Log.i("editing location failed", "error")
                    Log.i("response from api: ", it.toString())
                }
        ) {
            override fun getParams(): Map<String, String> {
                var params: HashMap<String, String> = HashMap()
                params["locationId"] = chosenLocation.locationId
                params["name"] = chosenLocation.name
                params["coordinates"] = chosenLocation.coordinates
                return params
            }
        }

        queue.add(editLocationRequest)
    }

    private fun deleteLocation() {
        locations.remove(locationId)

        val queue= Volley.newRequestQueue(this)
        val deleteLocationRequest: StringRequest = object : StringRequest(Method.POST, DELETE_LOCATION_URL,
                Response.Listener { response ->
                    Log.i("deleted location: ", locationId)
                    Log.i("response from api: ", response.toString())
                },
                Response.ErrorListener {
                    Log.i("deleting location failed", "error")
                    Log.i("response from api: ", it.toString())
                }
        ) {
            override fun getParams(): Map<String, String> {
                var params: HashMap<String, String> = HashMap()
                params["locationId"] = locationId
                return params
            }
        }

        queue.add(deleteLocationRequest)
    }
}