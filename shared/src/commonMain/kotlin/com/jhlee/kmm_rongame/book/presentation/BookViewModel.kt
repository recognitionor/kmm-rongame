package com.jhlee.kmm_rongame.book.presentation

import com.jhlee.kmm_rongame.book.domain.Book
import com.jhlee.kmm_rongame.book.domain.BookDataSource
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.cardgame.domain.CardGameDataSource
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class BookViewModel(private val bookDataSource: BookDataSource) : ViewModel() {
    companion object {

        const val VIEWMODEL_KEY = "book_view_model"

    }

    private val _state = MutableStateFlow(BookState())

    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    init {
        getBookList()
    }

    fun setSearchKeyword(keyword: String) {
        _state.update {
            it.copy(search = keyword)
        }
    }

    private fun getBookList() {
        bookDataSource.getBookList().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            bookList = result.data ?: emptyList(), isLoading = false
                        )
                    }
                }

                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }

                }

                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    Logger.log("error ${result.data}")
                }
            }
        }.launchIn(viewModelScope)
    }

}