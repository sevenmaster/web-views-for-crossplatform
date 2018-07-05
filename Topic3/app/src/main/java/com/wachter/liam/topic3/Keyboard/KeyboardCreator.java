package com.wachter.liam.topic3.Keyboard;

import android.graphics.PorterDuff;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.widget.GridLayout;

import com.wachter.liam.topic3.Keyboard.IndependentButton.IndependentButton;
import com.wachter.liam.topic3.LoggingTags;
import com.wachter.liam.topic3.MainActivity;
import com.wachter.liam.topic3.R;

import java.util.ArrayList;

public class KeyboardCreator {
    private KeyboardLayout keyboardLayout;

    public KeyboardCreator(KeyboardLayout keyboardLayout){
        this.keyboardLayout = keyboardLayout;
    }

    public void applyLayout(MainActivity mainActivity){
        GridLayout gridLayout = mainActivity.findViewById(R.id.Grid);
        assert gridLayout != null;
        gridLayout.removeAllViews();
        ArrayList<ArrayList<String>> texts = keyboardLayout.getButtonText();
        ArrayList<ArrayList<String>> colors = keyboardLayout.getButtonColor();

        if (!validateSize(texts, colors)) throw new Error("Texts and Colors are not the same size");
        gridLayout.setRowCount(texts.get(0).size());
        gridLayout.setColumnCount(texts.size());

        for (int i = 0; i < texts.size(); i++) {
            ArrayList<String> textLine = texts.get(i);
            ArrayList<String> colorLine = colors.get(i);
            for (int j = 0; j < textLine.size(); j++){
                IndependentButton button = new IndependentButton(mainActivity);
                button.setText(textLine.get(j));
                switch (colorLine.get(j).toLowerCase()){
                    case "red":
                        button.setBackgroundTintList(mainActivity.getResources().getColorStateList(
                                mainActivity.getResources().getIdentifier("@android:color/holo_red_dark", "color", "android")
                        ));
                        break;
                    case "blue":
                        button.setBackgroundTintList(mainActivity.getResources().getColorStateList(
                                mainActivity.getResources().getIdentifier("@android:color/holo_blue_light", "color", "android")
                        ));
                        break;
                    case "green":
                        button.setBackgroundTintList(mainActivity.getResources().getColorStateList(
                                mainActivity.getResources().getIdentifier("@android:color/holo_green_light", "color", "android")
                        ));
                        break;
                    default:
                        button.setBackgroundTintList(mainActivity.getResources().getColorStateList(
                                mainActivity.getResources().getIdentifier("@android:color/holo_orange_dark", "color", "android")
                        ));
                        break;
                }
                button.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                button.setTextColor(mainActivity.getResources().getColorStateList(
                        mainActivity.getResources().getIdentifier(
                                "@android:color/white",
                                "color",
                                "android")));
                button.setId(i * 10 + j);
                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                layoutParams.width = GridLayout.LayoutParams.WRAP_CONTENT;
                layoutParams.height = 200;
                layoutParams.rowSpec = GridLayout.spec(j, 1, 1f);
                layoutParams.columnSpec = GridLayout.spec(i, 1, 1f);
                button.setLayoutParams(layoutParams);
                TextViewCompat.setAutoSizeTextTypeWithDefaults(button, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
                gridLayout.addView(button);
            }
        }
    }

    private boolean validateSize(ArrayList<ArrayList<String>> texts, ArrayList<ArrayList<String>> colors){
        int textCount = 0;
        int colorCount = 0;
        for (ArrayList<String> textLine : texts){
            textCount += textLine.size();
        }
        for (ArrayList<String> colorLine : colors){
            colorCount += colorLine.size();
        }
        Log.i(LoggingTags.getButtons(), Integer.toString(textCount)+ ", " + Integer.toString(colorCount));
        return textCount == colorCount;
    }
}