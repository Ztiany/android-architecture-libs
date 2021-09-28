plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdkVersion(AppConfig.compileSdkVersion)
    buildToolsVersion(AppConfig.buildToolsVersion)

    defaultConfig {
        minSdkVersion(AppConfig.minSdkVersion)
        targetSdkVersion(AppConfig.targetSdkVersion)
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    lintOptions {
        isAbortOnError = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

}

dependencies {
    implementation(AndroidLibraries.annotations)
    implementation(ThirdLibraries.timber)
    implementation(ThirdLibraries.retrofit)
    implementation(ThirdLibraries.retrofitConverterGson)
    implementation(ThirdLibraries.okHttp)
    implementation(ThirdLibraries.gson)

    implementation(ThirdLibraries.supportOptional)

    compileOnly(ThirdLibraries.rxJava)
    compileOnly(ThirdLibraries.retrofitRxJava2CallAdapter)

    compileOnly(KotlinLibraries.kotlinStdlib)
    compileOnly(KotlinLibraries.kotlinCoroutines)
}
