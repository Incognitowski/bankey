package parameter

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class ParameterListenersTest {

    @Test
    fun `Should properly register listener`() {
        val lListener: (ParameterEntity) -> Unit = {}
        val lListenerId = ParameterListeners.register(lListener)
        assertNotNull(ParameterListeners.getListenerById(lListenerId))
        assertEquals(lListener, ParameterListeners.getListenerById(lListenerId))
    }

    @Test
    fun `Should raise exception when trying to find listener with non-existent ID`() {
        assertThrows<IllegalStateException> {
            ParameterListeners.getListenerById(-1)
        }
    }

    @Test
    fun `Should clear listeners with clearListeners() method`() {
        val lListener: (ParameterEntity) -> Unit = {}
        ParameterListeners.register(lListener)
        ParameterListeners.clearListeners()
        assertEquals(0, ParameterListeners.getAllListeners().size)
    }

    @Test
    fun `Should properly invoke listener`() {
        var lMethodWasInvoked = false
        val lListener: (ParameterEntity) -> Unit = {
            lMethodWasInvoked = true
        }
        ParameterListeners.register(lListener)
        ParameterListeners.notify(ParameterEntity())
        assertEquals(true, lMethodWasInvoked)
    }

    @Test
    fun `Should properly remove registered listener`() {
        val lListener: (ParameterEntity) -> Unit = {}
        val lListenerId = ParameterListeners.register(lListener)
        ParameterListeners.unregister(lListenerId)
        assertEquals(0, ParameterListeners.getAllListeners().size)
    }

}