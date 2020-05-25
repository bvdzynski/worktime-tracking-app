package com.project.worktimetrackingapp

import android.support.test.runner.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.*
import android.support.test.espresso.action.ViewActions.*
import android.support.test.rule.ActivityTestRule
import org.junit.Rule
import android.support.test.espresso.assertion.ViewAssertions.*

@RunWith(AndroidJUnit4::class)
class NextThroughNotesTest {
    @Rule @JvmField
    val noteListActivity = ActivityTestRule(TimeLogListActivity::class.java)

    @Test
    fun nextThroughNotes() {
        onData(allOf(instanceOf(TimeLogInfo::class.java), equalTo(DataManager.timeLogs[0]))).perform(click())

        for(index in 0..DataManager.timeLogs.lastIndex) {
            val note = DataManager.timeLogs[index]

            onView(withId(R.id.spinnerProjects)).check(
                    matches(withSpinnerText(note.project?.name)))
            onView(withId(R.id.textTimeLogTitle)).check(
                    matches(withText(note.title)))
            onView(withId(R.id.textTimeLogDescription)).check(
                    matches(withText(note.description)))

            if(index != DataManager.timeLogs.lastIndex)
                onView(allOf(withId(R.id.action_next), isEnabled())).perform(click())

        }

        onView(withId(R.id.action_next)).check(matches(isEnabled()))
    }
}






