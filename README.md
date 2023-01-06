# danger-kotlin-checkstyle_format

![publish](https://github.com/417-72KI/danger-kotlin-checkstyle_format/actions/workflows/publish.yml/badge.svg)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.417-72ki/danger-kotlin-checkstyle_format/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/io.github.417-72ki/danger-kotlin-checkstyle_format)

[Danger-Kotlin](https://github.com/danger/kotlin) plugin for processing checkstyle formatted xml file.

## Installation

Put

```kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("io.github.417-72ki:danger-kotlin-checkstyle_format:0.0.2")
```

to the top of your `Dangerfile.df.kts`

## Usage

```kotlin
// Required to register the plugin
register plugin CheckstyleFormat

danger(args) {
    // In this sample, you should run `./gradlew KtlintCheck` before executing `Danger-Kotlin`.
    FileSystems.getDefault()
        .getPathMatcher("glob:**/build/reports/ktlint/**/ktlint*.xml")
        .let {
            Files.walk(Path(System.getProperty("user.dir")))
                .filter(it::matches)
                .forEach(CheckstyleFormat::report)
        }
}
```

See [Dangerfile.df.kts](https://github.com/417-72KI/danger-kotlin-checkstyle_format/blob/main/Dangerfile.df.kts) for specific usage.
