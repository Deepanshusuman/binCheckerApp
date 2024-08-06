package com.assets.binfinder.util;

import java.util.Locale;

public class EmojiUtil {

    public static String fromCountryCode(String countryCode) {
        if (countryCode == null || countryCode.length() != 2) {
            return "-";
        }
        countryCode = countryCode.toUpperCase(Locale.US);
        int firstLetter = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6;
        int secondLetter = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6;
        return new String(Character.toChars(firstLetter)) + new String(Character.toChars(secondLetter));
    }
}

