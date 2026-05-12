package com.example.ex3.ui.fragments

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

    private fun onMenuItemClick(text: String) {
        val msg = when (text) {
            // 教师事务
            "· 办事大厅" -> "办事大厅 — 功能待上线"
            "· 教师邮箱@" -> "教师邮箱 — 功能待上线"
            "· 畅课平台" -> "畅课平台 — 功能待上线"
            "· OA系统" -> "OA系统 — 功能待上线"
            "· 教务部" -> "教务部 — 功能待上线"
            "· 研究生院" -> "研究生院 — 功能待上线"
            "· 科学技术" -> "科学技术 — 功能待上线"
            "· 社会科学" -> "社会科学 — 功能待上线"
            "· 人力资源" -> "人力资源 — 功能待上线"
            "· 实验设备" -> "实验设备 — 功能待上线"
            "· 财务服务" -> "财务服务 — 功能待上线"
            "· 招标采购" -> "招标采购 — 功能待上线"
            // 学生事务
            "· UOOC课程" -> "UOOC课程 — 功能待上线"
            "· 学生邮箱@" -> "学生邮箱 — 功能待上线"
            "· 本科生选课" -> "本科生选课 — 功能待上线"
            "· 研究生选课" -> "研究生选课 — 功能待上线"
            "· 学生工作" -> "学生工作 — 功能待上线"
            "· 事务中心" -> "事务中心 — 功能待上线"
            "· 心理咨询" -> "心理咨询 — 功能待上线"
            "· 缴学杂费" -> "缴学杂费 — 功能待上线"
            "· 考研考博" -> "考研考博 — 功能待上线"
            "· 就业指导" -> "就业指导 — 功能待上线"
            "· 本科生成绩" -> "本科生成绩 — 功能待上线"
            "· 研究生成绩" -> "研究生成绩 — 功能待上线"
            // 荔园生活
            "· 一网通办" -> "一网通办 — 功能待上线"
            "· 创新创业" -> "创新创业 — 功能待上线"
            "· 团学表彰" -> "团学表彰 — 功能待上线"
            "· 工会活动" -> "工会活动 — 功能待上线"
            "· 学生活动" -> "学生活动 — 功能待上线"
            "· 缤纷荔园" -> "缤纷荔园 — 功能待上线"
            "· 校园融媒" -> "校园融媒 — 功能待上线"
            "· 体育场馆" -> "体育场馆 — 功能待上线"
            "· 勤工助学" -> "勤工助学 — 功能待上线"
            "· 国际交流" -> "国际交流 — 功能待上线"
            "· 总医院" -> "总医院 — 功能待上线"
            "· 华南医院" -> "华南医院 — 功能待上线"
            // 网上服务
            "· 图书馆" -> "图书馆 — 功能待上线"
            "· 信息服务" -> "信息服务 — 功能待上线"
            "· 校务速办" -> "校务速办 — 功能待上线"
            "· 会议室申请" -> "会议室申请 — 功能待上线"
            "· 校史馆预约" -> "校史馆预约 — 功能待上线"
            "· 校园一卡" -> "校园一卡 — 功能待上线"
            "· 故障报修" -> "故障报修 — 功能待上线"
            "· 失物招领" -> "失物招领 — 功能待上线"
            "· 后勤保障" -> "后勤保障 — 功能待上线"
            "· 正版软件" -> "正版软件 — 功能待上线"
            "· 基金会" -> "基金会 — 功能待上线"
            "· 中行网银" -> "中行网银 — 功能待上线"
            else -> "功能待上线"
        }
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}
