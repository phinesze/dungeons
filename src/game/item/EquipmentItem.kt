package game.item

import game.field.Field
import game.mold.EquipmentMold
import game.param.MaxNowValue

class EquipmentItem(var mold: EquipmentMold, var durability: MaxNowValue, field: Field): GameObject(mold.name, field) {

    /**
     * 表示するための文字列を取得する。
     */
    override fun display(): String {
        return mold.display
    }
}