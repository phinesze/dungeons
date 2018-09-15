package game.param

class AbilityScore(hp: Int, mp: Int, var abilityMold: AbilityMold) {
    val hp = MaxNowValue(hp)
    val mp = MaxNowValue(mp)

    override fun toString(): String {
        return "HP: ${hp}, MP: ${mp}," +
                "attack: ${abilityMold.attack}, defense: ${abilityMold.defense}, " +
                "magic attack: ${abilityMold.magicAttack}, magic defense: ${abilityMold.magicDefense}, " +
                "agility: ${abilityMold.agility}"
    }
}