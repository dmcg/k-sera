package com.oneeyedmen.amock

import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher
import org.hamcrest.Description
import org.jmock.Expectations
import org.jmock.Sequence
import kotlin.reflect.KClass

@Suppress("unused")
class Expektations: Expectations() {

    inline fun <reified T: Any?> with(matcher: Matcher<T>): T {
        addParameterMatcher(matcher.asHamcrest())
        return dummyValueOfType(T::class)
    }

    val <T> T.which: WillThunker<T> get() = WillThunker()

    infix fun <T> WillThunker<T>.will(action: TypedAction<T>) = super.will(action)

    infix fun <T> WillThunker<T>.will(block: () -> T) = super.will(invoke("invoke a block", block))

    fun Any?.inSequence(sequence: Sequence) = super.inSequence(sequence)

    @Suppress("unused")
    class WillThunker<T>() // required to make the types work, see http://stackoverflow.com/q/39596420/97777



}

// Yuk yuk yuk yuk yuk
@Suppress("UNCHECKED_CAST", "PLATFORM_CLASS_MAPPED_TO_KOTLIN")
fun <T: Any?> dummyValueOfType(type: KClass<*>): T {
    // See http://stackoverflow.com/q/33987746/97777
    return when(type.java) {
        java.lang.Boolean::class.java-> false as T
        java.lang.Character::class.java-> '\u0000' as T
        else -> (if (Number::class.java.isAssignableFrom(type.java)) 0 else null) as T
    }
}

fun <T: Any?> Matcher<T>.asHamcrest(): org.hamcrest.Matcher<T> {
    return object : org.hamcrest.BaseMatcher<T>() {
        @Suppress("UNCHECKED_CAST")
        override fun matches(item: Any?): Boolean {
            return this@asHamcrest.invoke(item as T) is MatchResult.Match
        }

        override fun describeTo(description: Description) {
            description.appendText(this@asHamcrest.description)
        }
    }
}

