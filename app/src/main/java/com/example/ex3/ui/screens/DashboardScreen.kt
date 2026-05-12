package com.example.ex3.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ex3.ui.components.DateBar
import com.example.ex3.ui.components.NoticeBoard
import com.example.ex3.ui.components.ServiceGrid
import com.example.ex3.ui.components.SzuTopBar
import com.example.ex3.ui.components.UserBar
import com.example.ex3.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
    viewModel: DashboardViewModel = viewModel()
) {
    val news by viewModel.allNews.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        SzuTopBar()
        UserBar(
            username = "陈已润",
            onProfileClick = onProfileClick,
            onLogoutClick = onLogoutClick
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Compact layout: stacked vertically (phone)
        NoticeBoard(notices = news)
        Spacer(modifier = Modifier.height(8.dp))
        ServiceGrid()
        Spacer(modifier = Modifier.height(8.dp))
        DateBar()

        Box(modifier = Modifier.height(1.dp))
    }
}

@Composable
fun DashboardScreenExpanded(
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
    viewModel: DashboardViewModel = viewModel()
) {
    val news by viewModel.allNews.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        SzuTopBar()
        UserBar(
            username = "陈已润",
            onProfileClick = onProfileClick,
            onLogoutClick = onLogoutClick
        )

        // Expanded: left-right split (tablet)
        Row(modifier = Modifier.padding(4.dp)) {
            Column(modifier = Modifier.weight(0.4f)) {
                NoticeBoard(notices = news)
            }
            Column(modifier = Modifier.weight(0.6f)) {
                ServiceGrid()
            }
        }

        DateBar()
    }
}
