import java.util.*

plugins {
    id(Plugins.kotlin) version Plugins.Version.kotlin
    id(Plugins.dokka) version Plugins.Version.dokka
    id(Plugins.ktlint) version Plugins.Version.ktlint
    `java-library`
    `maven-publish`
    id(Plugins.gradleNexusPublishPlugin) version Plugins.Version.gradleNexusPublishPlugin
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

// Publish
val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

val javadocJar by tasks.creating(Jar::class) {
    dependsOn("dokkaJavadoc")
    archiveClassifier.set("javadoc")
    from(tasks.named("dokkaJavadoc"))
}

artifacts {
    archives(sourcesJar)
    archives(javadocJar)
}

group = "io.github.417-72ki"
version = createVersion(project)

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

val properties = project.localProperties ?: Properties().apply {
    setProperty("ossrhUsername", System.getenv("OSSRH_USERNAME"))
    setProperty("ossrhPassword", System.getenv("OSSRH_PASSWORD"))
    setProperty("sonatypeStagingProfileId", System.getenv("SONATYPE_STAGING_PROFILE_ID"))
    setProperty("signing.keyId", System.getenv("SIGNING_KEY_ID"))
    setProperty("signing.password", System.getenv("SIGNING_PASSWORD"))
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(properties.getProperty("ossrhUsername"))
            password.set(properties.getProperty("ossrhPassword"))
            stagingProfileId.set(properties.getProperty("sonatypeStagingProfileId"))
        }
    }
}
