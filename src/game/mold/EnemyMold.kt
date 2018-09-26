package game.mold

import game.NamableObject
import game.param.AbilityMold

/**
 *  敵キャラクターの種類を表す
 * @property display 自身の所属するFieldのtoStringで出力した際にオブジェクトの表示として表される文字
 * @property abilityMold 能力値を表す型
 * @property level レベル
 * @property experience プレイヤーが倒した場合に獲得できる経験値
 */
class EnemyMold(name: String,
        var display: String,
        var abilityMold: AbilityMold,
        var level: Int,
        var experience: Long
        ): NamableObject(name)