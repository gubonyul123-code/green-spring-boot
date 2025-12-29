package com.green.green.utils;

public class StringUtils {
    public static String truncate(String text, int maxLength) {
        if (text == null){
            return null;
        }

        if (text.length() <= maxLength) {
            return text;
        }

        return text.substring(0,maxLength) + "...";
    }
}
