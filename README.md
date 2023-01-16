# danger-kotlin-checkstyle_format

![publish](https://github.com/417-72KI/danger-kotlin-checkstyle_format/actions/workflows/publish.yml/badge.svg)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.417-72ki/danger-kotlin-checkstyle_format/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/io.github.417-72ki/danger-kotlin-checkstyle_format)

[Danger-Kotlin](https://github.com/danger/kotlin) plugin for processing checkstyle formatted xml file.

## Installation

Put

```kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("io.github.417-72ki:danger-kotlin-checkstyle_format:0.1.0")
```

to the top of your `Dangerfile.df.kts`

## Usage

```kotlin
// Required to register the plugin
register plugin CheckstyleFormat

danger(args) {
    // You should run `./gradlew KtlintCheck` with `checkstyle` reporter before executing `Danger-Kotlin`.
    CheckstyleFormat.reportKtlint()
}
```

See [Dangerfile.df.kts](https://github.com/417-72KI/danger-kotlin-checkstyle_format/blob/main/Dangerfile.df.kts) for more specific usage.
