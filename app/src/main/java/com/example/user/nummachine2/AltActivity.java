package com.example.user.nummachine2;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class AltActivity extends AppCompatActivity {

    TextView uploadBtn, modBtn;

    //Touch物件，控制按鈕變色
    TextView.OnTouchListener TVtouch = new TextView.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.e("touch", "down");
                uploadBtn.setTextColor(Color.parseColor("#dddddd"));
                uploadBtn.setBackgroundResource(R.drawable.titledown);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.e("touch", "up");
                uploadBtn.setTextColor(Color.parseColor("#000000"));
                uploadBtn.setBackgroundResource(R.drawable.title);
            }
            return true;
        }
    };

    //Touch物件，控制按鈕變色
    TextView.OnTouchListener TVtouch2 = new TextView.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                //modBtn.setTextColor(Color.parseColor("#dddddd"));
                modBtn.setBackgroundResource(R.drawable.titledown);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                //modBtn.setTextColor(Color.parseColor("#000000"));
                modBtn.setBackgroundResource(R.drawable.title);
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alt);

        modBtn = (TextView) findViewById(R.id.textView13);

        //按鈕變色

        modBtn.setOnTouchListener(TVtouch2);
    }
}
