package game.mold

import game.NamableObject
import game.param.AbilityMold

class EquipmentMold(name: String, var type: EquipmentMoldType, var abilityMold: AbilityMold, var display: String): NamableObject(name)