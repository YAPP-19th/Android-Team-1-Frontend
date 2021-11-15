object Kotlin {
    const val KOTLIN_STDLIB              = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN_VERSION}"
    const val COROUTINES_CORE            = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.KOTLIN_COROUTINE}"
    const val COROUTINES_ANDROID         = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.KOTLIN_COROUTINE}"
}

object AndroidX {
    const val APP_COMPAT                 = "androidx.appcompat:appcompat:${Versions.APP_COMPAT}"
    const val CONSTRAINT_LAYOUT          = "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
    const val LEGACY_SUPPORT             = "androidx.legacy:legacy-support-v4:${Versions.LEGACY_SUPPORT}"
    const val VIEW_DATA_BINDING          = "com.android.databinding:viewbinding:${Versions.VIEW_DATA_BINDING}"
    const val RECYCLERVIEW               = "androidx.recyclerview:recyclerview:${Versions.RECYCLERVIEW}"

    const val CORE_KTX                   = "androidx.core:core-ktx:${Versions.CORE_KTX}"
    const val ACTIVITY_KTX               = "androidx.activity:activity-ktx:${Versions.ACTIVITY_KTX}"
    const val FRAGMENT_KTX               = "androidx.fragment:fragment-ktx:${Versions.FRAGMENT_KTX}"

    const val LIFECYCLE_VIEWMODEL_KTX    = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE_KTX}"
    const val LIFECYCLE_EXTENSIONS       = "androidx.lifecycle:lifecycle-extensions:${Versions.LIFECYCLE_EXTENSION}"
    const val LIFECYCLE_RUNTIME_KTX      = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LIFECYCLE_RUNTIME_KTX}"
}

object Google {
    const val HILT_ANDROID               = "com.google.dagger:hilt-android:${Versions.HILT}"
    const val HILT_CORE                  = "com.google.dagger:hilt-core:${Versions.HILT}"
    const val HILT_ANDROID_COMPILER      = "com.google.dagger:hilt-android-compiler:${Versions.HILT}"
    const val HILT_COMPILER              = "androidx.hilt:hilt-compiler:${Versions.HILT_COMPILER}"
    const val MATERIAL                   = "com.google.android.material:material:${Versions.MATERIAL}"
    const val GSON                       = "com.google.code.gson:gson:${Versions.GSON}"
    const val FIREBASE_BOM               = "com.google.firebase:firebase-bom:${Versions.FIREBASE_BOM}"
    const val FIREBASE_ANALYTICS         = "com.google.firebase:firebase-analytics"
}

object Libraries {
    const val RETROFIT                   = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val RETROFIT_CONVERTER_GSON    = "com.squareup.retrofit2:converter-gson:${Versions.RETROFIT}"
    const val OKHTTP                     = "com.squareup.okhttp3:okhttp:${Versions.OKHTTP}"
    const val OKHTTP_LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:${Versions.OKHTTP}"

    const val GLIDE                      = "com.github.bumptech.glide:glide:${Versions.GLIDE}"
    const val GLIDE_COMPILER             = "com.github.bumptech.glide:compiler:${Versions.GLIDE}"

    const val TIMBER                     = "com.jakewharton.timber:timber:${Versions.TIMBER}"

    const val LOTTIE                     = "com.airbnb.android:lottie:${Versions.LOTTIE}"
    
    const val NAVER_MAP                  = "com.naver.maps:map-sdk:${Versions.NAVER_MAP}"

    const val TED_PERMISSION             = "io.github.ParkSangGwon:tedpermission-normal:${Versions.TED_PERMISSION}"
    const val TED_PERMISSION_COROUTINE   = "io.github.ParkSangGwon:tedpermission-coroutine:${Versions.TED_PERMISSION}"
}

object UnitTest {
    const val JUNIT                      = "junit:junit:${Versions.JUNIT}"
}

object AndroidTest {
    const val ANDROID_JUNIT = "androidx.test.ext:junit:${Versions.ANDROID_JUNIT}"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO_CORE}"
}
