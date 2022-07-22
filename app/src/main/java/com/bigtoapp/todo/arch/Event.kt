package com.bigtoapp.todo.arch

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again
     * **/
    fun getContent(): T? {
        return if(hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content? even if its already been handled
     * **/
    fun peekContent(): T = content
}