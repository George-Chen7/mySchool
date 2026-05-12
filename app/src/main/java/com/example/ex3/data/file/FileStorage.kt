package com.example.ex3.data.file

import android.content.Context
import java.io.File

class FileStorage(private val context: Context) {

    private val notesFile: File
        get() = File(context.filesDir, "user_notes.txt")

    private val logFile: File
        get() = File(context.filesDir, "op_log.txt")

    fun saveNotes(content: String) {
        notesFile.writeText(content)
    }

    fun loadNotes(): String {
        return if (notesFile.exists()) notesFile.readText() else ""
    }

    fun appendLog(entry: String) {
        val timestamped = "${System.currentTimeMillis()}: $entry\n"
        logFile.appendText(timestamped)
    }

    fun loadLog(): String {
        return if (logFile.exists()) logFile.readText() else "No logs yet."
    }
}
