package com.example.user.nummachine2;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CallActivity extends AppCompatActivity {

    Button conBtn, takeBtn;
    private CallFragment.OnFragmentInteractionListener mListener;

    //Touch物件，控制按鈕變色
    TextView.OnTouchListener conBtntouch = new TextView.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                conBtn.setBackgroundResource(R.drawable.callbtndown);
                //conBtn.setTextColor(Color.parseColor("#dddddd"));
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                conBtn.setBackgroundResource(R.drawable.callbtn);
                //conBtn.setTextColor(Color.parseColor("#000000"));
            }
            return false;
        }
    };

    //Touch物件，控制按鈕變色
    TextView.OnTouchListener takeBtntouch = new TextView.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                takeBtn.setBackgroundResource(R.drawable.callbtndown);
                //takeBtn.setTextColor(Color.parseColor("#dddddd"));
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                takeBtn.setBackgroundResource(R.drawable.callbtn);
                //takeBtn.setTextColor(Color.parseColor("#000000"));
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        conBtn = (Button) findViewById(R.id.button4);
        takeBtn = (Button) findViewById(R.id.button6);
        //按鈕變色
        conBtn.setOnTouchListener(conBtntouch);
        takeBtn.setOnTouchListener(takeBtntouch);
    }
}
