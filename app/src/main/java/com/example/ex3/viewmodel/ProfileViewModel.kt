package com.example.ex3.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ex3.broadcast.ForceOfflineReceiver
import com.example.ex3.data.file.FileStorage
import com.example.ex3.data.prefs.UserPreferences
import com.example.ex3.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = UserPreferences(application)
    private val fileStorage = FileStorage(application)

    val username = prefs.usernameFlow
    val isDarkTheme = prefs.getThemePreference()

    private val _notesContent = MutableStateFlow("")
    val notesContent: StateFlow<String> = _notesContent.asStateFlow()

    private val _opLog = MutableStateFlow("")
    val opLog: StateFlow<String> = _opLog.asStateFlow()

    fun loadFileData() {
        _notesContent.value = fileStorage.loadNotes()
        _opLog.value = fileStorage.loadLog()
    }

    fun saveNotes(content: String) {
        fileStorage.saveNotes(content)
        fileStorage.appendLog("保存笔记")
        _notesContent.value = content
        _opLog.value = fileStorage.loadLog()
    }

    fun toggleTheme(darkMode: Boolean) {
        prefs.saveThemePreference(darkMode)
        fileStorage.appendLog("切换主题: dark=$darkMode")
        _opLog.value = fileStorage.loadLog()
    }

    fun simulateOtherDeviceLogin() {
        val intent = Intent(ForceOfflineReceiver.ACTION_FORCE_OFFLINE)
        intent.setPackage(getApplication<Application>().packageName)
        getApplication<Application>().sendBroadcast(intent)
    }

    fun logout() {
        viewModelScope.launch {
            prefs.logout()
            SessionManager.forceLogout("您已注销。")
        }
    }
}
