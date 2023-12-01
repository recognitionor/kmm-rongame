package com.jhlee.kmm_rongame.utils

import com.jhlee.kmm_rongame.card.domain.Card


class GameUtils {

    companion object {

        fun getBattlePower(myCard: Card, enemyCard: Card) {
            var myPower = 0
            var enemyPower = 0
            val sb = StringBuilder()
            myCard.type.forEach { myType ->
                enemyCard.type.forEach { enemyType ->
                    val strong = myType.strongList[enemyType.id]
                    if (strong != null) {
                        sb.append("${myType.name} 타입은 ${enemyType.name} 에게 $strong 추가로 공격한다.\n")
                        myPower += strong
                    }
                }
            }

            enemyCard.type.forEach { enemyType ->
                myCard.type.forEach { myType ->
                    val strong = enemyType.strongList[myType.id]
                    if (strong != null) {
                        sb.append("${enemyType.name} 타입은 ${myType.name} 에게 $strong 추가로 공격한다.\n")
                        enemyPower += strong
                    }
                }
            }
            myCard.type.forEach { myType ->
                enemyCard.type.forEach { enemyType ->
                    val strong = myType.strongList[enemyType.id]
                    if (strong != null) {
                        myPower += strong
                    }
                }
            }
        }
    }
}