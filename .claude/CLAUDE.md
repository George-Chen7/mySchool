# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

深大校园门户 App — 1:1 复刻深大官网视觉风格（深红 `#C8102E` + 白色），
Jetpack Compose + Material3 + Navigation Compose。覆盖：广播强制下线、WindowSizeClass 平板/手机自适应、三种持久化对比。

完整设计文档见 [.claude/PLAN.md](.claude/PLAN.md)。

## 构建/运行命令

```bash
./gradlew assembleDebug      # 构建 debug APK
./gradlew installDebug       # 构建并安装到模拟器/设备
./gradlew test               # 运行单元测试
./gradlew connectedAndroidTest  # 运行 instrumentation 测试
./gradlew clean              # 清理
```

无 CI/CD，纯本地 Android Studio 开发。建议用 `Pixel Tablet` 或 `Resizable` AVD 验证平板布局。

## 当前实现状态

**已完成：**
- Theme（Color / Type / Theme）— 深大红白配色
- LoginScreen — 学号/密码/记住密码，DataStore 持久化
- DashboardScreen — phone 单栏 + tablet 左右双栏（WindowWidthSizeClass）
- SessionManager — 全局单例 StateFlow，管理登录态 + 强制下线事件
- ForceOfflineReceiver — 接收 `com.example.ex3.ACTION_FORCE_OFFLINE` 广播
- ForceOfflineDialog — 不可 dismiss 的强制下线弹窗
- UserPreferences — DataStore + 原始 SharedPreferences 双实现
- FileStorage — 内部存储笔记 + 操作日志

**待完成：Room 数据库**
`data/db/` 目录为空。DashboardViewModel 当前用内存 `seedNews`，需替换为 Room（NewsEntity → NewsDao → AppDatabase）。引入时需：
- `gradle/libs.versions.toml` 加 `ksp` 和 `room` 版本
- `app/build.gradle.kts` 加 `alias(libs.plugins.ksp)` 插件和 `room-runtime`、`room-ktx`、`room-compiler`(KSP) 依赖

## 关键架构

### 路由
```
LOGIN → DASHBOARD → PROFILE
  ↑                    │
  └── popUpTo(0) ─────┘ (强制下线时)
```
- Login 成功后 `popUpTo(LOGIN, inclusive=true)` 跳 DASHBOARD
- Profile 用 `popBackStack()` 返回
- 强制下线弹窗在 MainActivity 层监听 `SessionManager.forceLogoutEvent`，跨路由生效

### 自适应
- `WindowWidthSizeClass.Expanded` → `DashboardScreenExpanded`（平板左右双栏 40/60）
- 其余 → `DashboardScreen`（手机纵向堆叠）

### 强制下线流
```
ProfileScreen 点"模拟其他设备登录"
  → sendBroadcast("com.example.ex3.ACTION_FORCE_OFFLINE")
  → ForceOfflineReceiver.onReceive()
  → SessionManager.forceLogout(reason)
  → MainActivity 观察 forceLogoutEvent → ForceOfflineDialog
  → 点"重新登录" → clearForceLogoutEvent() + navigate(LOGIN, popUpTo(0))
```

### 持久化选择
| 方式 | 文件 | 用途 |
|---|---|---|
| DataStore | `data/prefs/UserPreferences.kt` | 用户名/密码/记住密码/登录态 |
| SharedPreferences | 同文件 `rawPrefs` | 主题偏好（对比演示） |
| File I/O | `data/file/FileStorage.kt` | 用户笔记、操作日志 |
| Room | `data/db/`（TODO） | 新闻/公告缓存 |

### 设计决定
- `SessionManager` 是 `object` 单例（无 DI 框架），直接暴露 `StateFlow`
- ViewModel 继承 `AndroidViewModel` 获取 Application context（供 DataStore/File 用）
- 不引入 Hilt/Dagger — 保持依赖简单

## Git 规则

每次完成任务后若有代码变更，提交 git 并自动生成 summary commit message。
