package com.wachter.liam.topic3.Keyboard;

import java.util.ArrayList;

public class KeyboardLayout {
    private ArrayList<ArrayList<String>> buttonText;
    private ArrayList<ArrayList<String>> buttonColor;

    public ArrayList<ArrayList<String>> getButtonText() {
        return buttonText;
    }

    public void setButtonText(ArrayList<ArrayList<String>> buttonText) {
        this.buttonText = buttonText;
    }

    public ArrayList<ArrayList<String>> getButtonColor() {
        return buttonColor;
    }

    public void setButtonColor(ArrayList<ArrayList<String>> buttonColor) {
        this.buttonColor = buttonColor;
    }
}
