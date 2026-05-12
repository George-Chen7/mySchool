package com.example.ex3.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SessionManager {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _forceLogoutEvent = MutableStateFlow<String?>(null)
    val forceLogoutEvent: StateFlow<String?> = _forceLogoutEvent.asStateFlow()

    fun login() {
        _isLoggedIn.value = true
        _forceLogoutEvent.value = null
    }

    fun forceLogout(reason: String) {
        _isLoggedIn.value = false
        _forceLogoutEvent.value = reason
    }

    fun clearForceLogoutEvent() {
        _forceLogoutEvent.value = null
    }
}
