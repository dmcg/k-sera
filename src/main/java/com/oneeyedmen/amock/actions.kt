package com.oneeyedmen.amock

import org.hamcrest.Description
import org.jmock.Expectations
import org.jmock.api.Action
import org.jmock.api.Invocation

fun <R> returnValue(value: R): TypedAction<R> = ReturnAction(value)

fun <T: Throwable> throwException(x: T): TypedAction<Nothing> = ThrowAction(x)

fun <R> invoke(name: String, lambda: (Invocation) -> R) = InvokeAction(name, lambda)

fun <R> invoke(name: String, lambda: () -> R) = InvokeAction(name, lambda)


