package com.gngsn
import com.fasterxml.jackson.databind.ObjectMapper

import GetJsonInputPort
import com.fasterxml.jackson.core.type.TypeReference


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