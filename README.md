# 我的校园 — SZU Campus Portal

深大校园门户 Android App，复刻[深圳大学官网](https://www1.szu.edu.cn/)视觉风格，适配手机和平板。

## 功能

- **登录** — 学号/密码登录，支持记住密码
- **主页** — 公文通、通知列表、四列功能导航网格（教师事务/学生事务/荔园生活/网上服务）
- **个人中心** — 数据存储演示 + 强制下线模拟
- **平板自适应** — 手机单栏 / 平板左右双栏
- **强制下线** — 通过 BroadcastReceiver 实现，不可跳过

## 技术栈

Jetpack Compose + Material3 + Navigation Compose + DataStore + Room + WindowSizeClass

## 构建

```bash
./gradlew assembleDebug      # 构建 debug APK
./gradlew installDebug       # 安装到模拟器/设备
./gradlew test               # 运行测试
```

建议使用 `Pixel Tablet` 或 `Resizable` AVD 验证平板布局。
