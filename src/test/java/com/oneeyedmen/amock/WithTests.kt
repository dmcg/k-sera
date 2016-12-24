@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

package com.oneeyedmen.amock

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.greaterThan
import org.jmock.api.Invocation
import org.jmock.integration.junit4.JUnitRuleMockery
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals


class WithTests {

    @Rule @JvmField val mockery = JUnitRuleMockery()
    val sut = mockery.mock<TestSubject>()

    @Test fun int() {
        mockery.expecting {
            allowing(sut).returnInt(with(equalTo(42))).which will returnFirstParameter()
        }
        assertEquals(42, sut.returnInt(42))
    }

    @Test fun long() {
        mockery.expecting {
            allowing(sut).returnLong(with(equalTo(42.toLong()))).which will returnFirstParameter()
        }
        assertEquals(42, sut.returnLong(42.toLong()))
    }

    @Test fun double() {
        mockery.expecting {
            allowing(sut).returnDouble(with(equalTo(42.toDouble()))).which will returnFirstParameter()
        }
        assertEquals(42.toDouble(), sut.returnDouble(42.toDouble()))
    }

    @Test fun byte() {
        mockery.expecting {
            allowing(sut).returnByte(with(equalTo(42.toByte()))).which will returnFirstParameter()
        }
        assertEquals(42, sut.returnByte(42.toByte()))
    }

    @Test fun boolean() {
        mockery.expecting {
            allowing(sut).returnBoolean(with(equalTo(true))).which will returnFirstParameter()
        }
        assertEquals(true, sut.returnBoolean(true))
    }

    @Test fun char() {
        mockery.expecting {
            allowing(sut).returnChar(with(equalTo(' '))).which will returnFirstParameter()
        }
        assertEquals(' ', sut.returnChar(' '))
    }

    @Test fun string() {
        mockery.expecting {
            allowing(sut).returnString(with(equalTo("banana"))).which will returnFirstParameter()
        }
        assertEquals("banana", sut.returnString("banana"))
    }

    @Test fun nullableString() {
        mockery.expecting {
            allowing(sut).returnNullableString(with(absent())).which will returnFirstParameter()
        }
        assertEquals(null, sut.returnNullableString(null))
    }

    @Test fun boxedInteger() {
        val integer = Integer.valueOf(42) as Integer
        mockery.expecting {
            allowing(sut).returnBoxedInteger(with(integer)).which will returnFirstParameter()
        }
        assertEquals(integer, sut.returnBoxedInteger(integer))
    }
}

interface TestSubject {
    fun returnInt(t: Int) = t
    fun returnByte(t: Byte) = t
    fun returnBoolean(t: Boolean) = t
    fun returnLong(t: Long) = t
    fun returnDouble(t: Double) = t
    fun returnChar(t: Char) = t
    fun returnString(t: String) = t
    fun returnNullableString(t: String?) = t
    fun returnBoxedInteger(t: Integer) = t

}

@Suppress("UNCHECKED_CAST")
private fun <T> returnFirstParameter(): InvokeAction<T> {
    return invoke<T>("") { invocation -> invocation.getParameter(0) as T }
}
