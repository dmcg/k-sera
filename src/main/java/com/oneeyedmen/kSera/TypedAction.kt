package com.oneeyedmen.kSera

import org.hamcrest.Description
import org.jmock.api.Action
import org.jmock.api.Invocation

interface TypedAction<out T> : Action {
    override fun invoke(invocation: Invocation): T
}

fun <T> Action.asTyped(): TypedAction<T> = object: TypedAction<T> {

    @Suppress("UNCHECKED_CAST")
    override fun invoke(invocation: Invocation): T = this@asTyped.invoke(invocation) as T

    override fun describeTo(description: Description) = this@asTyped.describeTo(description)

}
