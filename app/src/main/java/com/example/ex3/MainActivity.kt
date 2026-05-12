package com.example.ex3

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindLoginBarClicks()
    }

    private fun bindLoginBarClicks() {
        findViewById<TextView>(R.id.tv_username)?.setOnClickListener {
            Toast.makeText(this, "用户: 陈己润 — 功能待上线", Toast.LENGTH_SHORT).show()
        }
        findViewById<TextView>(R.id.tv_personal_center)?.setOnClickListener {
            Toast.makeText(this, "个人中心 — 功能待上线", Toast.LENGTH_SHORT).show()
        }
        findViewById<TextView>(R.id.tv_register)?.setOnClickListener {
            Toast.makeText(this, "注册 — 功能待上线", Toast.LENGTH_SHORT).show()
        }
        findViewById<TextView>(R.id.tv_help)?.setOnClickListener {
            Toast.makeText(this, "说明 — 功能待上线", Toast.LENGTH_SHORT).show()
        }
    }
}
