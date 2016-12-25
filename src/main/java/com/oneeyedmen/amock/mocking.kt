package com.oneeyedmen.amock

import org.jmock.Expectations
import org.jmock.Mockery
import org.jmock.Sequence
import org.jmock.api.Action

inline fun <reified T: Any> Mockery.mock(): T = mock(T::class.java)
inline fun <reified T: Any> Mockery.mock(name: String): T = mock(T::class.java, name)

fun Mockery.expecting(expectations: Expektations.() -> Unit) = this.apply {
    checking(Expektations().apply(expectations))
}

operator fun Mockery.invoke(init: BetterSyntax.() -> Unit) {
    BetterSyntax(this).init()
    assertIsSatisfied()
}

class BetterSyntax(private val mockery: Mockery) {
    private var block: (() -> Unit)? = null

    fun during(block: () -> Unit) {
        this.block = block
    }

    fun expecting(expectations: Expektations.() -> Unit) {
        mockery.expecting(expectations)
        consumeBlock()
    }

    fun verify(expectations: Expektations.() -> Unit)  = expecting(expectations)
    fun given(expectations: Expektations.() -> Unit)  = expecting(expectations)

    private fun consumeBlock() {
        block?.let {
            it()
        }
        block = null
    }

}






