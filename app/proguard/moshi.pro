# https://github.com/square/moshi/blob/master/moshi/src/main/resources/META-INF/proguard/moshi.pro

# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}

-keep @com.squareup.moshi.JsonQualifier interface *

# The name of @JsonClass types is used to look up the generated adapter.
-keepnames @com.squareup.moshi.JsonClass class *

# Retain generated JsonAdapters if annotated type is retained.
-keep class * extends com.squareup.moshi.JsonAdapter {
    <init>(...);
    <fields>;
}