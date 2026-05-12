package com.example.ex3.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ex3.ui.screens.DashboardScreen
import com.example.ex3.ui.screens.DashboardScreenExpanded

@Composable
fun AdaptiveDashboard(
    widthSizeClass: WindowWidthSizeClass,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (widthSizeClass) {
        WindowWidthSizeClass.Expanded -> {
            // Tablet: left-right split layout
            DashboardScreenExpanded(
                onProfileClick = onProfileClick,
                onLogoutClick = onLogoutClick
            )
        }
        else -> {
            // Phone: stacked vertical layout
            DashboardScreen(
                onProfileClick = onProfileClick,
                onLogoutClick = onLogoutClick
            )
        }
    }
}
