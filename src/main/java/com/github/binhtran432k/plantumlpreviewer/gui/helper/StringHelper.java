package com.github.binhtran432k.plantumlpreviewer.gui.helper;

/**
 * Helper for string, extracted from
 * {@link net.sourceforge.plantuml.StringUtils}
 */
public class StringHelper {

    public static String repeat(String s, int count) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < count; i++) {
            sb.append(s);
        }
        return sb.toString();
    }

    public static String unicodeForHtml(String s) {
        final StringBuilder result = new StringBuilder();
        for (char c : s.toCharArray())
            if (c > 127 || c == '&' || c == '|' || c == '<' || c == '>') {
                final int i = c;
                result.append("&#" + i + ";");
            } else {
                result.append(c);
            }

        return result.toString();
    }

    public static String eventuallyRemoveStartingAndEndingDoubleQuote(String s) {
        if (s == null)
            return s;

        return eventuallyRemoveStartingAndEndingDoubleQuote(s, "\"([:");
    }

    public static String eventuallyRemoveStartingAndEndingDoubleQuote(String s, String format) {
        if (s == null)
            return null;

        if (format.contains("\"") && s.length() > 1 && isDoubleQuote(s.charAt(0))
                && isDoubleQuote(s.charAt(s.length() - 1)))
            return s.substring(1, s.length() - 1);

        if (format.contains("(") && s.startsWith("(") && s.endsWith(")"))
            return s.substring(1, s.length() - 1);

        if (format.contains("[") && s.startsWith("[") && s.endsWith("]"))
            return s.substring(1, s.length() - 1);

        if (format.contains(":") && s.startsWith(":") && s.endsWith(":"))
            return s.substring(1, s.length() - 1);

        return s;
    }

    private static boolean isDoubleQuote(char c) {
        return c == '\"' || c == '\u201c' || c == '\u201d' || c == '\u00ab' || c == '\u00bb';
    }
}
