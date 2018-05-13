require './game/item/game_charactor'
require './game/mold/enemy_mold'

module Game
  module Item

    class Enemy < GameCharactor

      def initialize(
          name: "unnamed",
          hp: 100,
          mp: 0,
          attack: 0,
          defense: 0,
          magic_attack: 0,
          magic_defense: 0,
          agility: 0
      )
        super
      end

      #ステータスを文字列化する。
      def to_s
        str = <<"EOS"
#{@name}:
能力値:
#{@abilities}
位置:
#{@position}
EOS
      end
    end
  end
end
