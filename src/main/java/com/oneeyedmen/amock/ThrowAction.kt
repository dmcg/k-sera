package com.oneeyedmen.amock

import org.jmock.Expectations
import org.jmock.api.Action

class ThrowAction(delegate: Action) : TypedAction<Nothing> by delegate.asTyped() {
    constructor(exception: Throwable): this(Expectations.throwException(exception))
}