package com.jhlee.kmm_rongame.card.data

import com.jhlee.kmm_rongame.card.domain.CardType
import migrations.CardInfoEntity

object CardInfoManager {

//    val CARD_INFO_LIST by lazy {
//        ArrayList<CardInfoEntity>()
//    }

    val CARD_COMBINE_LIST by lazy {
        ArrayList<CardCombinationInfo>()
    }

    val CARD_TYPE_MAP by lazy {
        HashMap<String, CardType>()
    }
    val CARD_TYPE_ID_MAP by lazy {
        HashMap<Int, String>()
    }

    fun getCardTypeFromId(id: Int): CardType? {
        return CARD_TYPE_MAP[CARD_TYPE_ID_MAP[id] ?: ""]
    }
}