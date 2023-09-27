package com.jhlee.kmm_rongame.constants

class RuleConst {
    companion object {
        // 카드 게임에서 총 비용 이 값을 넘는 카드는 선택할수 없다
        const val CARD_BATTLE_MAX_COST = 10;
        // 카드 게임 총 배틀 라운드
        const val CARD_BATTLE_MAX_ROUND = 10
        // 출석 후 받을수 있는 금액
        const val ATTEND_REWARD = 300
        // 퀴즈 풀때 차감 금액
        const val QUIZ_COST = 100
    }
}