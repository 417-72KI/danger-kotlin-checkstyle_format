object Dependencies {
    val dangerKotlin by lazy { "systems.danger:danger-kotlin-sdk:${Version.dangerKotlin}" }
    val kotlinStdlib by lazy { "org.jetbrains.kotlin:kotlin-stdlib-jdk8" }
    val jacksonDataformatXml by lazy { "com.fasterxml.jackson.dataformat:jackson-dataformat-xml:${Version.jacksonDataformatXml}" }

    object Version {
        val dangerKotlin by lazy { "1.2" }
        val jacksonDataformatXml by lazy { "2.14.1" }
        val kotlin by lazy { "1.7.20" }
    }
}

object TestDependencies {
    val kotlinTest by lazy { "org.jetbrains.kotlin:kotlin-test" }
    val kotlinTestJunit by lazy { "org.jetbrains.kotlin:kotlin-test-junit" }
}

object Plugins {
    val kotlin by lazy { "org.jetbrains.kotlin.jvm" }
    val dokka by lazy { "org.jetbrains.dokka" }
    val ktlint by lazy { "org.jlleitschuh.gradle.ktlint" }
    val gradleNexusPublishPlugin by lazy { "io.github.gradle-nexus.publish-plugin" }


    object Version {
        val kotlin by lazy { Dependencies.Version.kotlin }
        val dokka by lazy { kotlin }
        val ktlint by lazy { "11.0.0" }
        val gradleNexusPublishPlugin by lazy { "1.1.0" }
    }
}
