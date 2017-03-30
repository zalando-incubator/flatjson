package org.zalando.flatjson;

class StringCodec {

    static String escape(String input) {
        StringBuilder result = new StringBuilder(input.length());
        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);
            switch (c) {
                    case '\\': result.append("\\\\"); break;
                    case '"': result.append("\\\""); break;
                    case '\b': result.append("\\b"); break;
                    case '\f': result.append("\\f"); break;
                    case '\n': result.append("\\n"); break;
                    case '\r': result.append("\\r"); break;
                    case '\t': result.append("\\t"); break;
                    default: if (c < 32 || c > 126) {
                        result.append("\\u" + Integer.toUnsignedString(c, 16));
                    } else {
                        result.append(c);
                    }
            }
            i++;
        }
        return result.toString();
    }

    static String unescape(String input) {
        StringBuilder result = new StringBuilder(input.length());
        int i = 0;
        while (i < input.length()) {
            if (input.charAt(i) == '\\') {
                i++;
                switch (input.charAt(i)) {
                    case '\\': result.append('\\'); break;
                    case '/': result.append('/'); break;
                    case '"': result.append('"'); break;
                    case 'b': result.append('\b'); break;
                    case 'f': result.append('\f'); break;
                    case 'n': result.append('\n'); break;
                    case 'r': result.append('\r'); break;
                    case 't': result.append('\t'); break;
                    case 'u': {
                        result.append(Character.toChars(Integer.parseInt(input.substring(i+1, i+5), 16)));
                        i += 4;
                    }
                }
            } else {
                result.append(input.charAt(i));
            }
            i++;
        }
        return result.toString();
    }
}
