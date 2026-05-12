package com.example.ex3.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ex3.ui.theme.SzuRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DateBar(modifier: Modifier = Modifier) {
    val dateStr = SimpleDateFormat("yyyy年MM月dd日 EEEE", Locale.CHINESE).format(Date())
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(SzuRed)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = dateStr,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
