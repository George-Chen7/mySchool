package com.example.ex3.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.ex3.session.SessionManager

class ForceOfflineReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_FORCE_OFFLINE) {
            SessionManager.forceLogout("您的账号已在其他设备登录，请重新登录。")
        }
    }

    companion object {
        const val ACTION_FORCE_OFFLINE = "com.example.ex3.ACTION_FORCE_OFFLINE"
    }
}
