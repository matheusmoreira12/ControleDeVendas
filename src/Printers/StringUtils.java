package Printers;

public abstract class StringUtils {
    public static String padEnd(String str, Character padChar, int length) {
        return str + generatePadding(padChar, length - str.length());
    }

    public static String padBoth(String str, Character padChar, int length) {
        int padLength = length -  str.length();
        int padStartLength = padLength / 2;
        int padEndLength = padLength - padStartLength;
        return generatePadding(padChar, padStartLength) + str + generatePadding(padChar, padEndLength);
    }

    private static String generatePadding(Character padChar, int padLength) {
        return String.valueOf(padChar).repeat(Math.max(0, padLength));
    }
}
