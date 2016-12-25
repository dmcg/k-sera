package com.oneeyedmen.amock

import com.natpryce.hamkrest.anything
import com.natpryce.hamkrest.greaterThan
import com.natpryce.hamkrest.isA
import org.jmock.integration.junit4.JUnitRuleMockery
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails


//README_TEXT
class AmockExampleTests {

    // Create a mockery using the usual JMock rule
    @Rule @JvmField val mockery = JUnitRuleMockery()

    // and a mock from the mockery
    val charSequence = mockery.mock<CharSequence>()

    @Test fun `expect a call to a mock via the mockery`() {
        mockery.expecting {
            oneOf(charSequence).length
        }
        // in this case JMock returns a default value
        assertEquals(0, charSequence.length)
    }

    @Test fun `you can specify a (type-checked) return value for a call`() {
        mockery.expecting {
            oneOf(charSequence).length.which will returnValue(42)
        }
        assertEquals(42, charSequence.length)

        // oneOf(charSequence).length.which will returnValue("banana") doesn't compile
    }

    @Test fun `simulate failures with throwException`() {
        mockery.expecting {
            oneOf(charSequence).length.which will throwException(RuntimeException("deliberate"))
        }
        assertFails {
            charSequence.length
        }
    }

    @Test fun `match parameters with Hamkrest`() {
        mockery.expecting {
            oneOf(charSequence).get(0).which will returnValue('*')
            oneOf(charSequence)[with(greaterThan(0))].which will returnValue('-')
            oneOf(charSequence)[with(anything)].which will returnValue('&')
        }
        assertEquals('-', charSequence.get(1))
        assertEquals('*', charSequence.get(0))
        assertEquals('&', charSequence.get(99))
    }

    @Test fun `which-will can take a block and access the call invocation`() {
        mockery.expecting {
            allowing(charSequence)[with(anything)].which will { invocation ->
                if (invocation.getParameter(0) as Int % 2 == 0) '+' else '-'
            }
        }
        assertEquals('+', charSequence[42])
        assertEquals('-', charSequence[43])
        assertEquals('-', charSequence[-1])
    }

    // Shall we play a game?

    val controlPanel = Panel(
        mockery.mock<Key>("key1"),
        mockery.mock<Key>("key2"),
        mockery.mock<Missile>())


    @Test fun `block syntax for expecting-during-verify`() =
        mockery {
            expecting {
                allowing(controlPanel.key1).isTurned.which will returnValue(true)
                allowing(controlPanel.key2).isTurned.which will returnValue(true)
            }
            during {
                controlPanel.pressButton()
            }
            verify {
                oneOf(controlPanel.missile).launch()
            }
        }

    @Test fun `given-that allows property references`() =
        mockery {
            given {
                that(controlPanel.key1, Key::isTurned).isProperty withValue (true)
                that(controlPanel.key2, Key::isTurned).isProperty withValue (false)
            }
            during {
                controlPanel.pressButton()
            }
            verify {
                never(controlPanel.missile).launch()
            }
        }

}
//README_TEXT

interface Key {
    val isTurned: Boolean
}

interface Missile {
    fun launch()
}

class Panel(val key1: Key, val key2: Key, val missile: Missile) {
    fun pressButton() {
        if (key1.isTurned && key2.isTurned)
            missile.launch()
    }
}