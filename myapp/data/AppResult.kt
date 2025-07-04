package com.wahab.myapp.data

sealed class AppResult<out T> {
    data class Success<out T>(val data: T): AppResult<T>()
    data class Error(val exception: Exception): AppResult<Nothing>()
}
