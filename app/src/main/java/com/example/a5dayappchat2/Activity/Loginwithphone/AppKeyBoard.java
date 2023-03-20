package com.example.a5dayappchat2.Activity.Loginwithphone;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.a5dayappchat2.R;

public class AppKeyBoard extends LinearLayout implements View.OnClickListener{


    public AppKeyBoard(Context context) {
        this(context, null, 0);
    }

    public AppKeyBoard(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppKeyBoard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ViewId(context, attrs);
    }



    private Button bt_Num1,bt_Num2,bt_Num3,bt_Num4,bt_Num5,bt_Num6,bt_Num7,bt_Num8,bt_Num9,bt_Num0;
    private ImageButton bt_Backspace;

    SparseArray<String> keyValues = new SparseArray<>();
    InputConnection inputConnection;

    private void ViewId(Context context, AttributeSet attrs){
        LayoutInflater.from(context).inflate(R.layout.keyboard, this, true);
        bt_Num1 = findViewById(R.id.bt_Num1);
        bt_Num2 = findViewById(R.id.bt_Num2);
        bt_Num3 = findViewById(R.id.bt_Num3);
        bt_Num4 = findViewById(R.id.bt_Num4);
        bt_Num5 = findViewById(R.id.bt_Num5);
        bt_Num6 = findViewById(R.id.bt_Num6);
        bt_Num7 = findViewById(R.id.bt_Num7);
        bt_Num8 = findViewById(R.id.bt_Num8);
        bt_Num9 = findViewById(R.id.bt_Num9);
        bt_Num0 = findViewById(R.id.bt_Num0);
        bt_Backspace = findViewById(R.id.bt_Backspace);

        bt_Num1.setOnClickListener(this);
        bt_Num2.setOnClickListener(this);
        bt_Num3.setOnClickListener(this);
        bt_Num4.setOnClickListener(this);
        bt_Num5.setOnClickListener(this);
        bt_Num6.setOnClickListener(this);
        bt_Num7.setOnClickListener(this);
        bt_Num8.setOnClickListener(this);
        bt_Num9.setOnClickListener(this);
        bt_Num0.setOnClickListener(this);
        bt_Backspace.setOnClickListener(this);

        keyValues.put(R.id.bt_Num1, "1");
        keyValues.put(R.id.bt_Num2, "2");
        keyValues.put(R.id.bt_Num3, "3");
        keyValues.put(R.id.bt_Num4, "4");
        keyValues.put(R.id.bt_Num5, "5");
        keyValues.put(R.id.bt_Num6, "6");
        keyValues.put(R.id.bt_Num7, "7");
        keyValues.put(R.id.bt_Num8, "8");
        keyValues.put(R.id.bt_Num9, "9");
        keyValues.put(R.id.bt_Num0, "0");

    }



    @Override
    public void onClick(View view) {

        if (inputConnection == null)
            return;

        if (view.getId() == R.id.bt_Backspace) {
            CharSequence selectedText = inputConnection.getSelectedText(0);
            if (TextUtils.isEmpty(selectedText)) {
                // no selection, so delete previous character
                inputConnection.deleteSurroundingText(1, 0);
            } else {
                // delete the selection
                inputConnection.commitText("", 1);
            }
        } else {
            String value = keyValues.get(view.getId());
            inputConnection.commitText(value, 1);
        }


    }

    public void setInputConnection(InputConnection ic) {
        this.inputConnection = ic;
    }

}
