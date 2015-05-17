package com.logicmonitor.util;

import java.util.Random;

/**
 * Created by allen.gl on 2015/5/13.
 */
public abstract class RandomStringGenerator {

    private static String LINE_SEPARATOR = System.getProperty("line.separator");

    private static final char[] symbols;

    static {
        StringBuilder tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ++ch)
            tmp.append(ch);
        for (char ch = 'a'; ch <= 'z'; ++ch)
            tmp.append(ch);
        symbols = tmp.toString().toCharArray();
    }

    private static final Random random = new Random();

    public static String nextString(int minLength, int maxLength) {
        int r = random.nextInt(maxLength - minLength);
        final int length = r + minLength;
        char[] buf = new char[length - 1];
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf) + LINE_SEPARATOR;
    }
}
