@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("io.github.ackeecz:danger-kotlin-junit:0.1.0")
@file:DependsOn("danger-kotlin-checkstyle_format.jar")
@file:OptIn(ExperimentalPathApi::class)

import io.github.ackeecz.danger.junit.JUnitPlugin
import jp.room417.danger_kotlin_checkstyle_format.*
import jp.room417.danger_kotlin_checkstyle_format.ktlint.*
import systems.danger.kotlin.*
import java.io.File
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.*

register plugin JUnitPlugin
register plugin CheckstyleFormat

danger(args) {
    val path = Path(System.getProperty("user.dir"))

    path.forEachDirectoryEntryRecursive(glob = "**/build/reports/**/build-error-log.txt") {
        it.useLines { seq ->
            seq.forEach { line ->
                when {
                    line.startsWith("w: ") -> {
                        warn(line.removeRange(0, 2))
                    }
                    line.startsWith("e: ") -> {
                        fail(line.removeRange(0, 2))
                    }
                }
            }
        }
    }
    CheckstyleFormat.basePath = if (System.getenv("CI") == "true") {
        // Path("/home/runner/work/danger-kotlin-checkstyle_format/danger-kotlin-checkstyle_format") // on GitHub Action
        Path("/__w/danger-kotlin-checkstyle_format/danger-kotlin-checkstyle_format") // `container` in GitHub Action
    } else {
        Path(System.getenv("WORKING_DIR") ?: System.getProperty("user.dir"))
    }

    CheckstyleFormat.reportKtlint()

    path.forEachDirectoryEntryRecursive(glob = "**/build/test-results/*/*.xml") {
        JUnitPlugin.parse(File(it.toUri()))
        JUnitPlugin.report()
    }
}

/**
 * Performs the given [action] on each entry in this directory
 * recursively filtered by matching against the specified [glob] pattern.
 *
 * @param glob the globbing pattern. The syntax is specified by the [java.nio.file.FileSystem.getPathMatcher] method.
 * @see Path.forEachDirectoryEntry
 */
@SinceKotlin("1.5")
@Throws(IOException::class)
fun Path.forEachDirectoryEntryRecursive(glob: String, action: (Path) -> Unit) {
    if (notExists()) return
    if (!glob.contains("/")) return forEachDirectoryEntry(glob, action)
    when (val subdir = glob.split("/").first()) {
        "*" -> forEachDirectoryEntry(glob = "*") {
            if (it.isDirectory())
                it.forEachDirectoryEntryRecursive(glob.removePrefix("$subdir/"), action)
        }
        "**" -> {
            forEachDirectoryEntryRecursive(glob.removePrefix("$subdir/"), action)
            forEachDirectoryEntry(glob = "**") {
                if (it.isDirectory())
                    it.forEachDirectoryEntryRecursive(glob, action)
            }
        }
        else -> resolve(subdir)
            .forEachDirectoryEntryRecursive(glob.removePrefix("$subdir/"), action)
    }
}
