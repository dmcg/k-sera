package com.oneeyedmen.amock

import org.jmock.Expectations
import org.jmock.Mockery
import org.jmock.Sequence
import org.jmock.api.Action

inline fun <reified T: Any> Mockery.mock(): T = mock(T::class.java)
inline fun <reified T: Any> Mockery.mock(name: String): T = mock(T::class.java, name)

fun org.jmock.Mockery.given(expectations: Expektations.() -> Unit): Mockery = expecting(expectations)

fun Mockery.expecting(expectations: Expektations.() -> Unit): Mockery {
    this.checking(Expektations().apply(expectations))
    return this
}

fun Mockery.whenRunning(block: () -> Unit) = ThenClause(this, block)

class ThenClause(private val mockery: Mockery, private val block: () -> Unit) {
    fun thenExpect(expectations: Expektations.() -> Unit) {
        mockery.expecting(expectations)
        block()
        mockery.assertIsSatisfied()
    }
}





