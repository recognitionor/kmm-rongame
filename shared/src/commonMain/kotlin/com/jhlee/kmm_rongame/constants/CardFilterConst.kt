package com.jhlee.kmm_rongame.constants

object CardFilterConst {
    //"번호", "등급", "업그레이드", "현재능력", "잠재능력", "소유"
    const val ID = 0
    const val GRADE = 1
    const val UPGRADE = 2
    const val POWER = 3
    const val POTENTIAL = 4

    val CARD_FILTER_MAP: Map<Int, String> = mapOf(
        ID to ("번호"),
        GRADE to ("등급"),
        UPGRADE to ("업그레이드"),
        POWER to ("현재능력"),
        POTENTIAL to ("잠재능력"),
    )


}