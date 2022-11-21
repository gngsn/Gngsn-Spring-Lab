package com.gngsn.dto

data class ResDTO (
    var statusCode: Number,
    var message: String?
) {
    companion object {
        @kotlin.jvm.JvmStatic
        fun ok(): ResDTO {
            return ResDTO(200, null)
        }
    }
}