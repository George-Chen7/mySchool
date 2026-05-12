package com.example.ex3.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ex3.ui.theme.LightGray
import com.example.ex3.ui.theme.SzuRed
import com.example.ex3.ui.theme.SzuRedDark
import com.example.ex3.ui.theme.SzuRedLight
import com.example.ex3.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val username by viewModel.username.collectAsState("")
    val notesContent by viewModel.notesContent.collectAsState()
    val opLog by viewModel.opLog.collectAsState()
    var editNotes by remember { mutableStateOf("") }
    var darkMode by remember { mutableStateOf(viewModel.isDarkTheme) }

    LaunchedEffect(Unit) { viewModel.loadFileData() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SzuRed)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "个人中心",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            OutlinedButton(
                onClick = onBack,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Text("返回主页")
            }
        }

        // User info
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "欢迎, $username",
                style = MaterialTheme.typography.headlineSmall,
                color = SzuRed
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Data storage demo section
            SectionHeader("数据存储演示")
            Text(
                text = "对比三种持久化方式的特点与适用场景",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // DataStore (modern SharedPreferences)
            CardSection("DataStore Preferences (现代 SharedPreferences)") {
                Text(
                    text = "用途: Session token、用户名存储\n特点: 协程安全、异步读写、适合小量 key-value",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }

            // Raw SharedPreferences
            CardSection("SharedPreferences (原始 API)") {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "深色模式",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = darkMode,
                        onCheckedChange = {
                            darkMode = it
                            viewModel.toggleTheme(it)
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = SzuRed, checkedTrackColor = SzuRedLight)
                    )
                }
                Text(
                    text = "方式: rawPrefs.edit().putBoolean().apply()\n特点: 同步提交、无协程支持、适合旧项目中少量标记位",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }

            // File storage
            CardSection("File I/O (内部存储)") {
                OutlinedTextField(
                    value = editNotes,
                    onValueChange = { editNotes = it },
                    label = { Text("用户笔记 (内部存储到文件)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { viewModel.saveNotes(editNotes) },
                    colors = ButtonDefaults.buttonColors(containerColor = SzuRed)
                ) {
                    Text("保存笔记到文件")
                }
                if (notesContent.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "已保存: $notesContent",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "📋 操作日志 (op_log.txt):",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = opLog,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Force offline demo
            SectionHeader("安全功能")
            CardSection("强制下线机制") {
                Text(
                    text = "点击下方按钮模拟\"其他设备登录\"场景，\nApp 将发出广播并强制跳转回登录页面。",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { viewModel.simulateOtherDeviceLogin() },
                        colors = ButtonDefaults.buttonColors(containerColor = SzuRed)
                    ) {
                        Text("模拟其他设备登录")
                    }
                    OutlinedButton(
                        onClick = { viewModel.logout() },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SzuRed)
                    ) {
                        Text("注销")
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .background(SzuRed)
            .padding(vertical = 6.dp, horizontal = 12.dp),
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    )
}

@Composable
private fun CardSection(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = SzuRedDark
        )
        Spacer(modifier = Modifier.height(4.dp))
        content()
        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(color = LightGray)
    }
}
