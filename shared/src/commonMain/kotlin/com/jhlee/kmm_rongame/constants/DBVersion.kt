package com.jhlee.kmm_rongame.constants

object DBVersion {
    const val CARD_DB_TYPE = 0
    const val CARDTYPE_DB_TYPE = 1
    const val CARDCOMBINE_DB_TYPE = 2
    const val QUIZ_DB_TYPE = 3

//    const val CARD_FB_PATH = "card_test"
//    const val CARDTYPE_FB_PATH = "card_type_test"
//    const val CARDCOMBINE_FB_PATH = "card_combine_test"
    const val CARD_FB_PATH = "card"
    const val CARDTYPE_FB_PATH = "card_type"
    const val CARDCOMBINE_FB_PATH = "card_combine"
    const val QUIZ_FB_PATH = "quiz"

    const val VERSION = "version.csv"

    val DB_CARD_FILE_LIST = arrayListOf(
        CARD_FB_PATH, CARDTYPE_FB_PATH, CARDCOMBINE_FB_PATH, QUIZ_FB_PATH
    )
}