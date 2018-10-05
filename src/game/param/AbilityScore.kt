package game.param

/**
 * プレイヤー/敵キャラクターの能力値を表す。
 * 現在HP、現在MPを除いた能力値は2つ以上のキャラクタで共有される可能性のあるのでAbilityMoldとして共有される。
 */
class AbilityScore(abilityMold: AbilityMold<Int>): Cloneable {

    /**
     * 2つ以上のキャラクタで共有される可能性のある能力値
     * 敵キャラクターなどの場合は複数のAbilityScoreがAbilityMoldを参照する形となる。
     */
    var abilityMold:AbilityMold<Int> = abilityMold
            set(abilityMoldValue) {
                field = abilityMoldValue
                this.hp.max = abilityMoldValue.maxHp
                this.mp.max = abilityMoldValue.maxMp
            }

    val hp = MaxNowValue(abilityMold.maxHp)

    val mp = MaxNowValue(abilityMold.maxMp)

    val attack: Int
            get() = this.abilityMold.attack
    val defense: Int
            get() = this.abilityMold.defense
    val magicAttack: Int
            get() = this.abilityMold.magicAttack
    val magicDefense: Int
            get() = this.abilityMold.magicDefense
    val agility: Int
            get() = this.abilityMold.agility
    val droppingExp: Int
            get() = this.abilityMold.droppingExp

    public override fun clone(): AbilityScore {
        return AbilityScore(abilityMold)
    }

    override fun toString(): String {
        return "HP: ${this.hp}, MP: ${this.mp}," +
                "attack: ${abilityMold.attack}, defense: ${abilityMold.defense}, " +
                "magic attack: ${abilityMold.magicAttack}, magic defense: ${abilityMold.magicDefense}, " +
                "agility: ${abilityMold.agility}"
    }
}