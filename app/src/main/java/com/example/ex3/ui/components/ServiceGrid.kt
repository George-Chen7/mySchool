package com.example.ex3.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ex3.ui.theme.LightGray
import com.example.ex3.ui.theme.LinkBlue
import com.example.ex3.ui.theme.SzuRed

data class ServiceColumn(
    val header: String,
    val items: List<String>
)

@Composable
fun ServiceGrid(
    modifier: Modifier = Modifier
) {
    val columns = listOf(
        ServiceColumn("教师事务", listOf(
            "办事大厅", "教师邮箱", "畅课平台", "OA系统", "教务部",
            "研究生院", "科学技术", "社会科学", "人力资源", "实验设备",
            "财务服务", "招标采购"
        )),
        ServiceColumn("学生事务", listOf(
            "UOOC课程", "学生邮箱", "本科生选课", "研究生选课", "学生工作",
            "事务中心", "心理咨询", "缴学杂费", "考研考博", "就业指导",
            "本科生成绩", "研究生成绩"
        )),
        ServiceColumn("荔园生活", listOf(
            "一网通办", "创新创业", "团学表彰", "工会活动", "学生活动",
            "缤纷荔园", "校园融媒", "体育场馆", "勤工助学", "国际交流",
            "总医院", "华南医院"
        )),
        ServiceColumn("网上服务", listOf(
            "图书馆", "信息服务", "校务速办", "会议室申请", "校史馆预约",
            "校园卡", "故障报修", "失物招领", "后勤保障", "正版软件",
            "基金会", "中行网银"
        ))
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(420.dp)
            .verticalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        columns.forEach { col ->
            Column(modifier = Modifier.weight(1f)) {
                // Column header
                Text(
                    text = col.header,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SzuRed)
                        .padding(vertical = 6.dp, horizontal = 4.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
                // Items
                col.items.forEach { item ->
                    Text(
                        text = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LightGray.copy(alpha = 0.5f))
                            .padding(vertical = 5.dp, horizontal = 4.dp),
                        color = LinkBlue,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                    // TODO: 填写跳转地址
                }
            }
        }
    }
}
