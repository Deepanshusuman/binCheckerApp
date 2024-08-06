
-keep class * extends com.google.protobuf.GeneratedMessageLite { *; }
-keep public class com.google.firebase.** {*;}
-keep class com.google.android.gms.internal.** {*;}
-keepclasseswithmembers class com.google.firebase.FirebaseException
-keepclassmembers, allowoptimization class io.grpc.okhttp.OkHttpChannelProvider {
    <init>();
}
-keep,allowshrinking class **.reflect.TypeToken { *; }
-keep,allowshrinking class * extends **.reflect.TypeToken

-dontwarn javax.naming.NamingEnumeration
-dontwarn javax.naming.NamingException
-dontwarn javax.naming.directory.Attribute
-dontwarn javax.naming.directory.Attributes
-dontwarn javax.naming.directory.DirContext
-dontwarn javax.naming.directory.InitialDirContext