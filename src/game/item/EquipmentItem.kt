package game.item

import game.field.Field
import game.mold.EquipmentMold

class EquipmentItem(var mold: EquipmentMold, field: Field) : GameObject(mold.name, field, mold.display) {

    init {
        this.display = mold.display
    }
}