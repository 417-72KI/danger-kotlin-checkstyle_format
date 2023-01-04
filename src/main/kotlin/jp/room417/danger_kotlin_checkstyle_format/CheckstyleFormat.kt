package jp.room417.danger_kotlin_checkstyle_format

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import systems.danger.kotlin.sdk.DangerPlugin
import java.nio.file.Path
import kotlin.io.path.inputStream
import kotlin.io.path.notExists

object CheckstyleFormat : DangerPlugin() {
    override val id: String = "danger-kotlin-checkstyle_format"

    @Suppress("unused")
    fun report(vararg paths: Path, inlineMode: Boolean = true) {
        paths.forEach { report(it, inlineMode) }
    }

    @Suppress("unused")
    fun report(paths: List<Path>, inlineMode: Boolean = true) {
        report(paths = paths.toTypedArray(), inlineMode = inlineMode)
    }

    internal fun report(path: Path, inlineMode: Boolean = true) {
        if (path.notExists()) {
            throw IllegalArgumentException("File not exists: ($path)")
        }

        val checkstyle = parse(path)
        val errors = CheckstyleError.from(checkstyle)
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
