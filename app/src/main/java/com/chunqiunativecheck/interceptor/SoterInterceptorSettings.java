package com.chunqiunativecheck.interceptor;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedPreferenceActivity;
import de.robv.android.xposed.XposedInit;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SoterInterceptorSettings extends XposedPreferenceActivity {

    private static final String TAG = "SoterInterceptor";
    private SharedPreferences prefs;

    public static final String PREF_ENABLE_LOGGING = "enable_logging";
    public static final String PREF_LOG_STACK_TRACE = "log_stack_trace";
    public static final String PREF_INTERCEPT_GET_APP_INFO = "intercept_get_app_info";
    public static final String PREF_INTERCEPT_IS_SUPPORT = "intercept_is_support";
    public static final String PREF_INTERCEPT_SIGN = "intercept_sign";
    public static final String PREF_INTERCEPT_NATIVE = "intercept_native";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        addPreferencesFromResource(0x7f020000);

        loadPreferences();
        setupListeners();
    }

    private void loadPreferences() {
        boolean enableLogging = prefs.getBoolean(PREF_ENABLE_LOGGING, true);
        boolean logStackTrace = prefs.getBoolean(PREF_LOG_STACK_TRACE, true);
        boolean interceptGetAppInfo = prefs.getBoolean(PREF_INTERCEPT_GET_APP_INFO, true);
        boolean interceptIsSupport = prefs.getBoolean(PREF_INTERCEPT_IS_SUPPORT, true);
        boolean interceptSign = prefs.getBoolean(PREF_INTERCEPT_SIGN, true);
        boolean interceptNative = prefs.getBoolean(PREF_INTERCEPT_NATIVE, true);

        XposedBridge.log(TAG + ": 加载配置:");
        XposedBridge.log(TAG + ":   启用日志: " + enableLogging);
        XposedBridge.log(TAG + ":   记录堆栈: " + logStackTrace);
        XposedBridge.log(TAG + ":   拦截getAppInfo: " + interceptGetAppInfo);
        XposedBridge.log(TAG + ":   拦截isSupport: " + interceptIsSupport);
        XposedBridge.log(TAG + ":   拦截Sign: " + interceptSign);
        XposedBridge.log(TAG + ":   拦截Native: " + interceptNative);
    }

    private void setupListeners() {
        getPreferenceScreen().getSharedPreferences()
            .registerOnSharedPreferenceChangeListener((prefs, key) -> {
                XposedBridge.log(TAG + ": 配置已更新: " + key + " = " + prefs.getBoolean(key, false));
            });
    }

    public static boolean isLoggingEnabled() {
        return true;
    }

    public static boolean shouldLogStackTrace() {
        return true;
    }

    public static boolean shouldInterceptMethod(String methodName) {
        if (methodName.contains("getSoterAppInfo")) {
            return true;
        } else if (methodName.contains("isSupportSoter")) {
            return true;
        } else if (methodName.contains("requestAvailabilityAndSign")) {
            return true;
        } else if (methodName.contains("nativeCheck")) {
            return true;
        }
        return false;
    }
}
