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
import java.util.Calendar
import java.util.concurrent.TimeUnit

class LeftPanelFragment : Fragment() {

    // 2026年春季学期开学日（周一）
    private val semesterStart by lazy {
        Calendar.getInstance().apply {
            set(2026, Calendar.MARCH, 9)
        }
    }

    private val weekDayNames = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_left_panel, container, false)

        setupSpinner(view)
        styleNewsItems(view)
        setupDateBar(view)

        return view
    }

    private fun setupDateBar(view: View) {
        val dateBar = view.findViewById<TextView>(R.id.tv_date_bar)
        val now = Calendar.getInstance()

        val year = now.get(Calendar.YEAR)
        val month = now.get(Calendar.MONTH) + 1
        val day = now.get(Calendar.DAY_OF_MONTH)
        val weekDay = weekDayNames[now.get(Calendar.DAY_OF_WEEK) - 1]

        // 计算学期第几周
        val diffMs = now.timeInMillis - semesterStart.timeInMillis
        val diffDays = TimeUnit.MILLISECONDS.toDays(diffMs)
        val weekNumber = ((diffDays / 7) + 1).coerceAtLeast(1).toInt()

        dateBar.text = "${year}年${month}月${day}日  $weekDay  本学期第${weekNumber}周（查看校历）"
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
