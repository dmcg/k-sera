
K-Sera
=====

A JMock wrapper for Kotlin.

[KSeraExampleTests](src/test/java/com/oneeyedmen/kSera/KSeraExampleTests.kt)
shows how to write a test.

```kotlin
class KSeraExampleTests {

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

    @Test fun `special operators work well`() {
        mockery.expecting {
            allowing(charSequence)[0].which will returnValue('*')
        }
        assertEquals('*', charSequence.get(0))
    }

    @Test fun `match parameters with Hamkrest`() {
        mockery.expecting {
            allowing(charSequence)[with(lessThan(0))].which will returnValue('-')
            allowing(charSequence)[with(anything)].which will returnValue('+')
        }
        assertEquals('-', charSequence.get(-1))
        assertEquals('+', charSequence.get(99))
    }

    @Test fun `which-will can take a block and access the call invocation`() {
        mockery.expecting {
            allowing(charSequence)[with(anything)].which will { invocation ->
                (invocation.getParameter(0) as Int).toChar()
            }
        }
        assertEquals(' ', charSequence[32])
        assertEquals('A', charSequence[65])
    }

    @Test fun `you can mock functions too`() {
        val f = mockery.mock<(Any?) -> String>()
        mockery.expecting {
            allowing(f).invoke(with(anything)).which will returnValue("banana")
        }
        assertEquals("banana", f("ignored"))
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
```

k-sera is available at Maven central.

