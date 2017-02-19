require "json"
require "random_data"

class Random
  COLORS = %w[aliceblue antiquewhite aqua aquamarine azure beige bisque black blanchedalmond blue blueviolet brown burlywood cadetblue chartreuse chocolate coral cornflowerblue cornsilk crimson cyan darkblue darkcyan darkgoldenrod darkgray darkgreen darkkhaki darkmagenta darkolivegreen darkorange darkorchid darkred darksalmon darkseagreen darkslateblue darkslategray darkturquoise darkviolet deeppink deepskyblue dimgray dodgerblue firebrick floralwhite forestgreen fuchsia gainsboro ghostwhite gold goldenrod gray green greenyellow honeydew hotpink indianred indigo ivory khaki lavender lavenderblush lawngreen lemonchiffon lightblue lightcoral lightcyan lightgoldenrodyellow lightgreen lightgrey lightpink lightsalmon lightseagreen lightskyblue lightslategray lightsteelblue lightyellow lime limegreen linen magenta maroon mediumaquamarine mediumblue mediumorchid mediumpurple mediumseagreen mediumslateblue mediumspringgreen mediumturquoise mediumvioletred midnightblue mintcream mistyrose moccasin navajowhite navy oldlace olive olivedrab orange orangered orchid palegoldenrod palegreen paleturquoise palevioletred papayawhip peachpuff peru pink plum powderblue purple red rosybrown royalblue saddlebrown salmon sandybrown seagreen seashell sienna silver skyblue slateblue slategray snow springgreen steelblue tan teal thistle tomato turquoise violet wheat white whitesmoke yellow yellowgreen]
  
  def self.color
    COLORS.sample
  end
end

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