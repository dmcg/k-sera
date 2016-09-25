package com.oneeyedmen.amock

import org.jmock.Expectations
import org.jmock.api.Action

class ReturnAction<R>(delegate: Action) : TypedAction<R> by delegate.asTyped() {
    constructor(value: R) : this(Expectations.returnValue(value))
}