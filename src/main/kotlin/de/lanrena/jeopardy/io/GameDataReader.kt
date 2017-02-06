package de.lanrena.jeopardy.io

import de.lanrena.jeopardy.model.Category
import de.lanrena.jeopardy.model.Field
import de.lanrena.jeopardy.model.Question
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipFile
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.File
import java.io.InputStreamReader
import java.io.Reader
import java.nio.file.Path

class GameDataReader(gameDataFile: File) {
    var name: String? = null
    var directory: String? = null
    val gameData: Path = gameDataFile.toPath()
    val fields: MutableList<Field> = mutableListOf()
    val categories: MutableList<Category> = mutableListOf()

    init {
        val archiveFile: ZipFile = ZipFile(gameDataFile)
        for (zipEntry: ZipArchiveEntry in archiveFile.entries) {
            if (zipEntry.name.endsWith(".json", true)) {
                val reader: Reader = InputStreamReader(archiveFile.getInputStream(zipEntry))
                val parser = JSONParser()
                val obj: JSONObject = parser.parse(reader) as JSONObject
                name = obj["name"] as String
                directory = obj["directory"] as String

                var columnId: Int = 1
                val boardData = obj["data"] as JSONArray
                for (column in boardData) {
                    if (column !is JSONObject)
                        continue

                    categories.add(Category(column = columnId,
                            label = column["name"] as String))

                    var rowId: Int = 1
                    for (cell in column["data"] as JSONArray) {
                        if (cell !is JSONObject)
                            continue

                        fields.add(Field(row = rowId,
                                col = columnId,
                                question = Question(cell["question"] as String),
                                bonus = false))

                        rowId++
                    }

                    columnId++
                }
            }
        }
    }
}
