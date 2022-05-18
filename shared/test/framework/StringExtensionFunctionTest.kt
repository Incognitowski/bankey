package framework

import framework.extensionFunctions.parseFromJson
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StringExtensionFunctionTest {

    data class Foo(val bar: String = "test", val baz: Int = 7)

    @Test
    fun `Should properly decode JSON to object`() {
        val lFoo : Foo = """{"bar":"test","baz":7}""".parseFromJson()
        assertEquals("test", lFoo.bar)
        assertEquals(7, lFoo.baz)
    }

}