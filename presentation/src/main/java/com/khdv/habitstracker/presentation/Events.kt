package com.khdv.habitstracker.presentation

import androidx.lifecycle.Observer

class ActionEvent {
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

    fun getContentIfNotHandled(): T? = when (hasBeenHandled) {
        true -> null
        else -> {
            hasBeenHandled = true
            content
        }
    }
}

class ActionEventObserver(private val onEventUnhandledCallback: () -> Unit) :
    Observer<ActionEvent> {

    override fun onChanged(event: ActionEvent?) {
        event?.executeIfNotHandled(onEventUnhandledCallback)
    }
}

class ContentEventObserver<T>(private val onEventUnhandledCallback: (T) -> Unit) :
    Observer<ContentEvent<T>> {

    override fun onChanged(event: ContentEvent<T>?) {
        event?.getContentIfNotHandled()?.run(onEventUnhandledCallback)
    }
}