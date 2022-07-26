package com.example.canvasclock.models

data class EventState<T>(
    val isLoading : Boolean = false,
    val cause : Throwable ?= null,
    val value : T? = null
) {
    val isError : Boolean = cause != null

    fun getOrDefault(defaultValue : T) : T {
        return value ?: defaultValue
    }

    companion object {
        fun <T> loading() = EventState<T>(isLoading = true)
        fun <T> success(value : T) = EventState<T>(value = value)
        fun <T> error(cause : Throwable) = EventState<T>(cause = cause)
    }
}