package game.mold

import game.NamableObject
import game.field.Field
import game.field.FieldBlock
import game.item.GameCharacter

/**
 * スキルを表す。
 */
class Skill(
        name: String,
        var action: (GameCharacter) -> Unit,
        var timeCost: Int,
        var mpCost: Int,
        var keyInput: String
) : NamableObject(name)