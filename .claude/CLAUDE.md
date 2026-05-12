# 我的校园 — SZU Campus Portal App Plan

## 项目目标
模拟深大校园主页，覆盖：活动、自定义UI、碎片、广播机制、数据持久化。
核心亮点：Glassmorphism UI、强制下线广播、平板/手机自适应、三种持久化方式对比。

---

## 技术栈
- Jetpack Compose + Material3
- Navigation Compose
- Room (SQLite)
- DataStore Preferences（现代 SharedPreferences）+ 原始 SharedPreferences API（对比演示）
- File I/O（内部存储）
- BroadcastReceiver（强制下线）
- WindowSizeClass（平板适配）

---

## 依赖变更

### gradle/libs.versions.toml 新增版本
```
ksp = "2.2.10-1.0.29"
navigationCompose = "2.7.7"
room = "2.6.1"
datastorePreferences = "1.1.1"
lifecycleViewmodelCompose = "2.8.7"
```

### app/build.gradle.kts
- 新增插件：`alias(libs.plugins.ksp)`
- 新增依赖：navigation-compose, room-runtime, room-ktx, ksp(room-compiler),
  datastore-preferences, lifecycle-viewmodel-compose, material3-window-size-class
- 将 `compileSdk { version = release(36){...} }` 改为 `compileSdk = 36`（KSP 兼容性）

### AndroidManifest.xml
- 注册 `ForceOfflineReceiver`，`android:exported="false"`
- MainActivity 加 `android:windowSoftInputMode="adjustResize"`

---

## 文件结构

```
java/com/example/ex3/
├── MainActivity.kt
├── session/
│   └── SessionManager.kt          # StateFlow 单例，管理登录/强制下线状态
├── broadcast/
│   └── ForceOfflineReceiver.kt    # 接收广播 → 触发 SessionManager.forceLogout()
├── data/
│   ├── db/
│   │   ├── NewsEntity.kt
│   │   ├── NewsDao.kt
│   │   └── AppDatabase.kt
│   ├── prefs/
│   │   └── UserPreferences.kt     # DataStore + 原始 SharedPreferences 双实现
│   └── file/
│       └── FileStorage.kt         # 内部存储读写用户笔记/日志
├── viewmodel/
│   ├── LoginViewModel.kt
│   ├── DashboardViewModel.kt
│   └── ProfileViewModel.kt
└── ui/
    ├── theme/
    │   ├── Color.kt               # Glassmorphism 色板
    │   ├── Type.kt
    │   └── Theme.kt               # 固定暗色主题
    ├── components/
    │   ├── GradientBackground.kt  # 动态渐变背景
    │   ├── GlassCard.kt           # 半透明毛玻璃卡片
    │   └── ForceOfflineDialog.kt  # 强制下线弹窗（不可关闭）
    ├── screens/
    │   ├── LoginScreen.kt
    │   ├── DashboardScreen.kt     # 主页：新闻列表 + 快捷入口
    │   ├── NewsDetailScreen.kt
    │   └── ProfileScreen.kt       # 个人中心 + "模拟踢下线"按钮
    └── navigation/
        ├── NavGraph.kt
        └── AdaptiveLayout.kt      # 手机 BottomNav / 平板 NavigationRail+双栏
```

---

## 核心功能设计

### 1. Glassmorphism UI
- 背景：`Brush.verticalGradient` 深海军蓝→深紫→深青，`rememberInfiniteTransition` 缓慢动画
- 卡片：`Card` with `containerColor = Color(0x1AFFFFFF)`（白色10%透明）+ `BorderStroke(1.dp, Color(0x33FFFFFF))`
- 强调色：Cyan `#00E5FF`、Purple `#7C4DFF`、Teal `#1DE9B6`
- 文字：纯白 + 70%白副文字

### 2. 强制下线机制（广播）
```
ProfileScreen 点击"模拟其他设备登录"
  → sendBroadcast(Intent("com.example.ex3.ACTION_FORCE_OFFLINE"))
  → ForceOfflineReceiver.onReceive()
  → SessionManager.forceLogout(reason)
  → MainActivity 观察 forceLogoutEvent StateFlow
  → 显示 ForceOfflineDialog（不可 dismiss）
  → 用户点"重新登录" → navController.navigate(Login) + popUpTo(0)
```

### 3. 平板/手机自适应（WindowSizeClass）
| 屏幕宽度 | 布局 |
|---|---|
| Compact（手机竖屏） | 底部 BottomNavigationBar，单栏内容 |
| Medium（平板竖屏/手机横屏） | 左侧 NavigationRail，单栏内容 |
| Expanded（平板横屏） | 左侧 NavigationRail + 右侧双栏（列表+详情） |

### 4. 数据持久化三种方式对比
| 方式 | 用途 | 适用场景说明 |
|---|---|---|
| SharedPreferences（原始API） | 记住密码、主题偏好 | 少量简单 key-value，无需异步 |
| DataStore Preferences（现代SP） | Session token、用户名 | 替代SP，协程安全，推荐新项目使用 |
| Room SQLite | 新闻/公告列表缓存 | 结构化数据，需要查询/排序/过滤 |
| File 内部存储 | 用户笔记、操作日志 | 非结构化大文本，无需查询 |

ProfileScreen 中有"数据存储演示"区块，展示三种方式的读写操作及说明文字。

---

## 屏幕列表

### LoginScreen
- 全屏渐变背景，居中 GlassCard
- 学号/密码输入框 + 记住密码 Checkbox + 登录按钮
- 硬编码账号：admin/123456
- 自动填充：从 SharedPreferences 读取已保存账号密码

### DashboardScreen
- 顶部：学校 Logo + 搜索栏（GlassCard）
- 快捷功能网格：教务系统、图书馆、一卡通、校历（4个 GlassCard 图标按钮）
- 新闻公告列表：从 Room 读取，LazyColumn，每条 GlassCard

### NewsDetailScreen
- 接收 newsId，从 Room 查询详情
- 平板 Expanded 模式下作为右栏直接显示

### ProfileScreen
- 用户信息卡片（头像占位 + 姓名/学号）
- "模拟其他设备登录"按钮 → 发送强制下线广播
- "数据存储演示"区块
- 退出登录按钮

---

## 实现顺序
1. 依赖配置（libs.versions.toml + build.gradle.kts + Manifest）
2. Theme 层（Color / Type / Theme）
3. Data 层（Entity → Dao → Database → UserPreferences → FileStorage）
4. Session + Broadcast（SessionManager → ForceOfflineReceiver）
5. UI 组件（GradientBackground → GlassCard → ForceOfflineDialog）
6. ViewModels
7. 各 Screen
8. Navigation + AdaptiveLayout
9. MainActivity 整合

---

## 验证方式
1. 手机模拟器：登录 → 主页 → 个人中心 → 点"模拟踢下线" → 弹窗 → 回登录页
2. 平板模拟器（Pixel Tablet）：验证 NavigationRail + 双栏布局
3. 旋转屏幕：验证 WindowSizeClass 切换
4. 重启 App：验证"记住密码"自动填充
5. 新闻列表：首次启动预填充 Room 数据，验证 SQLite 读取
