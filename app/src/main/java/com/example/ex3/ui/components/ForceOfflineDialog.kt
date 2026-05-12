package com.example.ex3.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.ex3.ui.theme.SzuRed

@Composable
fun ForceOfflineDialog(
    reason: String,
    onReLogin: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /* cannot dismiss */ },
        title = {
            Text(
                text = "提示",
                color = SzuRed,
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Text(
                text = reason,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = onReLogin) {
                Text("重新登录", color = SzuRed)
            }
        }
    )
}
