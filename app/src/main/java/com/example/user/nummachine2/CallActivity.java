package com.example.user.nummachine2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.Utils.DialogUtil;
import com.example.user.adapter.WaitNumAdapter;

import java.util.List;

public class CallActivity extends AppCompatActivity {

    Button conBtn, takeBtn;
    private CallFragment.OnFragmentInteractionListener mListener;
    private String storeName;
    private List<WaitNumAdapter.WaitNumberArray> waitNumberArrayList;

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

        storeName = getIntent().getStringExtra("storeName");

        //取得目前排隊號碼
        URLtool urlTool;
        urlTool = new URLtool(URLtool.getUrlWaitNumber(storeName), this);
        Log.d("result", URLtool.getUrlWaitNumber(storeName));
        urlTool.setOnCompleted(new URLtool.OnCompletedListener() {
            @Override
            public void OnCompleted(String httpResult) {
                Log.d("result", httpResult);
                String[] httpStatus = httpResult.split("/");

                if(httpStatus[0].equals("200")) {
                    String[] result = URLtool.getWaitNum(httpStatus[1]);
                    //依序將等待號碼傳入Adapter，取得等待號碼處理次數
                    int processRound = result.length / 5;
                    int procerrTime = result.length / 5;


                } else {

                }

            }
        });

        urlTool.execute();
    }

    private void processWaitNumber(int processRound, int processNumber, String[] oriWaitNumber) {
        int oriArrayCount = 0;
        while (processRound != 0) {
            for(int i=0; i<=5; i++) {
                String[] tmpWaitNumber = new String[5];
                tmpWaitNumber[0] = oriWaitNumber[oriArrayCount*5];
                tmpWaitNumber[1] = oriWaitNumber[oriArrayCount*5 + 1];
                tmpWaitNumber[2] = oriWaitNumber[oriArrayCount*5 + 2];
                tmpWaitNumber[3] = oriWaitNumber[oriArrayCount*5 + 3];
                tmpWaitNumber[4] = oriWaitNumber[oriArrayCount*5 + 4];

                WaitNumAdapter.WaitNumberArray waitNumberArray = null;
                waitNumberArray.setWaitNumber(tmpWaitNumber);
                waitNumberArrayList.add(waitNumberArray);

            }
            oriArrayCount++;
            processRound--;

            if(processNumber != 0) {
                String[] tmpWaitNumberTail = new String[5];
                for(int i=0; i<=processNumber; i++) {
                    if (oriWaitNumber[5*processRound + i] != null) {
                        tmpWaitNumberTail[i] = oriWaitNumber[5*processRound + i];
                    }

                }

                WaitNumAdapter.WaitNumberArray waitNumberArray = null;
                waitNumberArray.setWaitNumber(tmpWaitNumberTail);
                waitNumberArrayList.add(waitNumberArray);
            }
        }
        

    }
}
