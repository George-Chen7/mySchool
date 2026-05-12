package com.example.ex3.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ex3.data.prefs.UserPreferences
import com.example.ex3.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = UserPreferences(application)

    val savedUsername: StateFlow<String> = prefs.usernameFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")
    val rememberPassword: StateFlow<Boolean> = prefs.rememberPasswordFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val savedPassword: StateFlow<String> = prefs.passwordFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _rememberPwd = MutableStateFlow(false)
    val rememberPwd: StateFlow<Boolean> = _rememberPwd.asStateFlow()

    fun onUsernameChange(value: String) { _username.value = value }
    fun onPasswordChange(value: String) { _password.value = value }
    fun onRememberChange(value: Boolean) { _rememberPwd.value = value }

    fun login(onSuccess: () -> Unit) {
        if (_username.value.isBlank() || _password.value.isBlank()) return
        viewModelScope.launch {
            prefs.saveLoginInfo(_username.value, _password.value, _rememberPwd.value)
            SessionManager.login()
            onSuccess()
        }
    }
}
