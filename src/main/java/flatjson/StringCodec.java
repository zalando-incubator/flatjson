package flatjson;

class StringCodec {

    static String escape(String raw) {
        StringBuilder result = new StringBuilder(raw.length());
        int i = 0;
        while (i < raw.length()) {
            char c = raw.charAt(i);
            switch (c) {
                    case '\\': result.append("\\\\"); break;
                    case '"': result.append("\\\""); break;
                    case '\b': result.append("\\b"); break;
                    case '\f': result.append("\\f"); break;
                    case '\n': result.append("\\n"); break;
                    case '\r': result.append("\\r"); break;
                    case '\t': result.append("\\t"); break;
                    default: if (c < 32 || c > 126) {
                        result.append("\\u" + Integer.toUnsignedString((int)c, 16));
                    } else {
                        result.append(c);
                    }
            }
            i++;
        }
        return result.toString();
    }

    static String unescape(String raw) {
        StringBuilder result = new StringBuilder(raw.length());
        int i = 0;
        while (i < raw.length()) {
            if (raw.charAt(i) == '\\') {
                i++;
                switch (raw.charAt(i)) {
                    case '\\': result.append('\\'); break;
                    case '/': result.append('/'); break;
                    case '"': result.append('"'); break;
                    case 'b': result.append('\b'); break;
                    case 'f': result.append('\f'); break;
                    case 'n': result.append('\n'); break;
                    case 'r': result.append('\r'); break;
                    case 't': result.append('\t'); break;
                    case 'u': {
                        result.append(Character.toChars(Integer.parseInt(raw.substring(i+1, i+5), 16)));
                        i += 4;
                    }
                }
            } else {
                result.append(raw.charAt(i));
            }
            i++;
        }
        return result.toString();
    }
}
