package com.gngsn

import GetJsonInputPort
import com.fasterxml.jackson.core.type.TypeReference
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class GetUseCaseTest {
    private val getJsonInputPort = mockk<GetJsonInputPort>()
    private val getUseCase = GetUseCase(getJsonInputPort)

    @Test
    fun `getAs 메소드 명 때문에 stubbing 오류 발생`() {
        val typeList = listOf(Type1("먀"))
        val typeRef = object : TypeReference<List<Type1>>() {}
        every {
            getJsonInputPort.getAs(
                code = "STORY_TELLER",
                typeRef = typeRef
            )
        } returns Result.success(typeList)


        val result = getUseCase.getAll()
        assertNotNull(result)
        assertNotNull(result[0])
        assertEquals(result[0].name, "먀")
    }
}