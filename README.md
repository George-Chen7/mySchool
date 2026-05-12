# 我的校园 — SZU Campus Portal

深大校园门户 Android App，1:1 复刻[深圳大学官网](https://www1.szu.edu.cn/)视觉风格（深红 `#C8102E` + 白色），适配手机和平板。

## 功能

- **登录** — 学号/密码登录，支持记住密码 + 自动登录（DataStore 持久化）
- **主页** — 公文通入口、新闻列表（4 类标签）、四列功能导航网格（48 项菜单）、底部校园风光图
- **个人中心** — 用户信息 + 退出登录 + 模拟强制下线
- **平板自适应** — `res/layout/` 手机单栏 / `res/layout-sw600dp/` 平板左右双栏
- **强制下线** — BroadcastReceiver + AlertDialog（不可取消），跨路由生效

## 技术栈

**UI:** Android XML 布局 + Fragment（单 Activity）

**自适应:** `layout-sw600dp` 目录

**持久化对比（三种方式）:**

| 方式 | 用途 |
|------|------|
| DataStore Preferences | 用户名/密码/记住密码/登录态（响应式 Flow） |
| SharedPreferences（原始 API） | 主题偏好（对比演示） |
| File I/O（内部存储） | 用户笔记 + 操作日志 |
| Room（SQLite） | 待实现 — 新闻/公告缓存 |

**其他:** BroadcastReceiver、AlertDialog、Lifecycle + Coroutines

> 历史: 项目初期使用 Jetpack Compose + Navigation Compose，后改为 XML + Fragment。Compose 遗留代码保留在 `ui/components/`、`ui/screens/`、`ui/navigation/` 下但未接入。

## 构建

```bash
./gradlew assembleDebug      # 构建 debug APK
./gradlew installDebug       # 安装到模拟器/设备
./gradlew test               # 运行测试
```

建议使用 `Pixel Tablet` 或 `Resizable` AVD 验证平板布局。
