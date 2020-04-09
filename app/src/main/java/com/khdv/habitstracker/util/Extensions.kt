package com.khdv.habitstracker.util

internal inline fun <reified T : Enum<T>> Int.toEnum() = enumValues<T>()[this]