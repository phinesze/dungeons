package game.param

/**
 * プレイヤー/敵キャラクターの経験値とレベルを表現する。
 * プレイヤーオブジェクトに参照される場合は、派生クラスであるPlayerLevelAndExperienceを使用する。
 * 敵キャラクターオブジェクトに参照される場合は、このクラスのインスタンスをそのまま使用する。
 *
 * @param level プレイヤーの場合は初期レベル、敵キャラクターの場合は敵のレベル
 * @param experience プレイヤーの場合は初期累積経験値、敵キャラクターの場合は倒した際にプレイヤーが獲得する経験値
 */
open class LevelAndExperience(level: Int, experience: Long) {

    /**
     * 現在のレベルを表す1から始まる値
     */
    var level: Int = level
        protected set

    /**
     * 現在までの累積経験値
     */
    var experience: Long = 0
        protected set
}