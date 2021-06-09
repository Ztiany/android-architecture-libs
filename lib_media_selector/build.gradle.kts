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

    sourceSets.getByName("main") {
        java.srcDir("src/github/java")
        resjava.srcDir("src/github/res")
        java.srcDir("src/boxing/java")
        resjava.srcDir("src/boxing/res")
        java.srcDir("src/boxing-impl/java")
        resjava.srcDir("src/boxing-impl/res")
    }

    sourceSets.getByName("main") {
        java {
            srcDir("src/github/java")
            srcDir("src/boxing/java")
            srcDir("src/boxing-impl/java")
        }
        res {
            srcDir("src/github/res")
            srcDir("src/boxing/res")
            srcDir("src/boxing-impl/res")
        }
    }
}

dependencies {
    //support
    implementation(AndroidLibraries.appcompat)
    implementation(AndroidLibraries.material)
    implementation(AndroidLibraries.documentfile)
    implementation(AndroidLibraries.lifecycleRuntimeKtx)
    implementation(KotlinLibraries.kotlinStdlib)
    implementation(KotlinLibraries.kotlinCoroutines)
    //foundation
    implementation(project(":lib_foundation"))
    /*imageLoader*/
    implementation(ThirdLibraries.glide)
    implementation(ThirdLibraries.timber)
    implementation("com.github.yalantis:ucrop:2.2.4") {
        exclude(group = "com.squareup.okhttp3")
    }
}