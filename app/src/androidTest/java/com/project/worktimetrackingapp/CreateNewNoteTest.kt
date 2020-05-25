package com.project.worktimetrackingapp

import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.*
import android.support.test.espresso.action.ViewActions.*
import android.support.test.rule.ActivityTestRule
import org.junit.Rule
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard

@RunWith(AndroidJUnit4::class)
class CreateNewNoteTest{
    @Rule @JvmField
    val noteListActivity = ActivityTestRule(TimeLogListActivity::class.java)

    @Test
    fun createNewNote() {
        val course = DataManager.projects["android_async"]
        val noteTitle = "Test note title"
        val noteText = "This is the body of our test note"

        onView(withId(R.id.addTimeLogButton)).perform(click())

        onView(withId(R.id.spinnerProjects)).perform(click())
        onData(allOf(instanceOf(ProjectInfo::class.java), equalTo(course))).perform(click())


        onView(withId(R.id.textTimeLogTitle)).perform(typeText(noteTitle))
        onView(withId(R.id.textTimeLogDescription)).perform(typeText(noteText), closeSoftKeyboard())

        pressBack()

        val newNote = DataManager.timeLogs.last()
        assertEquals(course, newNote.project)
        assertEquals(noteTitle, newNote.title)
        assertEquals(noteText, newNote.description)


    }
}