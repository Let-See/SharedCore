package io.github.letsee.implementations

import io.github.letsee.interfaces.FileNameCleaner
import java.io.File

actual class JSONFileNameCleaner: FileNameCleaner {
    override fun clean(filePath: String): String {
        return File(filePath).nameWithoutExtension
    }
}