package com.assets.binfinder.util;

import android.content.Context;

import com.assets.binfinder.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.Map;

public class CountryDetails {
    private static Map<String, Country> countryMap;


    public static void initialize(Context context) {
        if (countryMap == null) {
            String json = FileUtils.readJsonFromAssets(context, "countries.json");
            Gson gson = new Gson();
            countryMap = gson.fromJson(json, new TypeToken<Map<String, Country>>() {}.getType());
        }
    }

    public static Country getData(String alpha2Code) {
        if (countryMap != null) {
            return countryMap.get(alpha2Code);
        }
        return null;
    }

    public static class Country {
        private String name;
        private String alpha2;
        private String alpha3;
        private String capital;
        private String domain;
        private String emoji;
        private double latitude;
        private double longitude;
        private String continentCode;
        private String region;
        private String subregion;
        private String language;
        private String languagealpha2;
        private String languagealpha3;
        private String currencySymbol;
        private String currencyCode;
        private String currencyName;
        private boolean developed;
        private int callingCode;
        private int numeric;
        private String startofWeek;
        private String[] languages;
        private String[] currencies;
        private String postalCodeFormat;
        private String[] timezones;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAlpha2() {
            return alpha2;
        }

        public void setAlpha2(String alpha2) {
            this.alpha2 = alpha2;
        }

        public String getAlpha3() {
            return alpha3;
        }

        public void setAlpha3(String alpha3) {
            this.alpha3 = alpha3;
        }

        public String getCapital() {
            return capital;
        }

        public void setCapital(String capital) {
            this.capital = capital;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getEmoji() {
            return emoji;
        }

        public void setEmoji(String emoji) {
            this.emoji = emoji;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getContinentCode() {
            return continentCode;
        }

        public void setContinentCode(String continentCode) {
            this.continentCode = continentCode;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getSubregion() {
            return subregion;
        }

        public void setSubregion(String subregion) {
            this.subregion = subregion;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getLanguagealpha2() {
            return languagealpha2;
        }

        public void setLanguagealpha2(String languagealpha2) {
            this.languagealpha2 = languagealpha2;
        }

        public String getLanguagealpha3() {
            return languagealpha3;
        }

        public void setLanguagealpha3(String languagealpha3) {
            this.languagealpha3 = languagealpha3;
        }

        public String getCurrencySymbol() {
            return currencySymbol;
        }

        public void setCurrencySymbol(String currencySymbol) {
            this.currencySymbol = currencySymbol;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getCurrencyName() {
            return currencyName;
        }

        public void setCurrencyName(String currencyName) {
            this.currencyName = currencyName;
        }

        public boolean isDeveloped() {
            return developed;
        }

        public void setDeveloped(boolean developed) {
            this.developed = developed;
        }

        public int getCallingCode() {
            return callingCode;
        }

        public void setCallingCode(int callingCode) {
            this.callingCode = callingCode;
        }

        public int getNumeric() {
            return numeric;
        }

        public void setNumeric(int numeric) {
            this.numeric = numeric;
        }

        public String getStartofWeek() {
            return startofWeek;
        }

        public void setStartofWeek(String startofWeek) {
            this.startofWeek = startofWeek;
        }

        public String[] getLanguages() {
            return languages;
        }

        public void setLanguages(String[] languages) {
            this.languages = languages;
        }

        public String[] getCurrencies() {
            return currencies;
        }

        public void setCurrencies(String[] currencies) {
            this.currencies = currencies;
        }

        public String getPostalCodeFormat() {
            return postalCodeFormat;
        }

        public void setPostalCodeFormat(String postalCodeFormat) {
            this.postalCodeFormat = postalCodeFormat;
        }

        public String[] getTimezones() {
            return timezones;
        }

        public void setTimezones(String[] timezones) {
            this.timezones = timezones;
        }
    }
}
