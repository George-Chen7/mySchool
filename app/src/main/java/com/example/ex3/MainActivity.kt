package com.example.ex3

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ex3.data.prefs.UserPreferences
import com.example.ex3.session.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private val prefs by lazy { UserPreferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!SessionManager.isLoggedIn.value) {
            redirectToLogin()
            return
        }

        setContentView(R.layout.activity_main)

        loadUsername()
        bindLoginBarClicks()
        observeForceLogout()
    }

    private fun loadUsername() {
        lifecycleScope.launch {
            val username = prefs.usernameFlow.first().ifEmpty { "陈己润" }
            findViewById<TextView>(R.id.tv_username)?.text = username
        }
    }

    private fun bindLoginBarClicks() {
        findViewById<TextView>(R.id.tv_username)?.setOnClickListener {
            showProfileDialog()
        }
        findViewById<TextView>(R.id.tv_personal_center)?.setOnClickListener {
            showProfileDialog()
        }
        findViewById<TextView>(R.id.tv_register)?.setOnClickListener {
            Toast.makeText(this, "注册 — 功能待上线", Toast.LENGTH_SHORT).show()
        }
        findViewById<TextView>(R.id.tv_help)?.setOnClickListener {
            Toast.makeText(this, "说明 — 功能待上线", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showProfileDialog() {
        val username = runBlocking { prefs.usernameFlow.first().ifEmpty { "陈己润" } }

        AlertDialog.Builder(this)
            .setTitle("个人中心")
            .setMessage("当前用户: $username")
            .setPositiveButton("模拟其他设备登录") { _, _ ->
                sendBroadcast(Intent("com.example.ex3.ACTION_FORCE_OFFLINE"))
            }
            .setNegativeButton("退出登录") { _, _ ->
                logout()
            }
            .show()
    }

    private fun observeForceLogout() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                SessionManager.forceLogoutEvent.collect { reason ->
                    if (reason != null) {
                        showForceOfflineDialog(reason)
                    }
                }
            }
        }
    }

    private fun showForceOfflineDialog(reason: String) {
        AlertDialog.Builder(this)
            .setTitle("强制下线")
            .setMessage(reason)
            .setCancelable(false)
            .setPositiveButton("重新登录") { _, _ ->
                SessionManager.clearForceLogoutEvent()
                redirectToLogin()
            }
            .show()
    }

    private fun logout() {
        lifecycleScope.launch {
            prefs.logout()
            redirectToLogin()
        }
    }

    private fun redirectToLogin() {
        startActivity(Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }
}
