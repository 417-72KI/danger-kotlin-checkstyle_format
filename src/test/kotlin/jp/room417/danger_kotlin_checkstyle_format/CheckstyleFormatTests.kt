package jp.room417.danger_kotlin_checkstyle_format

import org.junit.Test
import systems.danger.kotlin.sdk.DangerContext
import java.io.File
import kotlin.io.path.Path
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("NestedLambdaShadowedImplicitParameter")
class CheckstyleFormatTests {
    private lateinit var context: DangerContext

    @BeforeTest
    fun setup() {
        context = MockDangerContext()
        CheckstyleFormat.basePath = Path(System.getProperty("user.dir"))
        CheckstyleFormat.context = context
    }

    @Test
    fun testReport() {
        File(ClassLoader.getSystemResource("checkstyle.xml").toURI()).let {
            CheckstyleFormat.report(it.toPath())
            assertEquals(context.fails.count(), 7)
            context.fails[0].let {
                assertEquals(it.message, "Redundant curly braces")
                assertEquals(it.file, "/path/to/A.kt")
                assertEquals(it.line, 88)
            }
        }
    }

    @Test
    fun testParse() {
        File(ClassLoader.getSystemResource("checkstyle.xml").toURI()).let {
            val checkstyle = CheckstyleFormat.parse(it.toPath())
            assertEquals(checkstyle.files.count(), 3)
            checkstyle.files[0].let {
                assertEquals(it.name, "/path/to/A.kt")
                assertEquals(it.errors.count(), 3)
                it.errors[0].let {
                    assertEquals(it.line, "88")
                    assertEquals(it.column, "20")
                    assertEquals(it.severity, "error")
                    assertEquals(it.message, "Redundant curly braces")
                    assertEquals(it.source, "string-template")
                }
                it.errors[1].let {
                    assertEquals(it.line, "145")
                    assertEquals(it.column, "1")
                    assertEquals(it.severity, "error")
                    assertEquals(it.message, "Needless blank line(s)")
                    assertEquals(it.source, "no-consecutive-blank-lines")
                }
                it.errors[2].let {
                    assertEquals(it.line, "153")
                    assertEquals(it.column, "1")
                    assertEquals(it.message, "Unexpected indentation (24) (should be 20)")
                    assertEquals(it.severity, "error")
                    assertEquals(it.source, "indent")
                }
            }
            checkstyle.files[1].let {
                assertEquals(it.name, "/path/to/B.kt")
                assertEquals(it.errors.count(), 4)
                it.errors[0].let {
                    assertEquals(it.line, "3")
                    assertEquals(it.column, "1")
                    assertEquals(it.message, "Needless blank line(s)")
                    assertEquals(it.severity, "error")
                    assertEquals(it.source, "no-consecutive-blank-lines")
                }
                it.errors[1].let {
                    assertEquals(it.line, "8")
                    assertEquals(it.column, "1")
                    assertEquals(it.message, "Needless blank line(s)")
                    assertEquals(it.severity, "error")
                    assertEquals(it.source, "no-consecutive-blank-lines")
                }
                it.errors[2].let {
                    assertEquals(it.line, "86")
                    assertEquals(it.column, "38")
                    assertEquals(it.message, "Redundant curly braces")
                    assertEquals(it.severity, "error")
                    assertEquals(it.source, "string-template")
                }
                it.errors[3].let {
                    assertEquals(it.line, "150")
                    assertEquals(it.column, "1")
                    assertEquals(it.message, "Unexpected indentation (24) (should be 20)")
                    assertEquals(it.severity, "error")
                    assertEquals(it.source, "indent")
                }
            }
            checkstyle.files[2].let {
                assertEquals(it.name, "/path/to/C.kt")
                assertTrue(it.errors.isEmpty())
            }
        }
        File(ClassLoader.getSystemResource("checkstyle-empty.xml").toURI()).let {
            val checkstyle = CheckstyleFormat.parse(it.toPath())
            assertTrue(checkstyle.files.isEmpty())
        }
    }
}
