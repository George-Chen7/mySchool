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

    private fun findMenuTextViews(view: View): List<TextView> {
        val result = mutableListOf<TextView>()
        if (view is TextView && view.text.startsWith("· ")) {
            result.add(view)
        } else if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                result.addAll(findMenuTextViews(view.getChildAt(i)))
            }
        }
        return result
    }

    private val menuUrls = mapOf(
        // 教师事务
        "· 办事大厅" to "https://ehall.szu.edu.cn/new/index.html",
        // 其余菜单项待填写 URL 后自动跳转浏览器，未配置则 Toast 提示
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
