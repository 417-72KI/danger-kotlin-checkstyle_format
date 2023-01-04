plugins {
    id("org.jetbrains.kotlin.jvm") version Dependencies.Version.kotlin
    id("org.jetbrains.dokka") version Dependencies.Version.kotlin
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(Dependencies.kotlinStdlib)
    implementation(Dependencies.dangerKotlin)
    implementation(Dependencies.jacksonDataformatXml)

    testImplementation(TestDependencies.kotlinTest)
    testImplementation(TestDependencies.kotlinTestJunit)
}
