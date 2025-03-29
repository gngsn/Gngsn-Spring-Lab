import com.fasterxml.jackson.core.type.TypeReference

interface GetJsonInputPort {
    fun <T> getAs(code: String, clazz: Class<T>): Result<T?>
    fun <T> getAs(code: String, typeRef: TypeReference<T>): Result<T?>
}
