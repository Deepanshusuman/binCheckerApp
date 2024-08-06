package com.assets.binfinder.util;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.LinkedHashSet;
import java.util.Set;

public  class Util {
    @NonNull
    public static String padZero(@NonNull String str) {
        int len = str.length();
        int zerosToAdd = 11 - len;
        StringBuilder builder = new StringBuilder(str);
        for (int i = 0; i < zerosToAdd; i++) {
            builder.append('0');
        }
        return builder.toString();
    }
    public static long[] extractBins(String input) {
        String[] lines = input.split("\n");
        Set<Long> bins = new LinkedHashSet<>();

        for (String line : lines) {
            if (line.length() >= 6 && line.length() <= 11) {
                try {
                    long num = Long.parseLong(padZero(line));
                    bins.add(num);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        return bins.stream().mapToLong(Long::longValue).toArray();
    }
}
