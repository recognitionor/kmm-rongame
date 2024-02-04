package com.jhlee.kmm_rongame.book.presentation

import com.jhlee.kmm_rongame.book.domain.BookDataSource
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookViewModel(private val bookDataSource: BookDataSource) : ViewModel() {
    companion object {

        const val VIEWMODEL_KEY = "book_view_model"

    }

    private val _state = MutableStateFlow(BookState())

    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    init {
        getBookList()
    }

    fun setDetailCardInfo(card: Card) {
        _state.update {
            it.copy(detailCardInfo = card)
        }
    }

    fun selectScreenMode(viewMode: Int) {
        _state.update { it.copy(screenState = viewMode) }
    }

    fun setSearchKeyword(keyword: String) {
        _state.update {
            it.copy(search = keyword)
        }
    }

    fun searchSortBookList(
        keyword: String = state.value.search, sortIndex: Int = state.value.sortFilter
    ) {
        _state.update { it.copy(isLoading = true, sortFilter = sortIndex, search = keyword) }
        viewModelScope.launch {
            val sortedList = _state.value.bookList.filter {
                if (keyword.isEmpty()) {
                    true
                } else {
                    it.name.contains(
                        keyword, ignoreCase = true
                    ) || it.nameEng.contains(keyword, ignoreCase = true)
                }

            }.sortedWith(compareBy<Card> {
                when (state.value.sortFilter) {
                    1 -> {
                        -it.grade
                    }

                    2, 4 -> {
                        it.count
                    }

                    else -> {
                        it.cardId
                    }
                }
            }.thenBy {
                it.cardId
            })
            if (state.value.sortFilter == 3 || state.value.sortFilter == 2) {
                _state.update {
                    it.copy(sortedBookList = sortedList.reversed(), isLoading = false)
                }
            } else {
                _state.update {
                    it.copy(sortedBookList = sortedList, isLoading = false)
                }
            }
        }
    }

    private fun getBookList() {
        bookDataSource.getBookList().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false, bookList = result.data ?: emptyList()
                        )
                    }
                    searchSortBookList()
                }

                is Resource.Loading -> {
                    _state.update {
                        it.copy(
                            isLoading = true, bookList = result.data ?: emptyList()
                        )
                    }
                }

                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }.launchIn(viewModelScope)
    }

}