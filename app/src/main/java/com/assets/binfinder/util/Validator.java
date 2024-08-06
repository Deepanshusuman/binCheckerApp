package com.assets.binfinder.util;

import androidx.annotation.NonNull;

import com.assets.binfinder.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.regex.Pattern;

public class Validator {

    public static boolean luhnCheck(@NonNull String cardNumber) {
        int digits = cardNumber.length();
        int oddOrEven = digits & 1;
        long sum = 0;
        for (int count = 0; count < digits; count++) {
            int digit;
            try {
                digit = Integer.parseInt(cardNumber.charAt(count) + "");
            } catch (NumberFormatException e) {
                return false;
            }

            if (((count & 1) ^ oddOrEven) == 0) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
        }

        return sum != 0 && (sum % 10 == 0);
    }


    @NonNull
    public static String reverse(@NonNull String s) {
        return new StringBuilder(s).reverse().toString();
    }

    @NonNull
    public static String generate(@NonNull String prefix, int length) {
        StringBuilder prefixBuilder = new StringBuilder(prefix);
        while (prefixBuilder.length() < (length - 1)) {
            prefixBuilder.append(Double.valueOf(Math.floor(Math.random() * 10)).intValue());
        }
        prefix = prefixBuilder.toString();

        String reversedCCnumberString = reverse(prefix);
        List<Integer> reversedCCnumberList = new Vector<>();
        for (int i = 0; i < reversedCCnumberString.length(); i++) {
            reversedCCnumberList.add(Integer.valueOf(String.valueOf(reversedCCnumberString.charAt(i))));
        }
        int sum = 0;
        int pos = 0;
        int a = reversedCCnumberList.size();
        Integer[] reversedCCnumber = reversedCCnumberList.toArray(new Integer[a]);
        while (pos < length - 1) {
            int odd = reversedCCnumber[pos] * 2;
            if (odd > 9) {
                odd -= 9;
            }
            sum += odd;
            if (pos != (length - 2)) {
                sum += reversedCCnumber[pos + 1];
            }
            pos += 2;
        }
        int checkdigit = Double.valueOf(((Math.floor(sum / 10.0) + 1) * 10 - sum) % 10).intValue();
        prefix += checkdigit;
        return prefix;
    }


    public static int random(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    @NonNull
    public static String addZero(int value, int digit) {
        return String.format("%0" + digit + "d", value);
    }

    @NonNull
    public static String format(@NonNull String bin) {
        StringBuilder cardNumber = new StringBuilder();
        if (Pattern.compile("^(34|37)[0-9]*$").matcher(bin).matches()) {
            String s = String.format("%-15s", bin).replace(' ', '0');
            for (int i = 0; i < 15; i++) {
                if (i == 4 || i == 10) {
                    cardNumber.append(' ');
                }
                cardNumber.append(s.charAt(i));
            }

        } else if (Pattern.compile("^(36)[0-9]*$").matcher(bin).matches()) {
            String s = String.format("%-14s", bin).replace(' ', '0');
            for (int i = 0; i < 14; i++) {
                if (i == 4 || i == 10) {
                    cardNumber.append(' ');
                }
                cardNumber.append(s.charAt(i));
            }
        } else {
            String s = String.format("%-16s", bin).replace(' ', '0');
            for (int i = 0; i < 16; i++) {
                if (i == 4 || i == 8 || i == 12) {
                    cardNumber.append(' ');
                }
                cardNumber.append(s.charAt(i));
            }

        }
        return cardNumber.toString();
    }

    @NonNull
    public static ArrayList<String> createCards(@NonNull String card_number, int length, int howMany, @NonNull String cvv, @NonNull String month, @NonNull String year, @NonNull String format) {
        ArrayList<String> result = new ArrayList<>();
        int n = Integer.parseInt(card_number.length() > 2 ? card_number.substring(0, 2) : card_number.substring(0, 1));
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        int i = 0;
        while (i < howMany) {
            String str = generate(card_number, length);
            if (luhnCheck(str)) {
                str += (month.equals("Random") ? format + addZero(random(1, 12), 2) : (month.equals("No Month") ? "" : format + addZero(Integer.parseInt(month), 2)));
                str += (year.equals("Random") ? format + addZero(random(currentYear, currentYear + 10), 4) : (year.equals("No Year") ? "" : format + addZero(Integer.parseInt(year), 4)));
                str += (cvv.equals("Random") ? format + addZero(random(1, 999), n == 34 || n == 37 ? 4 : 3) : (cvv.equals("No Cvv") ? "" : format + addZero(Integer.parseInt(cvv), n == 34 || n == 37 ? 4 : 3)));
                result.add(str);
                i++;
            }
        }
        return result;
    }


    @NonNull
    public static Card findType(@NonNull String cardNumber) {
        if (Pattern.compile("^(4)[0-9]*$").matcher(cardNumber).matches()) {
            return new Card(cardNumber, "Visa", new int[]{4, 8, 12}, new int[]{16, 18, 19}, "CVV", new int[]{3}, R.drawable.ic_visa);
        } else if (Pattern.compile("^(2221|2222|2223|2224|2225|2226|2227|2228|2229|222|223|224|225|226|227|228|229|23|24|25|26|270|271|2720|50|51|52|53|54|55|56|57|58|59|67)[0-9]*$").matcher(cardNumber).matches()) {
            return new Card(cardNumber, "MasterCard", new int[]{4, 8, 12}, new int[]{16, 18, 19}, "CVC", new int[]{3}, R.drawable.ic_mastercard);
        } else if (Pattern.compile("^(34|37)[0-9]*$").matcher(cardNumber).matches()) {
            return new Card(cardNumber, "American Express", new int[]{4, 10}, new int[]{15}, "CID", new int[]{3, 4}, R.drawable.ic_amex);
        } else if (Pattern.compile("^(60|64|65)[0-9]*$").matcher(cardNumber).matches()) {
            return new Card(cardNumber, "Discover", new int[]{4, 8, 12}, new int[]{16, 18, 19}, "CID", new int[]{3}, R.drawable.ic_discover);
        } else if (Pattern.compile("^(352[89]|35[3-8][0-9])[0-9]*$").matcher(cardNumber).matches()) {
            return new Card(cardNumber, "JCB", new int[]{4, 8, 12}, new int[]{16, 18, 19}, "CVV", new int[]{3}, R.drawable.ic_jcb);
        } else if (Pattern.compile("^(36)[0-9]*$").matcher(cardNumber).matches()) {
            return new Card(cardNumber, "Diners Club", new int[]{4, 10}, new int[]{14}, "CVV", new int[]{3}, R.drawable.ic_diners);
        } else if (Pattern.compile("^(30|38|39)[0-9]*$").matcher(cardNumber).matches()) {
            return new Card(cardNumber, "Diners Club", new int[]{4, 10}, new int[]{16}, "CVV", new int[]{3}, R.drawable.ic_diners);
        } else if (Pattern.compile("^(62|81)[0-9]*$").matcher(cardNumber).matches()) {
            return new Card(cardNumber, "UnionPay", new int[]{4, 8, 12}, new int[]{16, 18, 19}, "CVN", new int[]{3}, R.drawable.ic_unionpay);
        } else {
            return new Card(cardNumber, "Unknown", new int[]{4, 8, 12}, new int[]{16}, "CVV", new int[]{3}, R.drawable.ic_unknown);
        }
    }
}
