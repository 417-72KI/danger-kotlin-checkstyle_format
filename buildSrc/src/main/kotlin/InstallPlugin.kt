import org.gradle.api.Project
import java.io.File
import java.nio.file.Paths

fun installPluginFromMavenLocal(project: Project) {
    val artifactId = "danger-kotlin-checkstyle_format"
    val dir = Paths.get("${project.buildDir}/libs")
    val jarFile = dir.resolve("$artifactId-${createVersion(project)}.jar")
        .toFile()
        .takeIf { it.isFile && it.exists() }
    val dangerLibDir = File("/usr/local/lib/danger/libs")
    jarFile?.let {
        val dest = dangerLibDir.resolve("$artifactId.jar")
        it.copyTo(dest, true)
        println("Plugin installed into $dest")
    }
}
