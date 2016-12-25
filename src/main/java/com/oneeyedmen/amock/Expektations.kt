package com.oneeyedmen.amock

import com.natpryce.hamkrest.Matcher
import org.jmock.Expectations
import org.jmock.Sequence
import org.jmock.api.Invocation
import kotlin.reflect.KProperty1

@Suppress("unused")
class Expektations: Expectations() {

    inline fun <reified T: Any?> with(matcher: Matcher<T>): T {
        addParameterMatcher(matcher.asHamcrest())
        return dummyValueOfType<T>(T::class)
    }

    val <T> T.which: WillThunker<T> get() = WillThunker()

    infix fun <T> WillThunker<T>.will(action: TypedAction<T>) = super.will(action)

    infix fun <T> WillThunker<T>.will(block: (Invocation) -> T) = super.will(invoke("invoke a block", block))

    infix fun <T> WillThunker<T>.willDo(block: Invocation.() -> T) = will(block)

    fun Any?.inSequence(sequence: Sequence) = super.inSequence(sequence)

    class WillThunker<T>() // required to make the types work, see http://stackoverflow.com/q/39596420/97777

    fun <T, R> that(mock: T, property: KProperty1<T, R>) = property.get(allowing(mock))

    val <T> T.isProperty: PropertyThunker<T> get() = PropertyThunker()
    class PropertyThunker<T>()

    infix fun <T> PropertyThunker<T>.withValue(value: T) = super.will(com.oneeyedmen.amock.returnValue(value))
}



