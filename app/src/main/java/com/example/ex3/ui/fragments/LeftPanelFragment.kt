package com.example.ex3.ui.fragments

import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.ex3.R

class LeftPanelFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_left_panel, container, false)

        setupSpinner(view)
        styleNewsItems(view)

        return view
    }

    private fun setupSpinner(view: View) {
        val spinner = view.findViewById<Spinner>(R.id.spinner_user_type)
        val options = arrayOf("阅览用户", "教职工", "学生", "访客")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun styleNewsItems(view: View) {
        val newsContainer = view.findViewById<LinearLayout>(R.id.news_container)
        val redColor = ContextCompat.getColor(requireContext(), R.color.szu_accent)

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
