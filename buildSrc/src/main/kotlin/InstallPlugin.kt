import java.io.File
import java.nio.file.Paths
import javax.xml.parsers.DocumentBuilderFactory

fun installPluginFromMavenLocal() {
    val groupId = "io.github.417-72ki"
    val artifactId = "danger-kotlin-checkstyle_format"
    val dir = Paths.get("${System.getProperty("user.home")}/.m2/repository/${groupId.replace(".", "/")}/$artifactId")
    val version = dir.resolve("maven-metadata-local.xml")
        .toFile()
        .takeIf { it.isFile && it.exists() }
        ?.let {
            DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(it)
                .apply { normalize() }
                .let { it.getElementsByTagName("latest").item(0).textContent }
        } ?: return
    val jarFile = dir.resolve("$version/$artifactId-$version.jar").toFile()
        .takeIf { it.isFile && it.exists() }

    val dangerLibDir = File("/usr/local/lib/danger/libs")
    jarFile?.let {
        val dest = dangerLibDir.resolve("$artifactId.jar")
        it.copyTo(dest, true)
        println("Plugin installed into $dest")
    }
}
