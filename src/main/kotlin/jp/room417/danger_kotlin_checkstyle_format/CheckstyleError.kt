package jp.room417.danger_kotlin_checkstyle_format

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.relativeTo

internal data class CheckstyleError(
    val file: String,
    val line: Int,
    val column: Int,
    val severity: Severity,
    val message: String,
    val source: String
) {
    enum class Severity {
        WARNING, ERROR
    }

    companion object {
        fun from(basePath: Path, checkstyle: Checkstyle) = checkstyle.files.flatMap { file ->
            file.errors.map { error ->
                CheckstyleError(
                    file = file.name.let {
                        if (Path(it).startsWith(basePath)) {
                            Path(it).relativeTo(basePath).toString()
                        } else {
                            it
                        }
                    },
                    line = error.line.toInt(),
                    column = error.column.toInt(),
                    severity = Severity.valueOf(error.severity.uppercase()),
                    message = error.message,
                    source = error.source
                )
            }
        }
    }
}
