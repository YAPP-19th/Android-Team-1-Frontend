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
    implementation(Kotlin.KOTLIN_STDLIB)
    implementation(Kotlin.COROUTINES_CORE)

    implementation(Google.HILT_CORE)
}