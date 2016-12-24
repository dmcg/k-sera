package com.oneeyedmen.amock

import kotlin.reflect.KClass

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