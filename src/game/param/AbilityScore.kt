package game.param

class AbilityScore(
        var hp: MaxNowValue,
        var mp: MaxNowValue,
        var attack: Int,
        var defense: Int,
        var magicAttack: Int,
        var magicDefense: Int,
        var agility: Int,
        var elementAttack: ElementScore = ElementScore(),
        var elementDefense: ElementScore = ElementScore()
) {

    override fun toString(): String {
        return "HP: ${hp}, MP: ${mp}, " +
                "attack: ${attack}, defense: ${defense}, " +
                "magic attack: ${magicAttack}, magic defense: ${magicDefense}, " +
                "agility: ${agility}"
    }
}