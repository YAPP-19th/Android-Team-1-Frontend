import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("com.google.firebase.appdistribution")
    id("com.google.gms.google-services")
}

val appDistributionApkPath: String = System.getenv("BITRISE_SIGNED_APK_PATH") ?: "EMPTY"

android {
    compileSdk = Configs.COMPILE_SDK

    defaultConfig {
        applicationId = Configs.APPLICATION_ID
        minSdk = Configs.MIN_SDK
        targetSdk = Configs.TARGET_SDK
        versionCode = Configs.VERSION_CODE
        versionName = Configs.VERSION_NAME

        buildConfigField("String", "KAKAO_LOCAL_API_KEY", getApiKey("KAKAO_LOCAL_API_KEY"))
        buildConfigField("String", "NAVER_MAP_APIKEY_ID", getApiKey("NAVER_MAP_APIKEY_ID"))
        buildConfigField("String", "NAVER_MAP_APIKEY_SECRET", getApiKey("NAVER_MAP_APIKEY_SECRET"))
        manifestPlaceholders["NAVER_MAP_APIKEY_ID"] = getApiKey("NAVER_MAP_APIKEY_ID")

        buildConfigField("String", "KAKAO_LOGIN_API_KEY", getApiKey("KAKAO_LOGIN_API_KEY"))
        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = getApiKey("KAKAO_NATIVE_APP_KEY")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    buildTypes {
        getByName(Configs.DEBUG) {
            isMinifyEnabled = false
            isDebuggable = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName(Configs.RELEASE) {
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            firebaseAppDistribution {
                groups = "DeliBuddy"
                apkPath = appDistributionApkPath
            }

            signingConfig = null
        }

        create(Configs.QA) {
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            firebaseAppDistribution {
                groups = "DeliBuddy"
                apkPath = appDistributionApkPath
            }

            signingConfig = null
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

fun getApiKey(propertyName: String): String {
    return gradleLocalProperties(rootDir).getProperty(propertyName) ?: Configs.PROP_EMPTY
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(Kotlin.COROUTINES_CORE)
    implementation(Kotlin.COROUTINES_ANDROID)

    implementation(AndroidX.APP_COMPAT)
    implementation(AndroidX.CONSTRAINT_LAYOUT)
    implementation(AndroidX.LEGACY_SUPPORT)
    implementation(AndroidX.DATA_BINDING_RUNTIME)
    implementation(AndroidX.VIEW_DATA_BINDING)
    implementation(AndroidX.RECYCLERVIEW)

    implementation(AndroidX.CORE_KTX)
    implementation(AndroidX.ACTIVITY_KTX)
    implementation(AndroidX.FRAGMENT_KTX)

    implementation(AndroidX.LIFECYCLE_VIEWMODEL_KTX)
    implementation(AndroidX.LIFECYCLE_EXTENSIONS)
    implementation(AndroidX.LIFECYCLE_RUNTIME_KTX)

    implementation(Google.HILT_ANDROID)
    kapt(Google.HILT_ANDROID_COMPILER)
    kapt(Google.HILT_COMPILER)
    implementation(Google.MATERIAL)
    implementation(Google.GSON)
    implementation(platform(Google.FIREBASE_BOM))
    implementation(Google.FIREBASE_ANALYTICS)
    implementation(Google.FIREBASE_MESSAGING)
    implementation(Google.LOCATION)

    implementation(Libraries.RETROFIT)
    implementation(Libraries.RETROFIT_CONVERTER_GSON)
    implementation(Libraries.OKHTTP)
    implementation(Libraries.OKHTTP_LOGGING_INTERCEPTOR)
    implementation(Libraries.TIMBER)
    implementation(Libraries.LOTTIE)
    implementation(Libraries.GLIDE)
    kapt(Libraries.GLIDE_COMPILER)
    implementation(Libraries.JWT_DECODE)

    implementation(Libraries.NAVER_MAP)
    implementation(Libraries.KAKAO_LOGIN)

    testImplementation(UnitTest.JUNIT)

    androidTestImplementation(AndroidTest.ANDROID_JUNIT)
    androidTestImplementation(AndroidTest.ESPRESSO_CORE)
}
