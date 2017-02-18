package flatjson;

import java.util.ArrayList;
import java.util.List;

public class Json {

    public static JsonValue parse(String raw) {
        return new Json(raw).parse();
    }

    enum Token {
        NULL,
        TRUE,
        FALSE,
        NUMBER,
        STRING,
        ARRAY,
        OBJECT
    }

    private static final int BLOCK_SIZE = 4 * 1024; // 16 KB

    private final String raw;
    private final List<int[]> elements;
    private int element;

    private Json(String raw) {
        this.raw = raw;
        this.elements = new ArrayList<>();
        this.element = 0;
    }

    private JsonValue parse() {
        if (raw == null) throw new ParseException("cannot parse null");
        if (raw.isEmpty()) throw new ParseException("cannot parse empty string");
        try {
            int index = skipWhitespace(parseValue(0));
//            System.out.println("last index: " + index);
            if (index != raw.length()) throw new ParseException("unbalanced json");
            return JsonValue.create(this, 0);
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("unbalanced json");
        }
    }

    private int parseValue(int i) {
        i = skipWhitespace(i);
        char c = readChar(i);
        switch (c) {
            case 'n': return parseNull(i);
            case 't': return parseTrue(i);
            case 'f': return parseFalse(i);
            case '"': return parseString(i);
            case '[': return parseArray(i);
            case '{': return parseObject(i);
            case '-':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9': return parseNumber(i);
            default: throw new ParseException(i);
        }
    }

    private int parseNumber(int i) {
        int from = i;
        boolean minus = false;
        boolean leadingZero = false;
        boolean dot = false;
        boolean exponent = false;
        while (i < raw.length()) {
            char c = readChar(i);
            if (c == '-') {
                if (i > from) throw new ParseException(c);
                minus = true;
            } else if (c == 'e' || c == 'E') {
                if (exponent) {
                    throw new ParseException("multiple exponents");
                } else {
                    leadingZero = false;
                    exponent = true;
                    c = readChar(i+1);
                    if (c == '-' || c == '+') {
                        c = readChar(i + 2);
                        if (c >= '0' && c <= '9') {
                            i += 2;
                        } else {
                            throw new ParseException("invalid exponent");
                        }
                    } else if (c >= '0' && c <= '9') {
                        i++;
                    } else {
                        throw new ParseException("invalid exponent");
                    }
                }
            } else if (c == '.') {
                if (dot) {
                    throw new ParseException("multiple dots");
                } else if (i == from || (minus && (i == from + 1))) {
                    throw new ParseException("missing digit before dot");
                } else {
                    leadingZero = false;
                    dot = true;
                }
            } else if (c == '0') {
                if (i == from) leadingZero = true;
            } else if (c >= '1' && c <= '9') {
                if (leadingZero) throw new ParseException("leading zero");
            } else {
                break;
            }
            i++;
        }
        if (minus && from == i - 1) throw new ParseException("isolated minus");
        return createElement(Token.NUMBER, from, i - 1, 0);
    }

    private int parseString(int i) {
        int from = i++;
        while (true) {
            char c = readChar(i);
            if (c == '"') {
                return createElement(Token.STRING, from, i, 0);
            } else if (c < 32) {
                throw new ParseException("illegal control char: " + (int)c);
            } else if (c == '\\') {
                c = readChar(i + 1);
                if (c == '"' || c == '/' || c == '\\' || c == 'b' || c == 'f' || c == 'n' || c == 'r' || c == 't') {
                    i++;
                } else if (c == 'u') {
                    expectHex(raw.substring(i + 2, i + 6));
                    i = i + 5;
                } else {
                    throw new ParseException("illegal escape char: " + c);
                }
            }
            i++;
        }
    }

    private int parseArray(int i) {
        int count = 0;
        int e = element;
        createElement(Token.ARRAY, i);
        i++;
        while (true) {
            i = skipWhitespace(i);
            if (readChar(i) == ']') return closeElement(e, i, count);
            if (count > 0) {
                expectChar(i, ',');
                i = skipWhitespace(i + 1);
            }
            i = parseValue(i);
            count++;
        }
    }

    private int parseObject(int i) {
        int count = 0;
        int e = element;
        createElement(Token.OBJECT, i);
        i++;
        while (true) {
            i = skipWhitespace(i);
            if (readChar(i) == '}') return closeElement(e, i, element - e);
            if (count > 0) {
                expectChar(i, ',');
                i = skipWhitespace(i + 1);
            }
            expectChar(i, '"');
            i = parseString(i);
            i = skipWhitespace(i);
            expectChar(i, ':');
            i = skipWhitespace(i+1);
            i = parseValue(i);
            count++;
        }
    }

    private int parseNull(int i) {
        if (raw.substring(i, i+4).equals("null")) return createElement(Token.NULL, i, i+3, 0);
        throw new ParseException(i);
    }

    private int parseTrue(int i) {
        if (raw.substring(i, i+4).equals("true")) return createElement(Token.TRUE, i, i+3, 0);
        throw new ParseException(i);
    }

    private int parseFalse(int i) {
        if (raw.substring(i, i+5).equals("false")) return createElement(Token.FALSE, i, i+4, 0);
        throw new ParseException(i);
    }

    private int skipWhitespace(int i) {
        while (i < raw.length()) {
            char c = readChar(i);
            if (c != ' ' && c != '\t' && c != '\n' && c != '\r') break;
            i++;
        }
        return i;
    }

    private void expectChar(int i, char c) {
        if (readChar(i) != c) throw new ParseException("expected char '" + c + "' at pos " + i);
    }

    private char readChar(int i) {
        char c = raw.charAt(i);
//        System.out.println("read: " + c);
        return c;
    }

    private void expectHex(String hexcode) {
        try {
            Integer.parseInt(hexcode, 16);
        } catch (NumberFormatException e) {
            throw new ParseException("invalid hex: " + hexcode);
        }
    }

    Token getToken(int element) {
        return Token.values()[getElement(element, 0)];
    }

    int getFrom(int element) {
        return getElement(element, 1);
    }

    int getTo(int element) {
        return getElement(element, 2);
    }

    int getNested(int element) {
        return getElement(element, 3);
    }

    String getRaw(int element) {
        return raw.substring(getFrom(element), getTo(element) + 1);
    }

    String getRawString(int element) {
        return raw.substring(getFrom(element) + 1, getTo(element));
    }

    private int getElement(int element, int offset) {
        int major = (element * 4 + offset) / BLOCK_SIZE;
        int minor = (element * 4 + offset) % BLOCK_SIZE;
        return elements.get(major)[minor];
    }

    private int createElement(Token token, int from) {
        return createElement(token, from, 0, 0);
    }

    private int createElement(Token token, int from, int to, int nested) {
        System.out.println("create: " + element + " " + token);
        int major = (element * 4) / BLOCK_SIZE;
        int minor = (element * 4) % BLOCK_SIZE;
        if (major == elements.size()) {
            elements.add(new int[BLOCK_SIZE]);
        }
        int[] block = elements.get(major);
        block[minor] = token.ordinal();
        block[minor + 1] = from;
        block[minor + 2] = to;
        block[minor + 3] = nested;
        element++;
        return to+1;
    }

    private int closeElement(int element, int to, int nested) {
        System.out.println("close: " + element + " (" + nested + ")");
        int major = (element * 4) / BLOCK_SIZE;
        int minor = (element * 4) % BLOCK_SIZE;
        int[] block = elements.get(major);
        block[minor + 2] = to;
        block[minor + 3] = nested;
        return to+1;
    }

}
