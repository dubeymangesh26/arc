# Standard Android ProGuard rules
-keepattributes SourceFile,LineNumberTable
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

# Keep your model/response classes
# These are used for GSON serialization and Retrofit response parsing
-keep class com.arcltd.staff.response.** { *; }
-keep class com.arcltd.staff.models.** { *; }

# Retrofit 2/3 rules
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**
-keepclasseswithmembers interface * {
    @retrofit2.http.* <methods>;
}

# OkHttp rules
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**

# GSON rules
-keep class com.google.gson.** { *; }
-keep class com.google.gson.reflect.TypeToken
-keep class * extends com.google.gson.TypeAdapter
-keep @com.google.gson.annotations.SerializedName class * {
    <fields>;
}

# Glide rules
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# RxJava rules
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.* {
    long producerIndex;
    long consumerIndex;
}

# Firebase rules
-keep class com.google.firebase.** { *; }

# Room rules
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# AndroidX Navigation
-keepnames class androidx.navigation.fragment.NavHostFragment

# Dexter rules
-keep class com.karumi.dexter.** { *; }

# Volley (if still used anywhere)
-keep class com.android.volley.** { *; }

# Other 3rd party libraries
-keep class com.intuit.sdp.** { *; }
-keep class com.intuit.ssp.** { *; }
-keep class de.hdodenhof.circleimageview.** { *; }
-keep class com.balysv.materialripple.** { *; }
-keep class com.github.chrisbanes.photoview.** { *; }
-keep class com.daimajia.slider.library.** { *; }
-keep class com.nineoldandroids.** { *; }
-keep class org.jsoup.** { *; }
-keep class com.jakewharton.threetenabp.** { *; }

# ZXing / Barcode Scanner rules
-keep class com.journeyapps.barcodescanner.** { *; }
-dontwarn com.journeyapps.barcodescanner.**
-keep class com.google.zxing.** { *; }
-dontwarn com.google.zxing.**

