# ProGuard / R8 rules for OFDRW Android app

# Keep WebView JavaScript interface methods
-keepclassmembers class org.ofdrw.android.OfdBridge {
    @android.webkit.JavascriptInterface <methods>;
}

# Keep all public API members called via reflection
-keepattributes JavascriptInterface

# Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}
