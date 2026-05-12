# 我的校园 — SZU Campus Portal App Plan

## 项目目标
1:1 复刻深大官网（https://www1.szu.edu.cn/）视觉风格，适配手机和平板，
加一个同风格的登录界面。功能模块保留跳转地址占位符，供后续填写。
同时覆盖实验要求：广播强制下线、平板/手机自适应、三种持久化方式对比。

---

## 视觉风格（来自参考图）
- 主色：深红/玫红 `#C8102E`（深大标准红）
- 辅色：白色背景、浅灰分割线
- 导航栏/标题栏：深红底色 + 白字
- 功能区列头：深红底 + 白字粗体
- 链接文字：深蓝 `#003087` 或黑色
- 字体：系统默认无衬线，正文 14sp
- 整体布局：左侧通知列表 + 右侧四列功能导航网格

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
│   └── SessionManager.kt
├── broadcast/
│   └── ForceOfflineReceiver.kt
├── data/
│   ├── db/
│   │   ├── NewsEntity.kt
│   │   ├── NewsDao.kt
│   │   └── AppDatabase.kt
│   ├── prefs/
│   │   └── UserPreferences.kt     # DataStore + 原始 SharedPreferences 双实现
│   └── file/
│       └── FileStorage.kt
├── viewmodel/
│   ├── LoginViewModel.kt
│   ├── DashboardViewModel.kt
│   └── ProfileViewModel.kt
└── ui/
    ├── theme/
    │   ├── Color.kt               # 深大红白配色
    │   ├── Type.kt
    │   └── Theme.kt
    ├── components/
    │   ├── SzuTopBar.kt           # 顶部 Logo + 校园图片横幅
    │   ├── UserBar.kt             # "陈已润 | 个人中心 | 注销 | 说明" 行
    │   ├── NoticeBoard.kt         # 左侧公文通 + 重要通知列表
    │   ├── ServiceGrid.kt         # 右侧四列功能导航网格
    │   ├── DateBar.kt             # 底部日期栏
    │   └── ForceOfflineDialog.kt
    ├── screens/
    │   ├── LoginScreen.kt         # 深大红白风格登录页
    │   ├── DashboardScreen.kt     # 主页（组合上述组件）
    │   └── ProfileScreen.kt       # 个人中心 + 踢下线 + 数据存储演示
    └── navigation/
        ├── NavGraph.kt
        └── AdaptiveLayout.kt      # 手机单栏滚动 / 平板双栏
```

---

## 页面布局设计

### LoginScreen（登录页）
- 顶部：深大红色横幅 + Logo + "SHENZHEN UNIVERSITY" 字样
- 中间：白色卡片，学号/密码输入框，记住密码 Checkbox，深红登录按钮
- 底部：深红色脚注栏
- 风格与官网一致：红白配色，无圆角大卡片

### DashboardScreen（主页，复刻参考图）

**手机布局（Compact）**：垂直滚动，各区块依次排列
```
┌─────────────────────────────┐
│  顶部横幅：Logo + 校园图片   │  深红左 + 图片右
├─────────────────────────────┤
│  用户栏：陈已润 | 个人中心…  │  浅灰底
├─────────────────────────────┤
│  公文通区块                  │  白底，深红标题
├─────────────────────────────┤
│  重要通知 | 深大新闻 | 学术  │  Tab 切换
│  新闻列表（LazyColumn）      │
├─────────────────────────────┤
│  四列功能网格（2x2 折叠）    │  深红列头
├─────────────────────────────┤
│  底部日期栏                  │  深红底
└─────────────────────────────┘
```

**平板布局（Expanded）**：左右双栏，复刻参考图原始比例
```
┌──────────────────────────────────────────┐
│         顶部横幅（全宽）                  │
├──────────────────────────────────────────┤
│  用户栏（全宽）                           │
├─────────────────┬────────────────────────┤
│  左栏（~40%）   │  右栏（~60%）           │
│  · 公文通       │  · 四列功能网格          │
│  · 通知列表     │    教师事务/学生事务/    │
│                 │    荔园生活/网上服务     │
├─────────────────┴────────────────────────┤
│  底部日期栏（全宽）                       │
└──────────────────────────────────────────┘
```

### 功能导航网格（ServiceGrid）
四列，每列有列头（深红底白字）+ 若干链接项：

| 教师事务 | 学生事务 | 荔园生活 | 网上服务 |
|---|---|---|---|
| 办事大厅 | UOOC课程 | 一网通办 | 图书馆 |
| 教师邮箱 | 学生邮箱 | 创新创业 | 信息服务 |
| 畅课平台 | 本科生选课 | 团学表彰 | 校务速办 |
| OA系统 | 研究生选课 | 工会活动 | 会议室申请 |
| 教务部 | 学生工作 | 学生活动 | 校史馆预约 |
| 研究生院 | 事务中心 | 缤纷荔园 | 校园卡 |
| 科学技术 | 心理咨询 | 校园融媒 | 故障报修 |
| 社会科学 | 缴学杂费 | 体育场馆 | 失物招领 |
| 人力资源 | 考研考博 | 勤工助学 | 后勤保障 |
| 实验设备 | 就业指导 | 国际交流 | 正版软件 |
| 财务服务 | 本科生成绩 | 总医院 | 基金会 |
| 招标采购 | 研究生成绩 | 华南医院 | 中行网银 |

每个链接项代码中留 `TODO: 填写跳转地址` 占位注释。

---

## 强制下线机制
```
ProfileScreen 点击"模拟其他设备登录"
  → sendBroadcast(Intent("com.example.ex3.ACTION_FORCE_OFFLINE"))
  → ForceOfflineReceiver.onReceive()
  → SessionManager.forceLogout(reason)
  → MainActivity 观察 forceLogoutEvent StateFlow
  → 显示 ForceOfflineDialog（不可 dismiss）
  → 用户点"重新登录" → navController.navigate(Login) + popUpTo(0)
```

---

## 数据持久化三种方式对比
| 方式 | 用途 | 适用场景说明 |
|---|---|---|
| SharedPreferences（原始API） | 记住密码、主题偏好 | 少量简单 key-value，无需异步 |
| DataStore Preferences（现代SP） | Session token、用户名 | 替代SP，协程安全，推荐新项目使用 |
| Room SQLite | 新闻/公告列表缓存 | 结构化数据，需要查询/排序/过滤 |
| File 内部存储 | 用户笔记、操作日志 | 非结构化大文本，无需查询 |

ProfileScreen 中有"数据存储演示"区块，展示三种方式的读写操作及说明文字。

---

## 实现顺序
1. 依赖配置（libs.versions.toml + build.gradle.kts + Manifest）
2. Theme 层（Color / Type / Theme）— 深大红白配色
3. Data 层（Entity → Dao → Database → UserPreferences → FileStorage）
4. Session + Broadcast（SessionManager → ForceOfflineReceiver）
5. UI 组件（SzuTopBar → UserBar → NoticeBoard → ServiceGrid → DateBar → ForceOfflineDialog）
6. ViewModels
7. 各 Screen（LoginScreen → DashboardScreen → ProfileScreen）
8. Navigation + AdaptiveLayout
9. MainActivity 整合

---

## 验证方式
1. 手机模拟器：登录 → 主页滚动查看所有区块 → 个人中心 → 点"模拟踢下线" → 弹窗 → 回登录页
2. 平板模拟器（Pixel Tablet）：验证左右双栏布局与参考图一致
3. 旋转屏幕：验证 WindowSizeClass 切换
4. 重启 App：验证"记住密码"自动填充
5. 新闻列表：首次启动预填充 Room 数据，验证 SQLite 读取
