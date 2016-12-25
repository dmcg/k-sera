package com.oneeyedmen.amock

import org.jmock.api.Invocation
import org.jmock.lib.action.CustomAction

class InvokeAction<out R>(private val description: String, private val block: (Invocation) -> R)
: TypedAction<R> by UntypedInvokeAction(description, block).asTyped()


class UntypedInvokeAction<out R>(description: String, private val lambda: (Invocation) -> R) : CustomAction(description) {

    override fun invoke(invocation: Invocation): R? {
        val result = lambda(invocation)
        return if (result == Unit) null else result // Java reflection doesn't understand Unit
    }
}

