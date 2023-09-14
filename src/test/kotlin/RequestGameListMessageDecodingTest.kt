import de.lanrena.jeopardy.view.JsonMessage
import de.lanrena.jeopardy.view.RequestGameList
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RequestGameListMessageDecodingTest {

    private val json = Json {
        prettyPrint = true
    }

    @Test
    fun asdf() {
        val expected = """
            {
                "type": "RequestGameList"
            }
        """.trimIndent()

        val input = RequestGameList

        val encoded = json.encodeToString(JsonMessage.serializer(), input)

        assertEquals(expected, encoded)
    }
}