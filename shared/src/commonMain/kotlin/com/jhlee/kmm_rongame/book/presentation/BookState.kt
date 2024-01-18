package com.jhlee.kmm_rongame.book.presentation

import com.jhlee.kmm_rongame.card.domain.Card

data class BookState(
    val isLoading: Boolean = false,
    val bookList: List<Card> = emptyList(),
    val search: String = "",
    val sortFilter: Int = 0
) {
    companion object {}
}