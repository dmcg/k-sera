package com.oneeyedmen.kSera

import org.jmock.api.Invocation

fun <R> returnValue(value: R): TypedAction<R> = ReturnAction(value)

fun <T: Throwable> throwException(x: T): TypedAction<Nothing> = ThrowAction(x)

fun <R> invoke(name: String, lambda: (Invocation) -> R) = InvokeAction(name, lambda)
