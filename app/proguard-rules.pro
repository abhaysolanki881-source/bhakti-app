# Bhakti Sagar - Production R8 / ProGuard Configuration

# Preserve readable crash stack traces in Google Play Console
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Keep ViewModels, Data Models, and Room Entities/DAOs
-keep class com.example.ui.viewmodel.** { *; }
-keep class com.example.data.model.** { *; }
-keep class com.example.data.local.** { *; }
-keepclassmembers class * extends androidx.room.RoomDatabase {
    <init>();
}
-dontwarn androidx.room.**

# Keep AdMob Config and Managers
-keep class com.example.ads.** { *; }
-keep class com.example.services.** { *; }

# Google Next-Gen Mobile Ads SDK & UMP Consent SDK
-keep class com.google.android.libraries.ads.mobile.sdk.** { *; }
-dontwarn com.google.android.libraries.ads.mobile.sdk.**
-keep class com.google.android.ump.** { *; }
-dontwarn com.google.android.ump.**
-keep class com.google.android.gms.ads.** { *; }
-dontwarn com.google.android.gms.ads.**
-keep class com.google.ads.** { *; }
-dontwarn com.google.ads.**

# Keep Kotlin Coroutines & Reflection metadata
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**

# Keep DataStore & Preferences
-keep class androidx.datastore.** { *; }
-dontwarn androidx.datastore.**

# Keep Android Resource identifiers
-keepclassmembers class com.example.R$* {
    public static <fields>;
}

