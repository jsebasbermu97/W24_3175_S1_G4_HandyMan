# Prerequisite

just copy and paste these dependencies into your `build.gradle(:app)`
```agsl
dependencies {
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.airbnb.android:lottie:3.4.0")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("androidx.navigation:navigation-fragment:2.6.0")
    implementation("androidx.navigation:navigation-ui:2.6.0")
    implementation("androidx.activity:activity:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.biometric:biometric:1.1.0")
    implementation("com.airbnb.android:lottie:5.0.3")
}
```
