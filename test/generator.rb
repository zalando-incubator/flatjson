require "json"
require "random_data"


def basic(level)
  [nil, true, false].sample
end

def array(level)
  Array.new(rand(2..8)) { value(level - 1) }
end

def record(level)
  {
    first_name: Random.firstname,
    last_name: Random.lastname,
    email: Random.email,
    city: Random.city,
  }
end

def localizer
  Random.paragraphs.split.sample(rand(3..6)).join("_")
end

def localized(level)
  {
    _localized: %w[de en fi nl it es pt].inject({}) { |hash, key| hash[key] = localizer; hash }
  }
end

def text(level)
  Random.paragraphs.strip
end

def value(level)
  options = %w[basic text record localized]
  options += %w[array array hash hash] if level > 0
  send(options.sample, level - 1)
end

def key
  Random.alphanumeric(rand(5..12))
end

def hash(level)
  result = {}
  rand(2..8).times do 
    result[key] = value(level - 1)
  end
  result
end

puts hash(6).to_json