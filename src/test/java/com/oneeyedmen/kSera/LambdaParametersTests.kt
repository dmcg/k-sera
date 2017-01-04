package com.oneeyedmen.kSera

import org.jmock.api.ExpectationError
import org.jmock.integration.junit4.JUnitRuleMockery
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.fail

// https://www.reddit.com/r/Kotlin/comments/54ea7s/i_suspect_that_the_set_of_people_using_kotlin_and/d86w1m6
class LambdaParametersTests {

    @Rule @JvmField val mockery = JUnitRuleMockery()
    val mock = mockery.mock<UnderTest>()

    @Test fun lambda_in_val() {
        val lambda: (Int) -> String = { it.toString() }
        mockery.expecting {
            oneOf(mock).f(lambda).which will returnValue("Result")
        }
        assertEquals("Result", mock.f(lambda))
    }


    @Test fun method_reference() {
        mockery.expecting {
            oneOf(mock).f(Int::toString).which will returnValue("Result")
        }
        assertEquals("Result", mock.f(Int::toString))
    }


    @Ignore("Doesn't work, but it's very hard to persuade the mockery not to fail the test")
    @Test fun lambda_literal() {
        mockery.expecting {
            oneOf(mock).f({ it.toString() }).which will returnValue("Result")
        }
        mock.f({ it.toString() })
    }

}

interface UnderTest {
    fun f(lambda: (Int) -> String): String
}

