plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("parcelize")
    kotlin("kapt")
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

    lintOptions {
        abortOnError = false
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
    }

    //如果不想生成某个布局的绑定类，可以在根视图添加 tools:viewBindingIgnore="true" 属性。
    buildFeatures {
        viewBinding = true
    }

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    //测试
    testImplementation(TestLibraries.junit)

    api(project(':lib_foundation'))

    //AndroidSupport
    api(AndroidLibraries.activity)
    api(AndroidLibraries.activityKtx)
    api(AndroidLibraries.fragment)
    api(AndroidLibraries.fragmentKtx)
    api(AndroidLibraries.appcompat)
    api(AndroidLibraries.recyclerView)
    api(AndroidLibraries.percentLayout)
    api(AndroidLibraries.constraintLayout)
    api(AndroidLibraries.swiperefreshlayout)
    api(AndroidLibraries.material)
    api(AndroidLibraries.viewpager2)
    api(AndroidLibraries.annotations)

    //AAC
    api(AndroidLibraries.archRuntime)
    api(AndroidLibraries.archCommon)
    api(AndroidLibraries.fragmentKtx)
    api(AndroidLibraries.lifecycleCommon)
    api(AndroidLibraries.lifecycleCommonJava8)
    api(AndroidLibraries.lifecycleRuntimeKtx)
    api(AndroidLibraries.lifecycleLiveDataCore)
    api(AndroidLibraries.lifecycleLiveData)
    api(AndroidLibraries.lifecycleLiveKtx)
    api(AndroidLibraries.lifecycleViewModel)
    api(AndroidLibraries.lifecycleViewModelKtx)
    api(AndroidLibraries.lifecycleExtensions)
    api(AndroidLibraries.lifecycleReactiveStreams)

    //Kotlin
    api(KotlinLibraries.kotlinStdlib)
    api(KotlinLibraries.kotlinReflect)
    api(KotlinLibraries.kotlinCoroutines)
    api(KotlinLibraries.kotlinAndroidCoroutines)
    api(KotlinLibraries.kotlinxCoroutinesRx2)

    //RxJava
    api(ThirdLibraries.rxJava)
    api(ThirdLibraries.rxAndroid)
    api(ThirdLibraries.rxBinding)
    api(ThirdLibraries.autoDispose)
    api(ThirdLibraries.autoDisposeAndroid)
    api(ThirdLibraries.autoDisposeLifecycle)
    api(ThirdLibraries.autoDisposeLifecycleArchcomponents)

    //LoadMore
    api(UILibraries.wrapperAdapter)

    //Adapter
    api(UILibraries.multiType)

    //Log
    api(ThirdLibraries.timber)

    //ImageLoader
    api(ThirdLibraries.glide)
    api(ThirdLibraries.glideOkHttp)
    api(ThirdLibraries.okHttp)

    //Utils
    api(ThirdLibraries.utilcode)
    api(ThirdLibraries.jOOR)
    api(ThirdLibraries.supportOptional)
}