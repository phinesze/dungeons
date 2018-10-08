package game.mold

import game.param.AbilityMold

/**
 *  敵キャラクターの種類を表す
 * @property display 自身の所属するFieldのtoStringで出力した際にオブジェクトの表示として表される文字
 * @property abilityMap 能力値を表す型
 */
class EnemyMold(val name: String,
                val display: String,
                val abilityMap: Map<Int, AbilityMold<Int>>
)