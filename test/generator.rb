require "json"
require "random_data"
require "securerandom"


class Random
  COLORS = %w[
    aquamarine azure beige black blue brown chartreuse chocolate coral 
    cornsilk crimson cyan firebrick forestgreen fuchsia gainsboro ghostwhite 
    gold gray green honeydew hotpink indianred indigo ivory khaki lavender 
    lawngreen lime linen magenta maroon navy olive orange orchid pink plum 
    purple red royalblue salmon sandybrown seagreen seashell sienna silver 
    skyblue slategray snow tan teal thistle tomato turquoise violet wheat 
    white yellow
  ]  
  def self.color
    COLORS.sample
  end
end


def bool
  [true, false, true, false, nil].sample
end

def int
  rand(-99_999..99_999)
end

def float
  (int * rand).round(rand(3..5))
end

def array_int
  {
    data: Array.new(rand(3..12)) { int }
  }
end

def array_float
  {
    metrics: Array.new(rand(3..12)) { float }
  }
end

def array_bool
  {
    settings: Array.new(rand(3..12)) { bool }
  }
end

def array_data
  send(%i[array_bool array_int array_float].sample)
end

def record
  {
    record_id: SecureRandom.uuid,
    first_name: Random.firstname,
    last_name: Random.lastname,
    email: Random.email,
    city: Random.city,
  }
end

def localized
  {
    localized: %w[de en fi nl it es pt].inject({}) do |hash, key| 
      hash[key] = Random.paragraphs.split.sample(rand(3..6)).join("_")
      hash 
    end
  }
end

def text
  {
    abstract: Random.paragraphs.strip
  }
end

def value
  send(%i[array_data record localized text].sample)
end

def hash
  result = { data_id: SecureRandom.uuid }
  rand(2..8).times do 
    result[Random.color] = value
  end
  result
end

def entry
  result = { entry_id: SecureRandom.uuid }
  rand(5..7).times do 
    result[Random.color] = hash
  end
  result
end

def list
  Array.new(rand(7..12)) { entry }
end

def message
  {
    created_at: Time.now.to_s,
    message_id: SecureRandom.uuid,
    offset: rand(9999999),
    data: list
  }
end


puts message.to_json

