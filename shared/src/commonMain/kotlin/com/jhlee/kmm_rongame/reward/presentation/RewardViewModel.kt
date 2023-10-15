package com.jhlee.kmm_rongame.reward.presentation

import com.jhlee.kmm_rongame.core.util.Logger
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class RewardViewModel : ViewModel() {

    companion object {
        const val VIEWMODEL_KEY = "reward_view_model"

        const val REWARD_DEFAULT_SCREEN = 0

        const val REWARD_QUIZ_SCREEN = 1

        const val REWARD_ETC_SCREEN = 0
    }

    private val _state = MutableStateFlow(RewardState())

    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    init {

    }

    fun selectedScreen(selected: Int) {
        Logger.log("selectedScreen")

        _state.update {
            it.copy(rewardScreenSelected = selected)
        }
    }

    fun toggleQuizDialog(toggleDialog: Boolean) {
        _state.update {
            it.copy(openQuizDialog = toggleDialog)
        }
    }

}