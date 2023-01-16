@file:Suppress("NestedLambdaShadowedImplicitParameter")

package jp.room417.danger_kotlin_checkstyle_format.extension

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.io.path.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PathTests {
    @get:Rule
    val tmpFolder = TemporaryFolder()

    private val tmpPath
        get() = Path(tmpFolder.root.absolutePath)

    @Test
    fun forEachDirectoryEntryRecursive_simple() {
        val testPattern = arrayOf(
            "build",
            "foo/build",
            "bar/build",
            "baz/qux/build"
        ).also {
            it.map(tmpPath::resolve)
                .forEach { it.createDirectories() }
        }
        val remainingPattern = testPattern.toMutableList()
        tmpPath.forEachDirectoryEntryRecursive("**/build") {
            val relativePath = it.relativeTo(tmpPath).pathString
            assertTrue(remainingPattern.contains(relativePath))
            remainingPattern.remove(relativePath)
        }
        assertEquals(listOf(), remainingPattern)
    }

    @Test
    fun forEachDirectoryEntryRecursive_ktlint() {
        val testPattern = arrayOf(
            "build",
            "foo/build",
            "bar/build",
            "baz/qux/build"
        ).map { "$it/reports/ktlint" }
            .flatMap { path ->
                listOf(
                    "ktlintKotlinScriptCheck",
                    "ktlintMainSourceSetCheck",
                    "ktlintTestSourceSetCheck"
                ).map { "$path/$it/$it.xml" }
            }
            .also {
                it.map(tmpPath::resolve)
                    .forEach {
                        it.parent.createDirectories()
                        it.createFile()
                    }
            }
        val remainingPattern = testPattern.toMutableList()
        tmpPath.forEachDirectoryEntryRecursive(glob = "**/build/reports/ktlint/**/ktlint*.xml") {
            val relativePath = it.relativeTo(tmpPath).pathString
            assertTrue(remainingPattern.contains(relativePath))
            remainingPattern.remove(relativePath)
        }
        assertEquals(listOf(), remainingPattern)
    }
}
