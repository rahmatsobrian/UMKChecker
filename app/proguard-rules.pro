# Add project specific ProGuard rules here.

# Keep Room entities and DAOs
-keep class com.rahmatsobrian.umkchecker.data.local.entity.** { *; }

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.rahmatsobrian.umkchecker.**$$serializer { *; }
-keepclassmembers class com.rahmatsobrian.umkchecker.** {
    *** Companion;
}
-keepclasseswithmembers class com.rahmatsobrian.umkchecker.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Remove Log calls in release builds (extra safety on top of R8 rules in code)
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
}
