package com.khdv.habitstracker.domain.shared

inline fun <reified T : Enum<T>> Int.toEnum() = enumValues<T>()[this]