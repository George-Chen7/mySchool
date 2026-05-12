package com.example.ex3.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ex3.R

class RightPanelFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_right_panel, container, false)
        bindMenuClickListeners(view)
        return view
    }

    private fun bindMenuClickListeners(root: View) {
        findMenuTextViews(root).forEach { tv ->
            tv.setOnClickListener { onMenuItemClick(tv.text.toString()) }
        }
    }

    private val columnHeaders = setOf("教师事务", "学生事务", "荔园生活", "网上服务")

    private fun findMenuTextViews(view: View): List<TextView> {
        val result = mutableListOf<TextView>()
        if (view is TextView && (view.text.startsWith("· ") || view.text.toString() in columnHeaders)) {
            result.add(view)
        } else if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                result.addAll(findMenuTextViews(view.getChildAt(i)))
            }
        }
        return result
    }

    private val menuUrls = mapOf(
        // 列标题
        "教师事务" to "https://www1.szu.edu.cn/view.asp?id=12",
        "学生事务" to "https://www1.szu.edu.cn/view.asp?id=13",
        "荔园生活" to "https://www1.szu.edu.cn/tv/",
        "网上服务" to "https://www1.szu.edu.cn/nc/",
        // 教师事务
        "· 办事大厅" to "https://ehall.szu.edu.cn/new/index.html",
        "· 教师邮箱@" to "https://mail.szu.edu.cn/",
        "· 畅课平台" to "https://lms.szu.edu.cn/user/index#/",
        "· OA系统" to "https://authserver.szu.edu.cn/authserver/login?service=http%3A%2F%2F210.39.3.155%3A9090%2FgoLogin.do",
        // 学生事务
        "· UOOC课程" to "https://www.uooc.net.cn/league/union",
        "· 学生邮箱@" to "https://www1.szu.edu.cn/nc/view.asp?id=633",
        "· 本科生选课" to "http://bkxk.szu.edu.cn/xsxkapp/sys/xsxkapp/*default/index.do",
        "· 创新创业" to "https://cxcy.szu.edu.cn/",
        // 荔园生活
        "· 体育场馆" to "https://www1.szu.edu.cn/v.asp?id=185",
        // 网上服务
        "· 图书馆" to "https://www.lib.szu.edu.cn/",
        "· 信息服务" to "https://www1.szu.edu.cn/nc/",
        "· 校务速办" to "https://xw.szu.edu.cn/sys/portal/page.jsp",
    )

    private fun onMenuItemClick(text: String) {
        val url = menuUrls[text]
        if (url != null) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
            return
        }
        val label = text.removePrefix("· ")
        Toast.makeText(requireContext(), "$label — 功能待上线", Toast.LENGTH_SHORT).show()
    }
}
