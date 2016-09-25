
Amock
=====

A JMock wrapper for Kotlin.

[AmockExampleTests](src/test/java/com/oneeyedmen/amock/AmockExampleTests.kt)
shows how to write a test.

```kotlin
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
        }
        assertEquals('-', charSequence.get(1))
        assertEquals('*', charSequence.get(0))
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

    val panel = Panel(mockery.mock<Key>("k1"), mockery.mock<Key>("k2"), mockery.mock<Missile>())

    @Test fun `specify expectations before and after action`() {
        mockery.given {
            allowing(panel.key1).isTurned.which will returnValue(true)
            allowing(panel.key2).isTurned.which will returnValue(true)
        }.whenRunning {
            panel.pressButton()
        }.thenExpect {
            oneOf(panel.missile).launch()
        }
    }

    @Test fun `better check we're safe`() {
        mockery.given {
            allowing(panel.key1).isTurned.which will returnValue(true)
            allowing(panel.key2).isTurned.which will returnValue(false)
        }.whenRunning {
            panel.pressButton()
        }.thenExpect {
            never(panel.missile).launch()
        }
    }
}
```

Amock is available at Maven central.

