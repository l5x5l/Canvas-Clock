package com.example.canvasclock.models

sealed class PagingItemEvent {
    data class Update<T>(val data : T) : PagingItemEvent()
    data class Delete<T>(val data : T) : PagingItemEvent()
}