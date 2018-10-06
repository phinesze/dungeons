package game.item

import game.field.Field
import game.mold.EquipmentMold
import game.param.MaxNowValue

class EquipmentItem(var mold: EquipmentMold, var durability: MaxNowValue, field: Field): GameObject(mold.name, field,mold.display) {

    init {
        this.display = mold.display
    }
}