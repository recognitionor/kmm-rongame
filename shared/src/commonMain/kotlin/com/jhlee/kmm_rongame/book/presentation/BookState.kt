package com.jhlee.kmm_rongame.book.presentation

import com.jhlee.kmm_rongame.card.domain.Card

data class BookState(
    val isLoading: Boolean = false,
    val screenState: Int = 0,
    val bookList: List<Card> = emptyList(),
    val detailCardInfo: Card? = null,
    val sortedBookList: List<Card> = emptyList(),
    val search: String = "",
    val sortFilter: Int = 0
) {
    companion object {
        val SORT_LIST = arrayListOf("번호", "등급", "소유", "번호 역순", "미소유")
        const val BOOK_SCREEN_DEFAULT = 0
        const val BOOK_SCREEN_DETAIL = 1
        const val BOOK_SCREEN_CARD_COLLECTOR = 2
    }
}