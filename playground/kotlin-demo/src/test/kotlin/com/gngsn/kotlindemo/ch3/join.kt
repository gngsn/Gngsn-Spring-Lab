package strings

fun <T> joinToString(
    collection: Collection<T>,
    sep: String,
    prefix: String,
    postfix: String
): String {
    val result = StringBuilder(prefix)

    for ((index, element) in collection.withIndex()) {
        if (index > 0) result.append(sep)
        result.append(element)
    }

    result.append(postfix)
    return result.toString()
}