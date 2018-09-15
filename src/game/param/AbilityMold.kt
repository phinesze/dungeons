package game.param

class AbilityMold(
        var attack: Int,
        var defense: Int,
        var magicAttack: Int,
        var magicDefense: Int,
        var agility: Int,
        var elementAttack: ElementScore = ElementScore(),
        var elementDefense: ElementScore = ElementScore()
): Cloneable {
    override fun clone(): AbilityMold {
        return AbilityMold(attack, defense, magicAttack, magicDefense, agility)
    }
}