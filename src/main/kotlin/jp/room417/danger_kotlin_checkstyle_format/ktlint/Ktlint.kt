package jp.room417.danger_kotlin_checkstyle_format.ktlint

import jp.room417.danger_kotlin_checkstyle_format.CheckstyleFormat
import jp.room417.danger_kotlin_checkstyle_format.extension.forEachDirectoryEntryRecursive
import java.nio.file.Path
import kotlin.io.path.Path

@Suppress("unused")
fun CheckstyleFormat.reportKtlint(path: Path = Path(System.getProperty("user.dir"))) {
    path.forEachDirectoryEntryRecursive(glob = "**/build/reports/ktlint/**/ktlint*.xml") {
        report(it)
    }
}
