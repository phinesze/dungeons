package game.mold

import game.NamableObject
import game.param.AbilityMold

/**
 *  敵キャラクターの種類を表す
 *
 */
class EnemyMold(name: String, display: String, internal var abilityMold: AbilityMold): NamableObject(name)