# SoterService 拦截器 - 技术说明

## 模块概述
- **作者**: 王听
- **版本**: 1.0
- **平台**: Android Xposed框架
- **目标**: 拦截com.chunqiunativecheck应用对SoterService的调用

## 警告
**该模块仅用于学习研究，请勿用于违法行为，出现问题自负。**

## 技术架构

### 1. 核心组件

#### SoterInterceptor.java
主模块类，实现了IXposedHookLoadPackage接口，负责：
- 检测目标应用加载
- 初始化各种Hook操作
- 管理拦截规则

#### 主要拦截点

##### 1.1 SoterService接口层
拦截SoterService APK提供的核心API：
- `getSoterAppInfo()` - 获取应用Soter配置信息
- `isSupportSoter()` - 检查设备Soter支持状态
- `requestAvailabilityAndSign()` - 请求生物特征认证

##### 1.2 系统服务层
通过hook ServiceManager监控：
- Soter系统服务的获取
- IBinder连接建立
- 服务调用路由

##### 1.3 Native层
Hook NativeCheck类的本地方法：
- `nativeCheckInit` - 初始化Native检查模块
- `nativeCheckUpdate` - 更新检查数据
- `nativeCheckVerify` - 执行本地验证
- `nativeCheckRelease` - 释放Native资源

### 2. 拦截策略

#### 双重Hook机制
```
┌─────────────────┐
│   Target App    │
│ com.chunqiunativecheck │
└────────┬────────┘
         │
         ├────────> 1. Java层Hook
         │           (SoterService API)
         │
         └────────> 2. Native层Hook
                     (NativeCheck JNI)
```

#### 调用监控
- 记录所有被hook方法的调用
- 捕获方法参数和返回值
- 记录完整调用栈信息
- 输出到Xposed日志系统

### 3. 日志输出

#### 日志格式
```
[SoterInterceptor] 时间戳
  拦截点: 方法名
  参数: [参数列表]
  返回值: [返回值]
  调用栈:
    at xxx.xxx.xxx.method(...)
    at xxx.xxx.xxx.method(...)
```

#### 查看方法
1. 打开LSPosed Manager或Xposed框架
2. 进入日志查看功能
3. 搜索标签: `SoterInterceptor`
4. 设置日志级别为Verbose

### 4. 使用场景

#### 学习研究
- 理解Android生物认证机制
- 学习Soter安全框架设计
- 分析应用安全验证流程

#### 安全审计
- 监控应用行为
- 检测异常调用
- 记录安全事件

#### 调试开发
- 追踪方法调用链路
- 分析参数传递
- 验证返回值逻辑

### 5. 编译构建

#### 环境要求
- Android SDK API 30
- Gradle 7.0+
- Android Studio 4.0+
- Xposed API 82

#### 编译步骤
```bash
# 克隆项目
git clone [项目地址]

# 进入目录
cd SoterServiceInterceptor

# 同步依赖
./gradlew sync

# 编译调试版
./gradlew assembleDebug

# 编译发布版
./gradlew assembleRelease
```

#### 输出
- Debug APK: `app/build/outputs/apk/debug/app-debug.apk`
- Release APK: `app/build/outputs/apk/release/app-release.apk`

### 6. 安装部署

#### 前置条件
1. Root权限
2. Xposed/LSPosed框架
3. Android 5.0+

#### 安装流程
1. 编译APK
2. 安装到设备
3. 打开Xposed管理器
4. 找到模块并启用
5. 选择目标应用
6. 软重启或重启目标应用

### 7. 配置文件

#### 默认行为
- 启用所有拦截
- 记录完整堆栈
- 输出详细日志

#### 可配置项
- 启用/禁用日志记录
- 启用/禁用堆栈记录
- 选择性拦截特定方法

### 8. 限制说明

#### 技术限制
- 仅支持Java层Hook
- Native层需要额外处理
- 混淆代码可能失效

#### 法律限制
- 仅用于学习研究
- 禁止逆向工程
- 禁止商业使用

### 9. 常见问题

#### Q: 模块无法激活
- 检查Root权限
- 确认Xposed框架安装
- 重启设备

#### Q: 没有拦截到日志
- 检查日志级别
- 确认目标应用已启动
- 查看Xposed日志设置

#### Q: 编译失败
- 检查Gradle版本
- 确认SDK配置
- 查看构建日志

### 10. 更新日志

#### v1.0 (2024)
- 初始版本发布
- 支持基本拦截功能
- 包含详细日志输出

## 责任声明
本模块仅供安全研究学习使用，使用者需自行承担所有责任。作者不对任何滥用行为负责。

## 技术支持
如遇到技术问题，请通过正规渠道反馈。

---

**最后更新**: 2024年
**免责声明**: 使用本模块即表示同意上述所有条款
