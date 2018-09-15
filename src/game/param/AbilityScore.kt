package game.param

class AbilityScore(hp: Int, mp: Int, var abilityMold: AbilityMold): Cloneable {
    val hp = MaxNowValue(hp)
    val mp = MaxNowValue(mp)

    public override fun clone(): AbilityScore {
        return AbilityScore(hp.max, mp.max, abilityMold)
    }

    override fun toString(): String {
        return "HP: ${hp}, MP: ${mp}," +
                "attack: ${abilityMold.attack}, defense: ${abilityMold.defense}, " +
                "magic attack: ${abilityMold.magicAttack}, magic defense: ${abilityMold.magicDefense}, " +
                "agility: ${abilityMold.agility}"
    }
}