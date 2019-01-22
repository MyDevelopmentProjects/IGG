package ge.unknown.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 *  StringUtils.java
 *  Created by George Vashakidze.
 *  Copyright (c) 2015 Selfin LLC. All rights reserved.
 *
 */
public class MGLStringUtils {
    /**
     * Return a substring of the source string up until the first
     * occurrence of the specified character
     *
     * @param source - The source string to operate on
     * @param c      - Character to search for
     * @return - Substring of the original string up until the first occurance of c
     */
    public static String substringUpToFirstOccurance(String source, char c) {
        return !IsNullOrBlank(source) && !Character.isWhitespace(c) ?
                source.substring(0, source.indexOf(c)) : null;
    }

    /**
     * Return a substring of the source string after the first
     * occurrence of the specified character
     *
     * @param source - The source string to operate on
     * @param c      - Character to search for
     * @return - Substring of the original string after the first occurrence of c
     */
    public static String substringFromFirstOccurance(String source, char c) {
        return !IsNullOrBlank(source) && !Character.isWhitespace(c) ?
                source.substring(source.indexOf(c), source.length() - 1) : null;
    }

    /**
     * Return a substring of the source string up until the last
     * occurrence of the specified character
     *
     * @param source - The source string to operate on
     * @param c      - Character to search for
     * @return - Substring of the original string up until the last occurrence of c
     */
    public static String substringUpToLastOccurance(String source, char c) {
        return !IsNullOrBlank(source) && !Character.isWhitespace(c) ?
                source.substring(0, source.lastIndexOf(c)) : null;
    }

    /**
     * Return a substring of the source string after the last
     * occurrence of the specified character
     *
     * @param source - The source string to operate on
     * @param c      - Character to search for
     * @return - Substring of the original string after the last occurrence of c
     */
    public static String substringFromLastOccurance(String source, char c) {
        return !IsNullOrBlank(source) && !Character.isWhitespace(c) ?
                source.substring(source.lastIndexOf(c), source.length() - 1) : null;
    }

    /**
     * Return a substring of the source string after the first occurrence
     * of the search String
     *
     * @param source       - The source string to operate on
     * @param searchString - String to search for
     * @return - Substring of the original string after the first occurrence of searchString
     */
    public static String substringAfterSearchString(String source, String searchString) {
        if (!IsNullOrBlank(source) && !IsNullOrBlank(searchString)) {
            int index = source.indexOf(searchString);
            return index > -1 ? source.substring((index + searchString.length()), source.length() - 1) : null;
        } else {
            return null;
        }
    }

    /**
     * @param param - The source string to operate on
     * @return - Returns True If param is null or param characters length == 0
     */
    public static boolean IsNullOrBlank(String param) {
        if (param != null && !param.isEmpty()) {
            return false;
        }
        return true;
    }

    public static Date String2Date(String dateStr) {
        if (dateStr != null && !dateStr.isEmpty()) {
            try {

                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return new Date();
    }
}
