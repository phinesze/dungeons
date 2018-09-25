package game.param

class AbilityScore(var abilityMold: AbilityMold): Cloneable {
    val hp = MaxNowValue(abilityMold.maxHp)
    val mp = MaxNowValue(abilityMold.maxMp)

    public override fun clone(): AbilityScore {
        return AbilityScore(abilityMold)
    }

    override fun toString(): String {
        return "HP: ${hp}, MP: ${mp}," +
                "attack: ${abilityMold.attack}, defense: ${abilityMold.defense}, " +
                "magic attack: ${abilityMold.magicAttack}, magic defense: ${abilityMold.magicDefense}, " +
                "agility: ${abilityMold.agility}"
    }
}