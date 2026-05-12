package com.example.ex3.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class NewsItem(
    val id: Long,
    val title: String,
    val category: String,
    val date: String,
    val source: String
)

private val seedNews = listOf(
    NewsItem(1, "关于2026年五一劳动节放假安排的通知", "重要通知", "2026-04-28", "党政办公室"),
    NewsItem(2, "关于开展2025-2026学年第二学期期中教学检查的通知", "重要通知", "2026-04-25", "教务部"),
    NewsItem(3, "关于校园卡系统升级维护的温馨提示", "重要通知", "2026-04-22", "信息中心"),
    NewsItem(4, "关于丽湖校区临时停水的通知", "重要通知", "2026-04-20", "后勤保障部"),
    NewsItem(5, "关于举办深圳大学2026年春季校园招聘会的通知", "重要通知", "2026-04-18", "学生部"),
    NewsItem(6, "深圳大学与华为签署战略合作协议", "深大新闻", "2026-04-30", "宣传部"),
    NewsItem(7, "我校在2026年全国大学生数学建模竞赛中获佳绩", "深大新闻", "2026-04-28", "数学科学学院"),
    NewsItem(8, "深大新增2个ESI全球前1%学科", "深大新闻", "2026-04-25", "发展规划部"),
    NewsItem(9, "我校学子荣获ACM-ICPC亚洲区域赛金牌", "深大新闻", "2026-04-22", "计算机与软件学院"),
    NewsItem(10, "深圳大学举办2026年研究生学术论坛", "学术动态", "2026-04-29", "研究生院"),
    NewsItem(11, "关于组织开展2026年国家自然科学基金申报的通知", "学术动态", "2026-04-26", "科学技术部"),
    NewsItem(12, "2026年粤港澳大湾区高校学术交流会在我校举行", "学术动态", "2026-04-23", "社会科学部")
)

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val _allNews = MutableStateFlow(seedNews)
    val allNews: StateFlow<List<NewsItem>> = _allNews.asStateFlow()
}
