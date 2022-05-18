package framework

import framework.extensionFunctions.toJSON
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AnyExtensionFunctionsTest {

    data class Foo(val bar: String = "test", val baz: Int = 7)

    @Test
    fun `Should properly encode object as JSON`() {
        assertEquals("""{"bar":"test","baz":7}""", Foo().toJSON())
    }

}