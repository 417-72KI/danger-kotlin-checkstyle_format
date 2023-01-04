object Dependencies {
    val dangerKotlin by lazy { "systems.danger:danger-kotlin-sdk:${Version.dangerKotlin}" }
    val kotlinStdlib by lazy { "org.jetbrains.kotlin:kotlin-stdlib-jdk8" }
    val jacksonDataformatXml by lazy { "com.fasterxml.jackson.dataformat:jackson-dataformat-xml:${Version.jacksonDataformatXml}" }

    object Version {
        val dangerKotlin by lazy { "1.2" }
        val jacksonDataformatXml by lazy { "2.12.2" }
        val kotlin by lazy { "1.7.20" }
    }
}

object TestDependencies {
    val kotlinTest by lazy { "org.jetbrains.kotlin:kotlin-test" }
    val kotlinTestJunit by lazy { "org.jetbrains.kotlin:kotlin-test-junit" }
}
