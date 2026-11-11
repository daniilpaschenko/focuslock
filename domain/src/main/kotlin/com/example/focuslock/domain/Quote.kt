package com.example.focuslock.domain

/**
 * Motivational quote used e.g. during a focus session.
 */
data class Quote(
    val id: String,
    val text: String,
    val author: String,
)
