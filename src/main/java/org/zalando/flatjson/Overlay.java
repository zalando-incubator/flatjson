package org.zalando.flatjson;

import java.util.ArrayList;
import java.util.List;

class Overlay {

    static int calculateBlockSize(int rawChars) {
        // make block size (in bytes) roughly equal to input size
        // (max block size is 16 KB)
        return 4 * Math.min(Math.max(rawChars / 16, 4), 1024);
    }

    private static final int TYPE = 0;
    private static final int FROM = 1;
    private static final int TO = 2;
    private static final int NESTED = 3;

    private final String raw;
    private final List<int[]> blocks;
    private final int blockSize;
    private int element;

    Overlay(String raw) {
        if (raw == null) throw new ParseException("cannot parse null");
        this.raw = raw;
        this.blocks = new ArrayList<>();
        this.blockSize = calculateBlockSize(raw.length());
        this.element = 0;
        parse();
    }

    Json.Type getType(int element) {
        return Json.Type.values()[getComponent(element, TYPE)];
    }

    int getNested(int element) {
        return getComponent(element, NESTED);
    }

    String getJson(int element) {
        return raw.substring(getComponent(element, FROM), getComponent(element, TO) + 1);
    }

    String getUnescapedString(int element) {
        String value = raw.substring(getComponent(element, FROM) + 1, getComponent(element, TO));
        return (getType(element) == Json.Type.STRING_ESCAPED) ? StringCodec.unescape(value) : value;
    }

    private void parse() {
        try {
            int last = skipWhitespace(parseValue(0));
            if (last != raw.length()) throw new ParseException("malformed json");
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("unbalanced json");
        }
    }

    private int parseValue(int i) {
        i = skipWhitespace(i);
        switch (raw.charAt(i)) {
            case '"': return parseString(i);
            case '{': return parseObject(i);
            case '[': return parseArray(i);
            case '0': case '1': case '2':
            case '3': case '4': case '5':
            case '6': case '7': case '8':
            case '9': case '-': return parseNumber(i);
            case 't': return parseTrue(i);
            case 'f': return parseFalse(i);
            case 'n': return parseNull(i);
            default: throw new ParseException("illegal char at pos: " + i);
        }
    }

    private int parseNumber(int i) {
        int from = i;
        boolean minus = false;
        boolean leadingZero = false;
        boolean dot = false;
        boolean exponent = false;
        while (i < raw.length()) {
            char c = raw.charAt(i);
            if (c == '-') {
                if (i > from) throw new ParseException("minus inside number");
                minus = true;
            } else if (c == 'e' || c == 'E') {
                if (exponent) throw new ParseException("double exponents");
                leadingZero = false;
                exponent = true;
                c = raw.charAt(i+1);
                if (c == '-' || c == '+') {
                    c = raw.charAt(i+2);
                    if (c < '0' || c > '9') throw new ParseException("invalid exponent");
                    i += 2;
                } else if (c >= '0' && c <= '9') {
                    i++;
                } else {
                    throw new ParseException("invalid exponent");
                }
            } else if (c == '.') {
                if (dot) throw new ParseException("multiple dots");
                if (i == from || (minus && (i == from + 1))) throw new ParseException("no digit before dot");
                leadingZero = false;
                dot = true;
            } else if (c == '0') {
                if (i == from) leadingZero = true;
            } else if (c >= '1' && c <= '9') {
                if (leadingZero) throw new ParseException("leading zero");
            } else {
                break;
            }
            i++;
        }
        if (minus && from == i-1) throw new ParseException("isolated minus");
        return createElement(Json.Type.NUMBER, from, i-1, 0);
    }

    private int parseString(int i) {
        boolean escaped = false;
        int from = i++;
        while (true) {
            char c = raw.charAt(i);
            if (c == '"') {
                Json.Type type = escaped ? Json.Type.STRING_ESCAPED : Json.Type.STRING;
                return createElement(type, from, i, 0);
            } else if (c < 32) {
                throw new ParseException("illegal control char: " + (int)c);
            } else if (c == '\\') {
                escaped = true;
                c = raw.charAt(i+1);
                if (c == '"' || c == '/' || c == '\\' || c == 'b' || c == 'f' || c == 'n' || c == 'r' || c == 't') {
                    i++;
                } else if (c == 'u') {
                    expectHex(i+2);
                    expectHex(i+3);
                    expectHex(i+4);
                    expectHex(i+5);
                    i += 5;
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
        createElement(Json.Type.ARRAY, i);
        i++;
        while (true) {
            i = skipWhitespace(i);
            if (raw.charAt(i) == ']') return closeElement(e, i, element - e - 1);
            if (count > 0) {
                expectChar(i, ',');
                i = skipWhitespace(i+1);
            }
            i = parseValue(i);
            count++;
        }
    }

    private int parseObject(int i) {
        int count = 0;
        int e = element;
        createElement(Json.Type.OBJECT, i);
        i++;
        while (true) {
            i = skipWhitespace(i);
            if (raw.charAt(i) == '}') return closeElement(e, i, element - e - 1);
            if (count > 0) {
                expectChar(i, ',');
                i = skipWhitespace(i+1);
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
        expectChar(i+1, 'u');
        expectChar(i+2, 'l');
        expectChar(i+3, 'l');
        return createElement(Json.Type.NULL, i, i+3, 0);
    }

    private int parseTrue(int i) {
        expectChar(i+1, 'r');
        expectChar(i+2, 'u');
        expectChar(i+3, 'e');
        return createElement(Json.Type.TRUE, i, i+3, 0);
    }

    private int parseFalse(int i) {
        expectChar(i+1, 'a');
        expectChar(i+2, 'l');
        expectChar(i+3, 's');
        expectChar(i+4, 'e');
        return createElement(Json.Type.FALSE, i, i+4, 0);
    }

    private int skipWhitespace(int i) {
        while (i < raw.length()) {
            char c = raw.charAt(i);
            if (c != ' ' && c != '\t' && c != '\n' && c != '\r') break;
            i++;
        }
        return i;
    }

    private void expectChar(int i, char c) {
        if (raw.charAt(i) != c) throw new ParseException("expected char '" + c + "' at pos " + i);
    }

    private void expectHex(int i) {
        char c = raw.charAt(i);
        if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')) return;
        throw new ParseException("invalid hex char at pos " + i);
    }

    private int getComponent(int element, int offset) {
        return getBlock(element)[getBlockIndex(element) + offset];
    }

    private int createElement(Json.Type type, int from) {
        return createElement(type, from, -1, -1);
    }

    private int createElement(Json.Type type, int from, int to, int nested) {
        int currentBlock = (element * 4) / blockSize;
        if (currentBlock == blocks.size()) {
            blocks.add(new int[blockSize]);
        }
        int[] block = blocks.get(currentBlock);
        int index = getBlockIndex(element);
        block[index] = type.ordinal();
        block[index + FROM] = from;
        block[index + TO] = to;
        block[index + NESTED] = nested;
        element++;
        return to+1;
    }

    private int closeElement(int element, int to, int nested) {
        int[] block = getBlock(element);
        int index = getBlockIndex(element);
        block[index + TO] = to;
        block[index + NESTED] = nested;
        return to+1;
    }

    private int[] getBlock(int element) {
        return blocks.get((element * 4) / blockSize);
    }

    private int getBlockIndex(int element) {
        return (element * 4) % blockSize;
    }
}
