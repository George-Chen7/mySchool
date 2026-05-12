# 我的校园 — SZU Campus Portal App Plan

## 项目目标
1:1 复刻深大官网（https://www1.szu.edu.cn/）视觉风格，适配手机和平板。
覆盖实验要求：广播强制下线、平板/手机自适应、三种持久化方式对比。

---

## 视觉风格（来自参考图）
- 主色：深红 `#C8102E`（深大标准红）
- 辅色：`#C70E5C`（szu_primary）、`#E64F80`（szu_accent）、`#FAF1F5`（szu_light）、`#ECE3EB`（szu_panel）
- 导航栏/标题栏：深红底色 + 白字
- 功能区列头：深红底 + 白字粗体（学生事务/网上服务用 accent 粉红）
- 链接文字：深蓝 `#003087`（LinkBlue）
- 字体：系统默认无衬线，正文 13～17sp
- 整体布局：左侧通知列表 + 右侧四列功能导航网格

---

## 技术栈（当前实际）

| 层级 | 技术 |
|------|------|
| UI | **Android XML 原生布局** + Fragment |
| 自适应 | `layout-sw600dp` 目录（平板备用布局） |
| 导航 | 单 Activity（MainActivity）+ FragmentContainerView |
| 持久化 | DataStore + SharedPreferences + File I/O + Room（TODO） |
| 广播 | BroadcastReceiver（强制下线） |
| 遗留代码 | Jetpack Compose 组件（SzuTopBar/ServiceGrid/NoticeBoard 等，未接入） |

> **历史**: 项目初期用 Compose + Navigation Compose（`d2108cb`），后改为 XML + Fragment（`1e70514`）。
> Compose 代码保留在 `ui/components/`、`ui/screens/`、`ui/navigation/` 下但未接入 MainActivity。

---

## 当前文件结构

```
java/com/example/ex3/
├── MainActivity.kt                  # 单 Activity，setContentView(R.layout.activity_main)
├── session/
│   └── SessionManager.kt            # object 单例，管理登录态 + 强制下线事件
├── broadcast/
│   └── ForceOfflineReceiver.kt      # 接收 ACTION_FORCE_OFFLINE 广播
├── data/
│   ├── db/                          # (TODO) Room 数据库 — 目录为空
│   ├── prefs/
│   │   └── UserPreferences.kt       # DataStore + 原始 SharedPreferences 双实现
│   └── file/
│       └── FileStorage.kt           # 内部存储：笔记 + 操作日志
├── viewmodel/
│   ├── LoginViewModel.kt
│   ├── DashboardViewModel.kt
│   └── ProfileViewModel.kt
└── ui/
    ├── theme/                       # Compose Theme（Color/Type/Theme），被遗留 Compose 代码引用
    ├── fragments/
    │   ├── HeaderFragment.kt        # 顶部栏（10001.png校徽 + 横幅图）
    │   ├── LeftPanelFragment.kt     # 左侧：公网通入口 + 新闻Tab + 新闻列表 + 日期栏
    │   └── RightPanelFragment.kt    # 右侧：四列功能导航网格 + 底部校园图
    ├── components/                  # Compose 遗留组件（SzuTopBar/ServiceGrid/NoticeBoard 等）
    ├── screens/                     # Compose 遗留页面（LoginScreen/DashboardScreen/ProfileScreen）
    └── navigation/                  # Compose 遗留导航（NavGraph/AdaptiveLayout）

res/
├── layout/
│   ├── activity_main.xml            # 手机主页（ScrollView + 三个 FragmentContainerView）
│   ├── fragment_header.xml          # 头部 38%/62% 左右分栏
│   ├── fragment_left_panel.xml      # 公网通 + 新闻列表（含 10003.png 底图背景）
│   └── fragment_right_panel.xml     # 四列导航网格（48项菜单，已填写部分URL）
└── layout-sw600dp/
    ├── activity_main.xml            # 平板主页
    ├── fragment_right_panel.xml     # 平板服务网格
    └── fragment_left_panel.xml      # 平板新闻列表（视情况存在）
```

---

## UI 架构（XML + Fragment）

### 布局树
```
ScrollView
└── LinearLayout (vertical)
    ├── HeaderFragment           ← 10001.png 校徽 (38%) + 横幅图 (62%)
    ├── LoginNavBar (inline)     ← 陈己润 | 个人中心 | 注册 | 说明 (浅灰底)
    ├── LeftPanelFragment        ← 公网通入口 + 重要通知Tab + 15条新闻 + 日期栏
    └── RightPanelFragment       ← 四列网格 (教师事务/学生事务/荔园生活/网上服务) × 12行
```

### 自适应策略
- **手机** (`layout/`): 垂直堆叠，ScrollView 滚动
- **平板** (`layout-sw600dp/`): `activity_main.xml` 改为左右双栏 Row

---

## 页面详情

### 头部（fragment_header.xml）
- 左 38%：深红背景 + 10001.png 校徽，`fitCenter` 等比缩放
- 右 62%：校园风光横幅图

### 左侧面板（fragment_left_panel.xml）
- 校园公网通区块：标题 + 授权须知 + 下拉选择框 + "进入公网通"按钮 → `https://www1.szu.edu.cn/board/`
- 新闻 Tab 行：重要通知（选中）/ 深大新闻 / 学术讲座
- 新闻列表：15 条通知（[专栏]/[科研]/[教务]/[行政] 标签着色）
- 底图：10003.png 半透明背景（alpha=0.12, fitXY）
- 日期栏：动态计算当天日期 + 星期 + 学期第几周

### 右侧面板（fragment_right_panel.xml）
- 四列功能导航网格，每列结构：
  - 列标题：深红/accent 粉红背景 + 白字粗体 17sp
  - 子项列表：szu_panel/szu_light 背景 + 蓝字 17sp，居中
  - 底部"展开"提示
- 已填写 URL 的项（15个）：办事大厅、教师邮箱、畅课平台、OA系统、UOOC课程、学生邮箱、本科生选课、创新创业、体育场馆、图书馆、信息服务、校务速办 + 4个列标题
- 未填 URL 的项：Toast "xxx — 功能待上线"
- 底部：4 张校园风景图横排

---

## 当前实现状态

### 已完成
- [x] Theme（Color / Type）— 深大红白配色
- [x] XML 主页布局（activity_main + 三个 FragmentContainerView）
- [x] HeaderFragment — 10001.png 校徽 + 横幅图
- [x] LeftPanelFragment — 公网通、新闻Tab、15条通知、日期栏、10003.png底图
- [x] RightPanelFragment — 四列导航网格、部分URL跳转、底部校园图
- [x] 手机/平板自适应（layout + layout-sw600dp 双目录）
- [x] 全局点击逻辑（公网通/授权须知/Tab/新闻条目/日期栏/登录条/菜单项）
- [x] SessionManager + ForceOfflineReceiver + ForceOfflineDialog（Compose实现，未接入XML）
- [x] UserPreferences — DataStore + 原始 SharedPreferences 双实现
- [x] FileStorage — 内部存储笔记 + 操作日志
- [x] DashboardViewModel（Compose遗留）
- [x] ProfileScreen（Compose遗留，含强制下线按钮）

### 待完成
- [x] **登录模块** — `LoginActivity`（XML）+ `LoginViewModel`，含自动登录/注销/强制下线弹窗
- [ ] **Room 数据库** — `data/db/` 目录为空，需引入 KSP + Room 依赖
- [ ] **Compose ↔ XML 整合** — 决定最终用 Compose 还是 XML，清理遗留代码
- [ ] **剩余菜单 URL** — 48个菜单项中约 2/3 尚未填写跳转地址

---

## 强制下线机制（已接入XML）
```
MainActivity 个人中心 → "模拟其他设备登录"
  → sendBroadcast(Intent("com.example.ex3.ACTION_FORCE_OFFLINE"))
  → ForceOfflineReceiver.onReceive()
  → SessionManager.forceLogout(reason)
  → MainActivity 观察 forceLogoutEvent StateFlow
  → AlertDialog（不可取消）
  → 用户点"重新登录" → LoginActivity + CLEAR_TASK
```

---

## 数据持久化三种方式对比
| 方式 | 文件 | 用途 |
|---|---|---|
| SharedPreferences（原始API） | `data/prefs/UserPreferences.kt` → `rawPrefs` | 主题偏好、少量简单 key-value |
| DataStore Preferences（现代SP） | `data/prefs/UserPreferences.kt` | 用户名/密码/记住密码/登录态 |
| File I/O（内部存储） | `data/file/FileStorage.kt` | 用户笔记、操作日志 |
| Room SQLite | `data/db/`（TODO） | 新闻/公告缓存，需要查询/排序 |

---

## 实现顺序（建议）
1. **登录模块** — XML 新增登录页面 / 或将 Compose LoginScreen 接入
2. **整合决策** — 决定沿用 XML 还是切回 Compose，清理另一套代码
3. **Room 数据库** — 完成依赖配置 + Entity/Dao/Database 实现
4. **URL 补充** — 填写剩余菜单项的跳转地址
5. **强制下线接入** — 确保 ForceOfflineDialog 在 XML 架构下生效

---

## 验证方式
1. 手机模拟器：主页 → 滚动查看头部/新闻/网格/底部图 → 点击各菜单跳转浏览器
2. 平板模拟器：验证 `layout-sw600dp` 左右双栏布局
3. 点击"办事大厅"等 → 打开浏览器到正确 URL
4. 未填 URL 的菜单 → Toast "xxx — 功能待上线"
5. 日期栏显示当天日期 + 学期周次（动态计算）
6. 重啟 App → 验证 DataStore 持久化的用户名/密码
