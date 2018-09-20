package com.example.user.nummachine2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.Utils.DialogUtil;
import com.example.user.adapter.WaitNumAdapter;

import java.util.ArrayList;
import java.util.List;

public class CallActivity extends AppCompatActivity {

    private Button conBtn, takeBtn;
    private RecyclerView waitNumRcv;
    private CallFragment.OnFragmentInteractionListener mListener;
    private String storeName;
    private List<WaitNumAdapter.WaitNumberArray> waitNumberArrayList;
    private WaitNumAdapter.WaitNumCallback waitNumCallback;

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
        waitNumRcv = (RecyclerView) findViewById(R.id.waitNumRcv);

        //按鈕變色
        conBtn.setOnTouchListener(conBtntouch);
        takeBtn.setOnTouchListener(takeBtntouch);

        waitNumberArrayList = new ArrayList<>();
        storeName = getIntent().getStringExtra("storeName");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        waitNumRcv.setLayoutManager(layoutManager);

        reFresh();
    }

    private void processWaitNumber(int processRound, int processNumber, String[] oriWaitNumber) {
        int oriArrayCount = 0;

        for(int i=0; i<processRound; i++) {
            List<String> tmpWaitNumber = new ArrayList<String>();
            tmpWaitNumber.add(oriWaitNumber[i*5]);
            tmpWaitNumber.add(oriWaitNumber[i*5 + 1]);
            tmpWaitNumber.add(oriWaitNumber[i*5 + 2]);
            tmpWaitNumber.add(oriWaitNumber[i*5 + 3]);
            tmpWaitNumber.add(oriWaitNumber[i*5 + 4]);

            WaitNumAdapter.WaitNumberArray waitNumberArray = new WaitNumAdapter.WaitNumberArray();
            waitNumberArray.setWaitNumber(tmpWaitNumber);
            waitNumberArrayList.add(waitNumberArray);

        }

        if(processNumber != 0) {
            List<String> tmpWaitNumberTail = new ArrayList<String>();
            for(int i=0; i<processNumber; i++) {
                tmpWaitNumberTail.add(oriWaitNumber[5*processRound + i]);
            }

            WaitNumAdapter.WaitNumberArray waitNumberArray = new WaitNumAdapter.WaitNumberArray();
            waitNumberArray.setWaitNumber(tmpWaitNumberTail);
            waitNumberArrayList.add(waitNumberArray);
        }
    }

    private void reFresh() {
        final URLtool urlTool;
        urlTool = new URLtool(URLtool.getUrlWaitNumber(storeName), this);
        urlTool.setOnCompleted(new URLtool.OnCompletedListener() {
            @Override
            public void OnCompleted(String httpResult) {
            Log.d("result", httpResult);
            String[] httpStatus = httpResult.split("/");

            if(httpStatus[0].equals("200")) {
                String[] result = URLtool.getWaitNum(httpStatus[1]);
                String[] resultRemoved = new String[result.length-1];

                System.arraycopy(result, 1, resultRemoved, 0, result.length-1);

                for(int i=0; i<resultRemoved.length; i++) {

                }

                //依序將等待號碼傳入Adapter，取得等待號碼處理次數
                int processRound = resultRemoved.length / 5;
                int procerrTime = resultRemoved.length % 5;

                processWaitNumber(processRound, procerrTime, resultRemoved);
                WaitNumAdapter waitNumAdapter = new WaitNumAdapter(CallActivity.this, waitNumberArrayList, new WaitNumAdapter.WaitNumCallback() {
                    @Override
                    public void onCallBack(String result) {
                        Log.d("result", result);
                        URLtool urlTool2 = new URLtool(URLtool.getUrlCallNumber(storeName, result), CallActivity.this);
                        urlTool2.setOnCompleted(new URLtool.OnCompletedListener() {
                            @Override
                            public void OnCompleted(String httpResult) {
                                waitNumberArrayList.clear();
                                reFresh();
                            }
                        });

                        urlTool2.execute();

                    }
                });
                waitNumRcv.setAdapter(waitNumAdapter);

            } else {

            }

            }
        });

        urlTool.execute();
    }
}
