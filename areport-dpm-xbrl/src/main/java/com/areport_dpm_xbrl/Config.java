package com.areport_dpm_xbrl;

import java.util.HashMap;
import java.util.Map;

public class Config {
    public static String monetaryItem = "EUR";

    public static Map<String, String> lang = new HashMap<>() {{
        put("0", "en");
        put("1", "bs-Latn-BA");
        put("2", "ba");
    }};

    public static Map<String, String> confSet = new HashMap<String, String>() {{
        put("lab-codes", "lab-codes");
        put("rend", "rend");
        put("def", "def");
        put("pre", "pre");
        put("tab", "tab");
    }};

    public static Map<String, String> moduleSet = new HashMap<String, String>() {{
        put("pre", "pre");
        put("rend", "rend");
        put("lab-codes", "lab-codes");
    }};

    public static Map<String, String> createInstance = new HashMap<String, String>() {{
        put("rend", "rend");
        put("def", "def");
    }};

    public static String owner = "www.eba.europa.eu";

    public static String publicDir() {
        return storagePath("app/public/tax/");
    }

    public static String prefixOwner = "fba";

    public static String setLogoPath() {
        return publicPath() + java.io.File.separator + "images" + java.io.File.separator + "logo.svg";
    }

    public static Map<String, Map<String, String>> owners() {
        Map<String, Map<String, String>> owners = new HashMap<>();

        Map<String, String> fba = new HashMap<>();
        fba.put("namespace", "http://www.fba.ba");
        fba.put("officialLocation", "http://www.fba.ba/xbrl");
        fba.put("prefix", "fba");
        fba.put("copyright", "(C) FBA");
        owners.put("fba", fba);

        Map<String, String> eba = new HashMap<>();
        eba.put("namespace", "http://www.eba.europa.eu/xbrl/crr");
        eba.put("officialLocation", "http://www.eba.europa.eu/eu/fr/xbrl/crr");
        eba.put("prefix", "eba");
        eba.put("copyright", "(C) EBA");
        owners.put("eba", eba);

        Map<String, String> audt = new HashMap<>();
        audt.put("namespace", "http://www.auditchain.finance/");
        audt.put("officialLocation", "http://www.auditchain.finance/fr/dpm");
        audt.put("prefix", "audt");
        audt.put("copyright", "(C) Auditchain");
        owners.put("audt", audt);

        return owners;
    }

//    public static String tmpPdfDir() {
//        return storagePath() + java.io.File.separator + "logs" + java.io.File.separator;
//    }

    private static String storagePath(String path) {
        // Implement this method to return the storage path
        return "path/to/storage/" + path;
    }

    private static String publicPath() {
        // Implement this method to return the public path
        return "path/to/public";
    }
}
