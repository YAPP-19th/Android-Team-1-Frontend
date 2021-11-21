plugins{
    `java-library`
    id("kotlin")
    id("kotlin-kapt")
}

java{
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies{
    implementation(Kotlin.KOTLIN_STDLIB)
    implementation(Kotlin.COROUTINES_CORE)

    implementation(Google.HILT_CORE)
}