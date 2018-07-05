package com.wachter.liam.topic3;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class TextSynthesis {

    private TextToSpeech textToSpeech;
    private int ttsID;

    private static final Map<String, String> operationNames;
    static
    {
        operationNames = new HashMap<>();
        operationNames.put("sqrt(", "Wurzel");
        operationNames.put("*", "Mal");
        operationNames.put("pow(", "Potenz");
        operationNames.put("ln(", "Logarithmus");
        operationNames.put("/", "durch");
        operationNames.put("-", "Minus");
        operationNames.put("%", "modulo");
    }

    public TextSynthesis(Context context){
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Log.i(LoggingTags.getTTS(), "Finished loading TTS");
                textToSpeech.setLanguage(Locale.GERMANY);
                // To check what was chosen closest to German eg. "de-CH-language"
                Log.i(LoggingTags.getTTS(),
                        "Picked available language: " + textToSpeech.getVoice().getName());
            }
        });
    }

    public void speak(String text, float... speed) {
        Iterator it = operationNames.entrySet().iterator();
        //noinspection WhileLoopReplaceableByForEach
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if (text.equals(pair.getKey()))
                text = (String) pair.getValue();
        }
        if(text.contains(".") && !(text.equals("."))){
            text = text.substring(2, text.length());
            double fpn = Double.parseDouble(text);
            Log.i(LoggingTags.getTTS(), Double.toString(fpn));
            fpn = Math.round(fpn * 100.0) / 100.0;
            text = Double.toString(fpn);
            text = "= ".concat(text);
        }
        if (speed.length == 0){
            textToSpeech.setSpeechRate(1.0f);
        }
        else {
            textToSpeech.setSpeechRate(speed[0]);
        }
        Bundle channelSettings = new Bundle();
        channelSettings.putInt(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_STREAM,
                AudioManager.STREAM_MUSIC);
        textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, channelSettings,
                Integer.toString(ttsID++));

        /* For now just for logging purposes. */
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                Log.i(LoggingTags.getTTS(), "Speaking: " + utteranceId);
            }

            @Override
            public void onDone(String utteranceId) {
                Log.i(LoggingTags.getTTS(), "Successfully spoke:" + utteranceId);
            }

            @Override
            public void onError(String utteranceId) {
                Log.i(LoggingTags.getTTS(), "An Error occurred during" + utteranceId);
            }
        });
    }

    public void clearQueue(){
        textToSpeech.speak("", TextToSpeech.QUEUE_FLUSH, new Bundle(),
                Integer.toString(ttsID++));
    }
}