package game.param

class AbilityMold(
        var attack: Int,
        var defense: Int,
        var magicAttack: Int,
        var magicDefense: Int,
        var agility: Int,
        var elementAttack: ElementScore = ElementScore(),
        var elementDefense: ElementScore = ElementScore()
)