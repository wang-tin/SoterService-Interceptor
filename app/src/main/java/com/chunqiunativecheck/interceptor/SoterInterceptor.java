package com.chunqiunativecheck.interceptor;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class SoterInterceptor implements IXposedHookLoadPackage {

    private static final String TAG = "SoterInterceptor";
    private static final String TARGET_PACKAGE = "com.chunqiunativecheck";
    private static final String SOTER_SERVICE_APK = "com.tencent.soter.soterserver";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals(TARGET_PACKAGE)) {
            return;
        }

        XposedBridge.log(TAG + ": 已加载目标应用: " + TARGET_PACKAGE);

        hookSoterServiceCalls(lpparam);
        hookNativeCheckMethods(lpparam);
    }

    private void hookSoterServiceCalls(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class<?> soterServiceClass = XposedHelpers.findClassIfExists(
                "com.tencent.soter.soterserver.ISoterService",
                lpparam.classLoader
            );

            if (soterServiceClass != null) {
                XposedBridge.log(TAG + ": 找到SoterService接口，开始拦截...");

                XposedHelpers.findAndHookMethod(
                    soterServiceClass,
                    "getSoterAppInfo",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log(TAG + ": 拦截到getSoterAppInfo调用");
                            logStackTrace();
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log(TAG + ": getSoterAppInfo返回: " + param.getResult());
                        }
                    }
                );

                XposedHelpers.findAndHookMethod(
                    soterServiceClass,
                    "isSupportSoter",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log(TAG + ": 拦截到isSupportSoter调用");
                            logStackTrace();
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log(TAG + ": isSupportSoter返回: " + param.getResult());
                        }
                    }
                );

                XposedHelpers.findAndHookMethod(
                    soterServiceClass,
                    "requestAvailabilityAndSign",
                    Object.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log(TAG + ": 拦截到requestAvailabilityAndSign调用");
                            logStackTrace();
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log(TAG + ": requestAvailabilityAndSign返回: " + param.getResult());
                        }
                    }
                );
            } else {
                XposedBridge.log(TAG + ": 未找到SoterService接口，尝试其他方式拦截...");
                hookSystemServiceCalls(lpparam);
            }

        } catch (Exception e) {
            XposedBridge.log(TAG + ": Hook SoterService时出错: " + e.getMessage());
        }
    }

    private void hookSystemServiceCalls(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class<?> systemServiceManagerClass = XposedHelpers.findClassIfExists(
                "android.os.ServiceManager",
                lpparam.classLoader
            );

            if (systemServiceManagerClass != null) {
                XposedHelpers.findAndHookMethod(
                    systemServiceManagerClass,
                    "getService",
                    String.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            String serviceName = (String) param.args[0];
                            if (serviceName != null && serviceName.contains("soter")) {
                                XposedBridge.log(TAG + ": 拦截到Soter系统服务获取: " + serviceName);
                                logStackTrace();
                            }
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            if (param.getResult() != null) {
                                String serviceName = (String) param.args[0];
                                if (serviceName != null && serviceName.contains("soter")) {
                                    XposedBridge.log(TAG + ": Soter服务已获取: " + serviceName);
                                }
                            }
                        }
                    }
                );
            }
        } catch (Exception e) {
            XposedBridge.log(TAG + ": Hook系统服务时出错: " + e.getMessage());
        }
    }

    private void hookNativeCheckMethods(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            String[] nativeMethods = {
                "nativeCheckInit",
                "nativeCheckUpdate",
                "nativeCheckVerify",
                "nativeCheckRelease"
            };

            for (String methodName : nativeMethods) {
                try {
                    XposedHelpers.findAndHookMethod(
                        lpparam.classLoader.loadClass("com.chunqiunativecheck.NativeCheck"),
                        methodName,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                XposedBridge.log(TAG + ": 拦截到Native方法: " + methodName);
                                logStackTrace();
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                XposedBridge.log(TAG + ": Native方法 " + methodName + " 返回: " + param.getResult());
                            }
                        }
                    );
                    XposedBridge.log(TAG + ": 成功Hook native方法: " + methodName);
                } catch (Exception e) {
                    XposedBridge.log(TAG + ": Hook " + methodName + " 失败: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            XposedBridge.log(TAG + ": Hook Native方法时出错: " + e.getMessage());
        }
    }

    private void logStackTrace() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append(TAG).append(": 调用栈:\n");
        
        for (int i = 4; i < Math.min(stackTrace.length, 10); i++) {
            sb.append("  at ").append(stackTrace[i].toString()).append("\n");
        }
        
        XposedBridge.log(sb.toString());
    }
}
