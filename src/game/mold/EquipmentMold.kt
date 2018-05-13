package game.mold

import game.NamableObject
import game.param.AbilityScore

class EquipmentMold(name: String, var type: EquipmentMoldType, var abilityScore: AbilityScore, var display: String): NamableObject(name)