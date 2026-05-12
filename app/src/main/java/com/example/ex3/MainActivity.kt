package com.example.ex3

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupSpinner()
        styleNewsItems()
    }

    private fun setupSpinner() {
        val spinner = findViewById<Spinner>(R.id.spinner_user_type)
        val options = arrayOf("阅览用户", "教职工", "学生", "访客")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun styleNewsItems() {
        // Color the category labels red in news items
        val newsContainer = findViewById<LinearLayout>(R.id.news_container)
        val redColor = ContextCompat.getColor(this, R.color.szu_accent)

        for (i in 0 until newsContainer.childCount) {
            val child = newsContainer.getChildAt(i)
            if (child is TextView) {
                val text = child.text.toString()
                val bracketEnd = text.indexOf(']')
                if (bracketEnd > 0) {
                    val spannable = SpannableString(text)
                    spannable.setSpan(
                        ForegroundColorSpan(redColor),
                        0, bracketEnd + 1,
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    child.text = spannable
                }
            }
        }
    }
}
