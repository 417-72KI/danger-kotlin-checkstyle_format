package jp.room417.danger_kotlin_checkstyle_format.extension

import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.forEachDirectoryEntry
import kotlin.io.path.isDirectory
import kotlin.io.path.notExists

@Throws(IOException::class)
internal fun Path.forEachDirectoryEntryRecursive(glob: String, action: (Path) -> Unit) {
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
