plugins {
    id(Plugins.kotlin) version Plugins.Version.kotlin
    id(Plugins.dokka) version Plugins.Version.dokka
    id(Plugins.ktlint) version Plugins.Version.ktlint
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

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    arrayOf(
        "import-ordering",
        "no-wildcard-imports"
    ).forEach(disabledRules::add)
    reporters {
        ignoreFailures.set(true)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
    }
}
