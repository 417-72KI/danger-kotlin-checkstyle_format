package jp.room417.danger_kotlin_checkstyle_format

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import systems.danger.kotlin.sdk.DangerPlugin
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.inputStream
import kotlin.io.path.notExists

/**
 * [Danger-Kotlin](https://github.com/danger/kotlin) plugin for checkstyle formatted xml file.
 */
object CheckstyleFormat : DangerPlugin() {
    override val id: String = "danger-kotlin-checkstyle_format"

    /**
     * Base path of `name` attributes in `file` tag.
     *
     * If the attributes and where `Danger-Kotlin`'s working directory is different (e.g. Run `Danger-Kotlin` via Docker),
     * You should modify this property.
     */
    var basePath = Path(System.getProperty("user.dir"))

    /**
     * Convert checkstyle errors to `Danger-Kotlin` warnings / errors
     *
     * @param paths Checkstyle formatted XML file paths
     * @param inlineMode If true, it makes comments as inline in Pull requests.
     */
    @Suppress("unused")
    fun report(vararg paths: Path, inlineMode: Boolean = true) {
        paths.forEach { report(it, inlineMode) }
    }

    /**
     * Convert checkstyle errors to `Danger-Kotlin` warnings / errors
     *
     * @param paths Checkstyle formatted XML file paths
     * @param inlineMode If true, it makes comments as inline in Pull requests.
     */
    @Suppress("unused")
    fun report(paths: List<Path>, inlineMode: Boolean = true) {
        report(paths = paths.toTypedArray(), inlineMode = inlineMode)
    }

    internal fun report(path: Path, inlineMode: Boolean = true) {
        if (path.notExists()) {
            throw IllegalArgumentException("File not exists: ($path)")
        }

        val checkstyle = parse(path)
        val errors = CheckstyleError.from(basePath, checkstyle)
        sendComment(errors, inlineMode)
    }

    internal fun parse(path: Path): Checkstyle = path.inputStream().use {
        XmlMapper().readValue(it, Checkstyle::class.java)
    }

    private fun sendComment(errors: List<CheckstyleError>, inlineMode: Boolean) {
        if (inlineMode) {
            errors.forEach {
                when (it.severity) {
                    CheckstyleError.Severity.ERROR -> context.fail(
                        message = it.message,
                        file = it.file,
                        line = it.line
                    )
                    CheckstyleError.Severity.WARNING -> context.warn(
                        message = it.message,
                        file = it.file,
                        line = it.line
                    )
                }
            }
        } else {
            errors.forEach {
                when (it.severity) {
                    CheckstyleError.Severity.ERROR -> context.fail(message = it.message)
                    CheckstyleError.Severity.WARNING -> context.warn(message = it.message)
                }
            }
        }
    }
}
