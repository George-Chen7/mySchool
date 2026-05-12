package com.example.ex3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.ex3.session.SessionManager
import com.example.ex3.ui.components.ForceOfflineDialog
import com.example.ex3.ui.navigation.AdaptiveDashboard
import com.example.ex3.ui.navigation.NavGraph
import com.example.ex3.ui.navigation.Routes
import com.example.ex3.ui.theme.Ex3Theme
import com.example.ex3.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val forceLogoutEvent by SessionManager.forceLogoutEvent.collectAsState()
            val currentRoute = navController.currentBackStackEntry?.destination?.route

            Ex3Theme {
                NavGraph(
                    navController = navController,
                    dashboardContent = { modifier ->
                        AdaptiveDashboard(
                            widthSizeClass = androidx.compose.material3.windowsizeclass
                                .calculateWindowSizeClass(this).widthSizeClass,
                            onProfileClick = {
                                navController.navigate(Routes.PROFILE)
                            },
                            onLogoutClick = {
                                SessionManager.forceLogout("您已注销。")
                            }
                        )
                    }
                )

                // Show force offline dialog when event triggers
                if (forceLogoutEvent != null && currentRoute != Routes.LOGIN) {
                    ForceOfflineDialog(
                        reason = forceLogoutEvent ?: "您的账号已在其他设备登录。",
                        onReLogin = {
                            SessionManager.clearForceLogoutEvent()
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}
