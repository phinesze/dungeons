require './game/item/game_charactor'

module Game
  module Item
    class Player < GameCharactor

      #表示
      def display;
        "😏 "
      end

      #装備
      def equipments;
        @equipments
      end

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
        @equipments = EquipmentState.new()

        puts "\"#{@name}\" は生まれました。"
      end

      #ゲームボード上での1カウント経過時の行動
      def move_in_count
        super
      end

      #ステータスを文字列化する。
      def to_s
        str = <<"EOS"
#{@name}:
能力値:
#{@abilities}
装備:
#{@equipments}
位置:
#{@position}
EOS
        return str
      end

      private

      #行動が回ってきた時の動作
      def turn
        super
        loop do

          puts turn_message
          puts

          input = gets
          puts

          case input
          when "w\n" then
            puts "#{@name}は前に進んだ。"
          when "s\n" then
            puts "#{@name}は後ろに進んだ。"
          when "a\n"
            puts "#{@name}は左に進んだ。"
          when "d\n" then
            puts "#{@name}は右に進んだ。"
          when "exit\n" then
            puts "さよならだ・・また会う日まで"
            exit
          else
            puts "もう一度入力してください。"
          end
          puts
        end
      end

      #行動が回ってきた時のメッセージを出力する。
      def turn_message
        <<"EOS"
#{@name}は何をしますか？
w:前へ
s:後ろへ
a:左へ
d:右へ
exit:終了
EOS
      end
    end
  end
end
