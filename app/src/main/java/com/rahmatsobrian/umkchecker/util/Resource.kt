package com.rahmatsobrian.umkchecker.util

/**
 * Generic wrapper used across the Repository -> ViewModel boundary to represent
 * the outcome of an operation without leaking exceptions into the UI layer.
 */
sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String, val cause: Throwable? = null) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}
