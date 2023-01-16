import java.util.*

plugins {
    id(Plugins.kotlin) version Plugins.Version.kotlin
    id(Plugins.dokka) version Plugins.Version.dokka
    id(Plugins.ktlint) version Plugins.Version.ktlint
    `java-library`
    `maven-publish`
    signing
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

ktlint {
    arrayOf(
        "import-ordering",
        "no-wildcard-imports"
    ).forEach(disabledRules::add)
    reporters {
        ignoreFailures.set(true)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
    }
}

tasks.register("printVersion") {
    doLast {
        println(createVersion(project))
    }
}

tasks.register("releaseTag") {
    doFirst {
        if ("git symbolic-ref --short HEAD".runCommand().output != "main") {
            throw GradleException("This task must be run in main branch")
        }
        "git pull --rebase --tags".runCommand()
    }
    doLast {
        var version = createVersion(rootProject)
            .replaceFirst("-SNAPSHOT", "")
        if (!version.startsWith("v")) {
            version = "v$version"
        }
        val versionList = "git tag".runCommand().output.split("\n")
        if (versionList.contains(version))
            throw GradleException("`$version` already exists.")

        println("Create release `$version`")

        "gh release create $version --target main --generate-notes".runCommand().exitCode.let {
            if (it != 0) throw GradleException("invalid return code on `gh release create`: $it")
        }
    }
}

tasks.register("installPluginFromLocal") {
    installPluginFromLocal(rootProject)
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
            artifact(sourcesJar)
            artifact(javadocJar)

            project.getProperties("pom.properties")?.let { properties ->
                pom {
                    name.set(properties.getProperty("ARTIFACT_ID"))
                    description.set(properties.getProperty("DESCRIPTION"))
                    url.set(properties.getProperty("URL"))

                    licenses {
                        license {
                            name.set(properties.getProperty("LICENSE_NAME"))
                            url.set(properties.getProperty("LICENSE_URL"))
                        }
                    }
                    developers {
                        developer {
                            id.set(properties.getProperty("DEVELOPER_ID"))
                            name.set(properties.getProperty("DEVELOPER_NAME"))
                            email.set(properties.getProperty("DEVELOPER_EMAIL"))
                        }
                    }
                    scm {
                        connection.set(properties.getProperty("SCM_CONNECTION"))
                        developerConnection.set(properties.getProperty("SCM_DEVELOPER_CONNECTION"))
                        url.set(properties.getProperty("SCM_URL"))
                    }
                }
            }
        }
    }
}

val properties = project.localProperties ?: Properties().apply {
    System.getenv("OSSRH_USERNAME")?.let {
        setProperty("ossrhUsername", it)
    }
    System.getenv("OSSRH_PASSWORD")?.let {
        setProperty("ossrhPassword", it)
    }
    System.getenv("SONATYPE_STAGING_PROFILE_ID")?.let {
        setProperty("sonatypeStagingProfileId", it)
    }
    System.getenv("SIGNING_KEY_ID")?.let {
        setProperty("signing.keyId", it)
    }
    System.getenv("SIGNING_KEY")?.let {
        setProperty("signing.key", it)
    }
    System.getenv("SIGNING_PASSWORD")?.let {
        setProperty("signing.password", it)
    }
}

signing {
    useInMemoryPgpKeys(
        properties.getProperty("signing.keyId"),
        properties.getProperty("signing.key"),
        properties.getProperty("signing.password")
    )
    sign(publishing.publications)
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
