package com.pigic.hzeropigic.utils;

/**
 * @author guchang.pan@hand-china.com
 * @Date: 2019/6/22 12:56
 */

import io.choerodon.mybatis.code.Style;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    private StringUtil() {
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String convertByStyle(String str, Style style) {
        switch(style) {
            case CAMELHUMP:
                return camelhumpToUnderline(str);
            case UPPERCASE:
                return str.toUpperCase();
            case LOWERCASE:
                return str.toLowerCase();
            case CAMELHUMP_AND_LOWERCASE:
                return camelhumpToUnderline(str).toLowerCase();
            case CAMELHUMP_AND_UPPERCASE:
                return camelhumpToUnderline(str).toUpperCase();
            case NORMAL:
            default:
                return str;
        }
    }

    public static String camelhumpToUnderline(String str) {
        char[] chars = str.toCharArray();
        int size = chars.length;
        StringBuilder sb = new StringBuilder(size * 3 / 2 + 1);

        for(int i = 0; i < size; ++i) {
            char c = chars[i];
            if (isUppercaseAlpha(c)) {
                sb.append('_').append(toLowerAscii(c));
            } else {
                sb.append(c);
            }
        }

        return sb.charAt(0) == '_' ? sb.substring(1) : sb.toString();
    }

    public static String underlineToCamelhump(String str) {
        Matcher matcher = Pattern.compile("_[a-z]").matcher(str);
        StringBuilder builder = new StringBuilder(str);

        for(int i = 0; matcher.find(); ++i) {
            builder.replace(matcher.start() - i, matcher.end() - i, matcher.group().substring(1).toUpperCase());
        }

        if (Character.isUpperCase(builder.charAt(0))) {
            builder.replace(0, 1, String.valueOf(Character.toLowerCase(builder.charAt(0))));
        }

        return builder.toString();
    }

    public static boolean isUppercaseAlpha(char c) {
        return c >= 'A' && c <= 'Z';
    }

    public static boolean isLowercaseAlpha(char c) {
        return c >= 'a' && c <= 'z';
    }

    public static char toUpperAscii(char c) {
        if (isLowercaseAlpha(c)) {
            c = (char)(c - 32);
        }

        return c;
    }

    public static char toLowerAscii(char c) {
        if (isUppercaseAlpha(c)) {
            c = (char)(c + 32);
        }

        return c;
    }

    public static String join(Iterable<?> iterable, String separator) {
        StringBuilder builder = new StringBuilder();
        Iterator iterator = iterable.iterator();

        while(iterator.hasNext()) {
            builder.append(iterator.next());
            if (iterator.hasNext()) {
                builder.append(separator);
            }
        }

        return builder.toString();
    }

    public static boolean tableNameAllUpperCase(String tableName) {
        String s = tableName.replaceAll("_", "");

        for(int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if (Character.isLowerCase(c)) {
                return false;
            }
        }

        return true;
    }
}

