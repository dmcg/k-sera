package com.oneeyedmen.kSera

import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher
import org.hamcrest.Description


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
