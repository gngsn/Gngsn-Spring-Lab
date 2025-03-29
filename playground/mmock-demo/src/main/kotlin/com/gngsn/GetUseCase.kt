package com.gngsn

import com.fasterxml.jackson.core.type.TypeReference

import GetJsonInputPort

class GetUseCase(
    private val overloadInputPort: GetJsonInputPort
) {
    fun getAll(): List<Type1> {
        return overloadInputPort
            .getAs("STORY_TELLER", object: TypeReference<List<Type1>>() {})
            .getOrNull() ?: listOf()
    }
}