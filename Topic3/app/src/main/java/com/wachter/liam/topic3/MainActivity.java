package com.wachter.liam.topic3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.wachter.liam.topic3.Keyboard.IndependentButton.IndependentButtonListener;
import com.wachter.liam.topic3.Keyboard.KeyboardCreator;
import com.wachter.liam.topic3.Keyboard.KeyboardLayout;

public class MainActivity extends AppCompatActivity implements IndependentButtonListener {

    private TextSynthesis textSynthesis;
    private MyWebView webView;
    private String content = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textSynthesis = new TextSynthesis(getApplicationContext());
        initializeWebView();
    }

    @Override
    protected void onPause(){
        super.onPause();
        textSynthesis.clearQueue();
    }

    private void initializeWebView(){
        webView = findViewById(R.id.WebView);
        webView.Initialize(this);
        webView.loadUrl(getString(R.string.url));
    }

    public void newKeyboard(KeyboardLayout keyboardLayout){
        KeyboardCreator keyboardCreator = new KeyboardCreator(keyboardLayout);
        keyboardCreator.applyLayout(this);
    }

    @Override
    public void clickOn(String buttonText) {
        textSynthesis.speak(buttonText);
        content =  content.concat(buttonText);
        webView.writeInTextBox(content);
    }

    @Override
    public void delete() {
        textSynthesis.speak(getString(R.string.delete_speech), 2);
        if(content.length() > 0) {
            content = content.subSequence(0, content.length() - 1).toString();
            webView.writeInTextBox(content);
        }
    }

    @Override
    public void clear() {
        textSynthesis.speak(getString(R.string.delete), 2);
        content = "";
        webView.writeInTextBox("");
    }

    @Override
    public void evaluate() {
        webView.evaluate();
    }

    public TextSynthesis getTextSynthesis() {
        return textSynthesis;
    }
}