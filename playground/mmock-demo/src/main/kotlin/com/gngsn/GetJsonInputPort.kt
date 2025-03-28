import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper

interface GetJsonInputPort {
    fun <T> getAs(code: String, clazz: Class<T>): Result<T?>
    fun <T> getAs(code: String, typeRef: TypeReference<T>): Result<T?>
}

data class Type1(
    val name: String
)

class GetJsonUseCase: GetJsonInputPort {
    override fun <T> getAs(code: String, clazz: Class<T>): Result<T?> {
        return kotlin.runCatching {
            null
        }
    }

    override fun <T> getAs(code: String, typeRef: TypeReference<T>): Result<T?> {
        val data = "[{\"name\": \"먀\"}, {\"name\": \"묘\"}]".toByteArray()

        return kotlin.runCatching {
            ObjectMapper().readValue(data, typeRef)
        }
    }
}

class GetUseCase(
    val overloadInputPort: GetJsonInputPort
) {
    fun getAll(): List<Type1> {
        return overloadInputPort
            .getAs("STORY_TELLER", object: TypeReference<List<Type1>>() {})
            .getOrNull() ?: listOf()
    }
}