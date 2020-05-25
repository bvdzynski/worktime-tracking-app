package com.project.worktimetrackingapp

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class DataManagerTest {

    @Before
    fun setUp() {
        DataManager.timeLogs.clear()
        DataManager.initializeTimeLogs()
    }

    @Test
    fun addNote() {
        val course = DataManager.projects.get("android_async")!!
        val noteTitle = "This is a test note"
        val noteText = "This is the body of my test note"

        val index = DataManager.addTimeLog(course, noteTitle, noteText)
        val note = DataManager.timeLogs[index]
        assertEquals(course, note.project)
        assertEquals(noteTitle, note.title)
        assertEquals(noteText, note.description)
    }

    @Test
    fun findSimilarNotes() {
        val course = DataManager.projects.get("android_async")!!
        val noteTitle = "This is a test note"
        val noteText1 = "This is the body of my test note"
        val noteText2 = "This is the body of my second test note"

        val index1 = DataManager.addTimeLog(course, noteTitle, noteText1)
        val index2 = DataManager.addTimeLog(course, noteTitle, noteText2)

        val note1 = DataManager.findTimeLog(course, noteTitle, noteText1)
        val foundIndex1 = DataManager.timeLogs.indexOf(note1)
        assertEquals(index1, foundIndex1)

        val note2 = DataManager.findTimeLog(course, noteTitle, noteText2)
        val foundIndex2 = DataManager.timeLogs.indexOf(note2)
        assertEquals(index2, foundIndex2)

    }
}