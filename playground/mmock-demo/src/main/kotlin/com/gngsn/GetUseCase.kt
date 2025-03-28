package kotlin.com.gngsn

class GetUseCase(
    val overloadInputPort: GetJsonInputPort
) {
    fun getAll(): List<Type1> {
        return overloadInputPort
            .getAs("STORY_TELLER", object: TypeReference<List<Type1>>() {})
            .getOrNull() ?: listOf()
    }
}