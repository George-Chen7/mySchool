package com.example.ex3

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ex3.data.prefs.UserPreferences
import com.example.ex3.session.SessionManager
import com.example.ex3.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (wasLoggedIn()) {
            SessionManager.login()
            startMainActivity()
            return
        }

        setContentView(R.layout.activity_login)

        val etUsername = findViewById<EditText>(R.id.et_username)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val cbRemember = findViewById<CheckBox>(R.id.cb_remember_pwd)
        val btnLogin = findViewById<TextView>(R.id.btn_login)
        val tvError = findViewById<TextView>(R.id.tv_login_error)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.savedUsername.collect { username ->
                        if (username.isNotEmpty()) {
                            etUsername.setText(username)
                            etUsername.setSelection(username.length)
                        }
                    }
                }
                launch {
                    viewModel.rememberPassword.collect { remember ->
                        cbRemember.isChecked = remember
                    }
                }
                launch {
                    viewModel.savedPassword.collect { pwd ->
                        if (pwd.isNotEmpty()) {
                            etPassword.setText(pwd)
                            etPassword.setSelection(pwd.length)
                        }
                    }
                }
            }
        }

        cbRemember.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onRememberChange(isChecked)
        }

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty()) {
                tvError.text = getString(R.string.login_error_empty_username)
                tvError.visibility = TextView.VISIBLE
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                tvError.text = getString(R.string.login_error_empty_password)
                tvError.visibility = TextView.VISIBLE
                return@setOnClickListener
            }

            tvError.visibility = TextView.GONE
            viewModel.onUsernameChange(username)
            viewModel.onPasswordChange(password)

            viewModel.login {
                Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show()
                startMainActivity()
            }
        }
    }

    private fun wasLoggedIn(): Boolean = runBlocking {
        UserPreferences(this@LoginActivity).isLoggedInFlow.first()
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
