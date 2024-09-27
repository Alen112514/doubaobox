plugins {
    alias(libs.plugins.android.application)
}


android {
    namespace = "com.doubaobox.doubaobox"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.doubaobox.doubaobox"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    // Other dependencies

    // Add consistent lifecycle-viewmodel and lifecycle-viewmodel-ktx versions
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.5.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.0")

    // Retrofit and Gson converter for networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.android.material:material:1.9.0")
    // Testing libraries
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}