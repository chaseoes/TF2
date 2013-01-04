package me.chaseoes.tf2.utilities;

public class GeneralUtilities {

    public static String colorize(String s) {
        if (s == null) {
            return null;
        }
        return s.replaceAll("&([l-ok0-8k9a-f])", "\u00A7$1");
    }

}
