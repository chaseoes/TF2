package com.chaseoes.tf2.utilities;

public class GeneralUtilities {

    public static String colorize(String s) {
        if (s == null) {
            return null;
        }
        return s.replaceAll("&([l-ok0-8k9a-f])", "\u00A7$1");
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

}
