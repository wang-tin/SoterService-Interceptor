# 江南护法 - Jiangnan Guardian

## 说明
该模块是一个Magisk模块和Xposed模块的组合，用于拦截SoterService调用。

## 作者
王听

## 免责声明
该模块仅用于学习研究，请勿用于违法行为，出现问题自负。

## 文件说明
- `JiangnanGuardian_Module.zip` - Magisk模块安装包
- `JiangnanGuardian_APK/` - Android项目源码，用于编译APK
- `JiangnanGuardian.apk` - Xposed模块APK

## 安装方法

### Magisk模块安装
1. 打开Magisk Manager
2. 进入模块页面
3. 点击 "+" 号
4. 选择 `JiangnanGuardian_Module.zip`
5. 等待安装完成
6. 重启设备

### Xposed模块安装
1. 安装 `JiangnanGuardian.apk`
2. 打开Xposed/LSPosed Manager
3. 找到 "江南护法" 模块并启用
4. 选择目标应用 "com.chunqiunativecheck"
5. 重启设备或目标应用

## 功能
- 拦截SoterService接口调用
- 记录调用栈信息
- 监控Native方法调用

## 版本
v1.0
