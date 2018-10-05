package game.mold

import game.NamableObject
import game.param.AbilityMold

class EquipmentMold(name: String, var type: EquipmentMoldType, var abilityMold: AbilityMold<Int>, var display: String): NamableObject(name)