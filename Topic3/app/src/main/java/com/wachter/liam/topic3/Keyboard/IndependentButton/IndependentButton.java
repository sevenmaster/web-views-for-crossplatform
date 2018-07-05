package com.wachter.liam.topic3.Keyboard.IndependentButton;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.wachter.liam.topic3.LoggingTags;

/**
 * Independent button that reports its inputs to the activity it belongs to.
 * Possible inputs are a click on the button or a speech input with the most likely result
 * being the text on the button.
 */
public class IndependentButton extends AppCompatButton implements View.OnClickListener{

    public IndependentButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(this);
    }
    public IndependentButton(Context context){
        this(context, null, android.support.v7.appcompat.R.attr.buttonStyle);
    }
    public IndependentButton(Context context, AttributeSet attrs){
        this(context, attrs, android.support.v7.appcompat.R.attr.buttonStyle);
    }
    @Override
    public void onClick(View v) {
        if (v.getContext() instanceof IndependentButtonListener){
            IndependentButtonListener toNotify = (IndependentButtonListener) v.getContext();
            switch (getText().toString()){
                case "DEL":
                    toNotify.delete();
                    break;
                case "CLR":
                    toNotify.clear();
                    break;
                case "=":
                    toNotify.evaluate();
                    break;
                case "√":
                    toNotify.clickOn("sqrt(");
                    break;
                case "^":
                    toNotify.clickOn("pow(");
                    break;
                default:
                    toNotify.clickOn(getText().toString());
            }
        }
        Log.i(LoggingTags.getButtons(), getText().toString());
    }

}