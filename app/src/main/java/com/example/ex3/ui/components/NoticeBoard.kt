package com.example.ex3.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ex3.ui.theme.MediumGray
import com.example.ex3.ui.theme.SzuRed
import com.example.ex3.ui.theme.TextSecondary
import com.example.ex3.viewmodel.NewsItem

@Composable
fun NoticeBoard(
    notices: List<NewsItem>,
    modifier: Modifier = Modifier
) {
    val tabs = listOf("重要通知", "深大新闻", "学术动态")
    var selectedTab by remember { mutableIntStateOf(0) }
    val filtered = notices.filter { it.category == tabs[selectedTab] }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SzuRed)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = "公文通",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = SzuRed,
            divider = { HorizontalDivider(color = MediumGray) }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        LazyColumn(modifier = Modifier.height(280.dp)) {
            items(filtered) { news ->
                NoticeItem(news)
                HorizontalDivider(color = MediumGray.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
private fun NoticeItem(news: NewsItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = news.title,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(2.dp))
        Row {
            Text(text = news.source, color = SzuRed, fontSize = 10.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = news.date, color = TextSecondary, fontSize = 10.sp)
        }
    }
}
