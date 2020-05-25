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
import android.support.test.espresso.contrib.DrawerActions
import android.support.test.espresso.contrib.NavigationViewActions
import android.support.test.espresso.contrib.RecyclerViewActions

@RunWith(AndroidJUnit4::class)
class NavigationTest {
    @Rule @JvmField
    val itemsActivity = ActivityTestRule(DeveloperListAllTimeLogsActivity::class.java)

    @Test
    fun selectNoteAfterNavigationDrawerChange() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_projects))

        val coursePosition = 0
        onView(withId(R.id.listItems)).perform(
                RecyclerViewActions.actionOnItemAtPosition<ProjectRecyclerAdapter.ViewHolder>(coursePosition, click())
        )

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_timeLogs))

        val notePosition = 0
        onView(withId(R.id.listItems)).perform(
                RecyclerViewActions.actionOnItemAtPosition<TimeLogRecyclerAdapter.ViewHolder>(notePosition, click())
        )

        val note = DataManager.timeLogs[notePosition]
        onView(withId(R.id.spinnerProjects)).check(matches(withSpinnerText(containsString(note.project?.name))))
        onView(withId(R.id.textTimeLogTitle)).check(matches(withText(containsString(note.title))))
        onView(withId(R.id.textTimeLogDescription)).check(matches(withText(containsString(note.description))))


    }
}


