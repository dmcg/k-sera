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
            oneOf(charSequence).get(with(greaterThan(0))).which will returnValue('-')
            oneOf(charSequence).get(with(anything)).which will returnValue('&')
        }
        assertEquals('-', charSequence.get(1))
        assertEquals('*', charSequence.get(0))
        assertEquals('&', charSequence.get(99))
    }

    @Test fun `which-will can take a lambda`() {
        var count = 0
        mockery.expecting {
            allowing(charSequence)[with(isA<Int>())].which will {
                count++
                ' '
            }
        }
        assertEquals(' ', charSequence[0])
        assertEquals(' ', charSequence[1])
        assertEquals(2, count)
    }

    @Test fun `invoke action can take a lambda to do complicated things`() {
        mockery.expecting {
            allowing(charSequence)[with(isA<Int>())].which will invoke("return alternating values") { invocation ->
                val index = invocation.getParameter(0) as Int
                if (index % 2 == 0) '+' else '-'
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

    @Test fun `specify expectations before and after action`() {
        mockery.given {
            allowing(controlPanel.key1).isTurned.which will returnValue(true)
            allowing(controlPanel.key2).isTurned.which will returnValue(true)
        }.whenRunning {
            controlPanel.pressButton()
        }.thenExpect {
            oneOf(controlPanel.missile).launch()
        }
    }

    @Test fun `check we're safe with method references`() {
        mockery.expecting {
            allowing(controlPanel.key1, Key::isTurned).which will returnValue(true)
            allowing(controlPanel.key2, Key::isTurned).which will returnValue(false)
        }.whenRunning {
            controlPanel.pressButton()
        }.thenExpect {
            never(controlPanel.missile).launch()
        }
    }

    @Test fun `*that* works on properties or calls`() {
        mockery.given {
            that(controlPanel.key1, Key::isTurned).isEqual toValue true
            that(controlPanel.key2).isTurned.isEqual toValue true
        }.whenRunning {
            controlPanel.pressButton()
        }.thenExpect {
            oneOf(controlPanel.missile).launch()
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