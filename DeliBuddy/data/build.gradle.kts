plugins {
    `java-library`
    id(Configs.KOTLIN)
    id(Configs.KOTLIN_KAPT)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    api(project(":domain"))

    implementation(Kotlin.KOTLIN_STDLIB)
    implementation(Kotlin.COROUTINES_CORE)

    implementation(Libraries.RETROFIT)
    implementation(Libraries.RETROFIT_CONVERTER_GSON)
    implementation(Libraries.OKHTTP)
    implementation(Libraries.OKHTTP_LOGGING_INTERCEPTOR)

    implementation(Google.HILT_CORE)
}