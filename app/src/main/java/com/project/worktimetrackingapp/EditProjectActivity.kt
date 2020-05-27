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

import kotlinx.android.synthetic.main.activity_edit_project.*
import kotlinx.android.synthetic.main.content_edit_project.*

class EditProjectActivity : AppCompatActivity() {
    private var projectId: String = ""
    var projects: HashMap<String, ProjectInfo> = DataManager.getProjects(roleInTeam)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_project)
        setSupportActionBar(toolbar)

        if (intent.extras != null) {
            projectId = intent.extras.getString(PROJECT_ID)
        }

        projectEditSaveButton.setOnClickListener { view ->
            saveProject()
            this.startActivity(Intent(this, TeamLeaderViewActivity::class.java))
        }

        if(projectId == "NEW"){
            var createdProject :ProjectInfo = createNewProject()
            projectId = createdProject.projectId

            val queue= Volley.newRequestQueue(this)
            val addProjectRequest: StringRequest = object : StringRequest(Method.POST, ADD_PROJECT_URL,
                    Response.Listener { response -> // response
                        Log.i("created in app new projectId: ", createdProject.projectId)
                        Log.i("response from api: ", response.toString())
                    },
                    Response.ErrorListener {
                        Log.i("creating project failed", "error")
                        Log.i("response from api: ", it.toString())
                    }
            ) {
                override fun getParams(): Map<String, String> {
                    var params: HashMap<String, String> = HashMap()
                    params["projectId"] = createdProject.projectId
                    return params
                }
            }

            queue.add(addProjectRequest)
        }

        editProject(projects)
    }

    private fun createNewProject(): ProjectInfo {
        return DataManager.addNewProject()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(PROJECT_ID, projectId)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_edit_project, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_delete -> {
                deleteProject()
                val intent = Intent(this, TeamLeaderViewActivity::class.java)
                this.startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.startActivity(Intent(this, TeamLeaderViewActivity::class.java))
    }

    private fun editProject(projects: HashMap<String, ProjectInfo>) {

        var chosenProject = ProjectInfo()

        projects.forEach{
            if(it.value.projectId == projectId){
                chosenProject = it.value
            }
        }

        projectEditNameText.setText(chosenProject.name)
        projectEditAssignedCheckbox.isChecked = chosenProject.assignedToDeveloper
    }

    private fun showMessage(message: String) {
        Snackbar.make(projectEditNameText, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onPause() {
        super.onPause()
    }

    private fun saveProject() {

        var chosenProject = ProjectInfo()

        projects.forEach{
            if(it.value.projectId == projectId){
                chosenProject = it.value
            }
        }

        chosenProject.name = projectEditNameText.text.toString()
        chosenProject.assignedToDeveloper = projectEditAssignedCheckbox.isChecked

        val queue= Volley.newRequestQueue(this)
        val editProjectRequest: StringRequest = object : StringRequest(Method.POST, EDIT_PROJECT_URL,
                Response.Listener { response -> // response
                    Log.i("edited project: ", chosenProject.projectId)
                    Log.i("response from api: ", response.toString())
                },
                Response.ErrorListener {
                    Log.i("editing project failed", "error")
                    Log.i("response from api: ", it.toString())
                }
        ) {
            override fun getParams(): Map<String, String> {
                var params: HashMap<String, String> = HashMap()
                params["projectId"] = chosenProject.projectId
                params["name"] = chosenProject.name
                params["assignedToDeveloper"] = if (chosenProject.assignedToDeveloper) "1" else "0"
                return params
            }
        }

        queue.add(editProjectRequest)
    }

    private fun deleteProject() {
        projects.remove(projectId)

        val queue= Volley.newRequestQueue(this)
        val deleteLocationRequest: StringRequest = object : StringRequest(Method.POST, DELETE_PROJECT_URL,
                Response.Listener { response ->
                    Log.i("deleted project: ", projectId)
                    Log.i("response from api: ", response.toString())
                },
                Response.ErrorListener {
                    Log.i("deleting project failed", "error")
                    Log.i("response from api: ", it.toString())
                }
        ) {
            override fun getParams(): Map<String, String> {
                var params: HashMap<String, String> = HashMap()
                params["projectId"] = projectId
                return params
            }
        }

        queue.add(deleteLocationRequest)
    }
}