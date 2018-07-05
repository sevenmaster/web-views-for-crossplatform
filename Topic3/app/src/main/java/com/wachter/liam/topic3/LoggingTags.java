package com.wachter.liam.topic3;

public class LoggingTags {
    private final static String TTS = "Text To Speech";
    private final static String Buttons = "Buttons";
    private final static String WebView = "Web View";
    private final static String LoggingPrefix = "My Logging: ";

    public static String getTTS() {
        return LoggingPrefix + TTS;
    }

    public static String getButtons() {
        return LoggingPrefix + Buttons;
    }

    public static String getWebView(){return LoggingPrefix + WebView;}

}
