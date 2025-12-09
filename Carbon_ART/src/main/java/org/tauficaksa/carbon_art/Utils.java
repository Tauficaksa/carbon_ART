package org.tauficaksa.carbon_art;


public class Utils {
    public static String normalizeUrl(String text) {
        if (text.startsWith("http://") || text.startsWith("https://")) return text;
        return "https://" + text;
    }


    public static String shortTitle(String url) {
        if (url == null || url.isBlank()) return "New Tab";
        try {
            return new java.net.URL(url).getHost();
        } catch (Exception e) {
            return truncate(url, 20);
        }
    }


    public static String truncate(String s, int n) {
        if (s == null) return "";
        return s.length() <= n ? s : s.substring(0, n-1) + "…";
    }
}