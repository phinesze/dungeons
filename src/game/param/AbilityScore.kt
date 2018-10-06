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

    /**
     * 最大HPと現在HPを表す値
     */
    val hp = MaxNowValue(abilityMold.maxHp)

    /**
     * 最大MPと現在MPを表す値
     */
    val mp = MaxNowValue(abilityMold.maxMp)

    /**
     * ブーストなどで調整済みの物理攻撃力を取得する。
     */
    val attack: Int
            get() = this.abilityMold.attack

    /**
     * ブーストなどで調整済みの物理防御力を取得する。
     */
    val defense: Int
            get() = this.abilityMold.defense

    /**
     * ブーストなどで調整済みの魔法攻撃力を取得する。
     */
    val magicAttack: Int
            get() = this.abilityMold.magicAttack

    /**
     * ブーストなどで調整済みの魔法防御力を取得する。
     */
    val magicDefense: Int
            get() = this.abilityMold.magicDefense

    /**
     * ブーストなどで調整済みの行動力を取得する。
     */
    val agility: Int
            get() = this.abilityMold.agility

    /**
     * 倒された場合に倒した側が獲得する経験値を取得する。
     */
    val droppingExp: Int
            get() = this.abilityMold.droppingExp

    public override fun clone(): AbilityScore {
        return AbilityScore(abilityMold)
    }

    override fun toString(): String {
        return "HP: ${this.hp}, MP: ${this.mp}," +
                "attack: ${this.attack}, defense: ${this.defense}, " +
                "magic attack: ${this.magicAttack}, magic defense: ${this.magicDefense}, " +
                "agility: ${this.agility}"
    }
}