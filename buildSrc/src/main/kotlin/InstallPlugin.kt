import org.gradle.api.Project
import java.io.File
import java.nio.file.Paths

fun installPluginFromLocal(project: Project) {
    val artifactId = "danger-kotlin-checkstyle_format"
    val dir = Paths.get("${project.buildDir}/libs")
    val jarFile = dir.resolve("$artifactId-${createVersion(project)}.jar")
        .toFile()
        .also { println("$it: ${it.exists()}") }
        .takeIf { it.isFile && it.exists() }
    val dangerLibDir = File("/usr/local/lib/danger/libs")
    if (!dangerLibDir.exists()) {
        dangerLibDir.mkdirs()
    }
    jarFile?.let {
        val dest = dangerLibDir.resolve("$artifactId.jar")
        it.copyTo(dest, true)
        println("Plugin installed into $dest")
    }
}
