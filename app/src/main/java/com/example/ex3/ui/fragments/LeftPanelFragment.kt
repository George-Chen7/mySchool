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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.ex3.R
import java.util.Calendar
import java.util.concurrent.TimeUnit

class LeftPanelFragment : Fragment() {

    private val semesterStart by lazy {
        Calendar.getInstance().apply { set(2026, Calendar.MARCH, 9) }
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
        bindClickListeners(view)

        return view
    }

    private fun bindClickListeners(view: View) {
        val ctx = requireContext()

        // 进入公网通
        view.findViewById<TextView>(R.id.btn_enter).setOnClickListener {
            Toast.makeText(ctx, "校园公网通 — 功能待上线", Toast.LENGTH_SHORT).show()
        }

        // 授权须知
        view.findViewById<TextView>(R.id.tv_auth_notice).setOnClickListener {
            Toast.makeText(ctx, "授权须知 — 功能待上线", Toast.LENGTH_SHORT).show()
        }

        // 新闻 Tab 切换
        val tabImportant = view.findViewById<TextView>(R.id.tab_important)
        val tabNews = view.findViewById<TextView>(R.id.tab_news)
        val tabLecture = view.findViewById<TextView>(R.id.tab_lecture)

        tabImportant.setOnClickListener {
            selectTab(tabImportant, tabNews, tabLecture)
            Toast.makeText(ctx, "重要通知 — 已选中", Toast.LENGTH_SHORT).show()
        }
        tabNews.setOnClickListener {
            selectTab(tabNews, tabImportant, tabLecture)
            Toast.makeText(ctx, "深大新闻 — 功能待上线", Toast.LENGTH_SHORT).show()
        }
        tabLecture.setOnClickListener {
            selectTab(tabLecture, tabImportant, tabNews)
            Toast.makeText(ctx, "学术讲座 — 功能待上线", Toast.LENGTH_SHORT).show()
        }

        // 新闻条目
        view.findViewById<LinearLayout>(R.id.news_container).let { container ->
            for (i in 0 until container.childCount) {
                val child = container.getChildAt(i)
                if (child is TextView) {
                    child.setOnClickListener {
                        Toast.makeText(ctx, "新闻详情 — 功能待上线", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // 日期栏（查看校历）
        view.findViewById<TextView>(R.id.tv_date_bar).setOnClickListener {
            Toast.makeText(ctx, "校历 — 功能待上线", Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectTab(selected: TextView, vararg others: TextView) {
        selected.apply {
            setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.szu_primary))
            setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
        others.forEach {
            it.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            it.setTextColor(ContextCompat.getColor(requireContext(), R.color.szu_primary))
        }
    }

    private fun setupDateBar(view: View) {
        val dateBar = view.findViewById<TextView>(R.id.tv_date_bar)
        val now = Calendar.getInstance()

        val year = now.get(Calendar.YEAR)
        val month = now.get(Calendar.MONTH) + 1
        val day = now.get(Calendar.DAY_OF_MONTH)
        val weekDay = weekDayNames[now.get(Calendar.DAY_OF_WEEK) - 1]

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
