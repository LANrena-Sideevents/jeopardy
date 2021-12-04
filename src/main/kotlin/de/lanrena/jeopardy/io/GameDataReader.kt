package de.lanrena.jeopardy.io

import de.lanrena.jeopardy.model.Category
import de.lanrena.jeopardy.model.Field
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipFile
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.nio.file.Paths
import java.util.function.Supplier

class GameDataReader(gameDataFile: File) {
    var name: String? = null
    var directory: String? = null
    val fields: MutableList<Field> = mutableListOf()
    val categories: MutableList<Category> = mutableListOf()
    val resources: MutableMap<String, Supplier<InputStream>> = mutableMapOf()

    init {
        val archiveFile: ZipFile = ZipFile(gameDataFile)
        archiveFile.entries.toList().forEach {
            zipEntry: ZipArchiveEntry ->
            if (zipEntry.name.endsWith(".json", true)) {
                parseJson(InputStreamReader(archiveFile.getInputStream(zipEntry)))
            } else {
                parseResource(archiveFile, zipEntry)
            }
        }
    }

    private fun parseJson(reader: Reader) {
        val parser = JSONParser()
        val obj: JSONObject = parser.parse(reader) as JSONObject
        name = obj["name"] as String
        directory = obj["directory"] as String

        var columnId: Int = 1
        val boardData = obj["data"] as JSONArray
        for (column in boardData) {
            if (column !is JSONObject) continue

            categories.add(Category(column = columnId,
                    label = column["name"] as String))

            var rowId: Int = 1
            for (cell in column["data"] as JSONArray) {
                if (cell !is JSONObject)
                    continue

                fields.add(Field(row = rowId,
                        col = columnId,
                        question = cell["question"] as String,
                        answer = cell["answer"] as String,
                        bonus = cell["daily"] != null))

                rowId++
            }

            columnId++
        }
    }

    private fun parseResource(archive: ZipFile, entry: ZipArchiveEntry) {
        val key = Paths.get(entry.name).fileName.toString()
        resources[key] = Supplier<InputStream> { archive.getInputStream(entry) }
    }
}
