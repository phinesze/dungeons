package game.mold

import game.NamableObject
import game.param.AbilityScore

/**
 *  敵キャラクターの種類を表す
 *
 */
class EnemyMold(name: String, display: String, var abilityScore: AbilityScore): NamableObject(name)