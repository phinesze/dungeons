require './game/mold/skill'
include Game::Mold

module Game::DataList
  skill_list = [
      Skill.new(name: "ファイアα", time_cost: 1000, mp_cost: 5),
      Skill.new(name: "アイスα", time_cost: 1000, mp_cost: 5)
  ]
end
