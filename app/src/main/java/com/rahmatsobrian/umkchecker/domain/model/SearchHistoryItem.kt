package com.rahmatsobrian.umkchecker.domain.model

data class SearchHistoryItem(
    val id: Long,
    val query: String,
    val timestamp: Long
)
