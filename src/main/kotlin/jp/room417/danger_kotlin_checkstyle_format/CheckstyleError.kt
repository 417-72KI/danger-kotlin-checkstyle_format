package jp.room417.danger_kotlin_checkstyle_format

data class CheckstyleError(
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
        internal fun from(checkstyle: Checkstyle) = checkstyle.files.flatMap { file ->
            file.errors.map { error ->
                CheckstyleError(
                    file = file.name,
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
