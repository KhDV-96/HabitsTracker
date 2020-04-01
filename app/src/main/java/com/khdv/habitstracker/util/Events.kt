package com.khdv.habitstracker.util

open class ActionEvent {
    private var hasBeenHandled = false

    fun executeIfNotHandled(block: () -> Unit) {
        if (!hasBeenHandled) {
            hasBeenHandled = true
            block()
        }
    }
}

open class ContentEvent<out T>(private val content: T) {

    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? = when(hasBeenHandled) {
        true -> null
        else -> {
            hasBeenHandled = true
            content
        }
    }
}