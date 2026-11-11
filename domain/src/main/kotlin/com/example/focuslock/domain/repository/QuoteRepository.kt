package com.example.focuslock.domain.repository

import com.example.focuslock.domain.model.Quote

/**
 * Repository for retrieving motivational quotes
 */
interface QuoteRepository {
    fun getQuotes(): List<Quote>

    fun getQuoteById(id: String): Quote?

    fun getRandomQuote(): Quote?
}
