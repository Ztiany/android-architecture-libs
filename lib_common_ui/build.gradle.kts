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
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    testOptions{
        unitTests.isReturnDefaultValues = true
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

    //如果不想生成某个布局的绑定类，可以在根视图添加 tools:viewBindingIgnore="true" 属性。
    buildFeatures {
        enabled = true
    }

}

dependencies {
    implementation(AndroidLibraries.appcompat)
    implementation(AndroidLibraries.annotations)
    implementation(AndroidLibraries.viewpager)
    implementation(AndroidLibraries.recyclerView)
    implementation(AndroidLibraries.material)
    implementation(AndroidLibraries.ktx)

    implementation(KotlinLibraries.kotlinStdlib)

    implementation(ThirdLibraries.timber)
}