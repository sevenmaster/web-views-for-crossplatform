package com.wachter.liam.topic3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.wachter.liam.topic3.Keyboard.KeyboardLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MyWebView extends WebView {

    private MainActivity mainActivity;

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void Initialize(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        getSettings().setJavaScriptEnabled(true);
        getSettings().setDisplayZoomControls(false);
        getSettings().setBuiltInZoomControls(false);
        getSettings().setSupportZoom(false);
        addJavascriptInterface(this, "Application");
        setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
                /*
                try {
                    handleAlert(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                */
                result.confirm();
                return true;
            }
        });
    }

    @JavascriptInterface
    public void sendAlert(final String message) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(LoggingTags.getWebView(), message);
                    handleAlert(message);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        mainHandler.post(myRunnable);
    }

    protected void handleAlert(String message) throws JSONException {
        if (message.startsWith("=")){
            mainActivity.getTextSynthesis().speak(message);
            return;
        }
        JSONObject keyboard = null;
        boolean exception = false;
        Log.i(LoggingTags.getWebView(), message);
        try {
            keyboard = new JSONObject(message);
        } catch (JSONException e) {
            exception = true;
            e.printStackTrace();
        }
        if (!exception) {
            KeyboardLayout keyboardLayout = new KeyboardLayout();
            ArrayList<ArrayList<String>> texts = new ArrayList<>();
            ArrayList<ArrayList<String>> colors = new ArrayList<>();
            JSONArray content = keyboard.getJSONArray("content");
            for (int j = 0; j < content.length(); j++) {
                JSONArray line = content.getJSONArray(j);
                ArrayList<String> textLine = new ArrayList<>();
                ArrayList<String> colorLine = new ArrayList<>();
                for (int i = 0; i < line.length(); i++) {
                    JSONObject button = line.getJSONObject(i);
                    String text = button.getString("text");
                    String color = button.getString("color");
                    textLine.add(text);
                    colorLine.add(color);
                }
                texts.add(textLine);
                colors.add(colorLine);
            }
            keyboardLayout.setButtonText(texts);
            keyboardLayout.setButtonColor(colors);
            mainActivity.newKeyboard(keyboardLayout);
        }
    }

    private String loadJavascriptString(String fileName, String... args)
    {
        AssetManager assetManager = mainActivity.getAssets();
        StringBuilder builder = new StringBuilder();
        builder.append("javascript: (");

        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open("JavaScript/" + fileName)));
            String line;
            while ((line = reader.readLine()) != null)
                builder.append(line).append("\n");

            builder.append(")(");
            for (int i = 0; i < args.length; i++)
            {
                builder.append("\"").append(args[i]).append("\"");
                if (i < args.length - 1)
                    builder.append(", ");
            }
            builder.append(");");

            return builder.toString();
        } catch (IOException e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public void writeInTextBox(String content)
    {
        loadUrl(loadJavascriptString("PerformInputInWebView.js", "mathExpression", content));
    }

    public void evaluate(){
        loadUrl("javascript:(doCalculation())");
    }
}
