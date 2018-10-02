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
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.Utils.DialogUtil;
import com.example.user.adapter.WaitNumAdapter;

import java.util.ArrayList;
import java.util.List;

public class CallActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText takeEdt;
    private TextView conNum_tv;
    private Button conBtn, takeBtn;
    private RecyclerView waitNumRcv;
    private CallFragment.OnFragmentInteractionListener mListener;
    private String storeName;
    private List<WaitNumAdapter.WaitNumberArray> waitNumberArrayList;
    private WaitNumAdapter.WaitNumCallback waitNumCallback;
    private URLtool urlToolCallNum, urlToolgetNum;

    //Touch物件，控制按鈕變色
    TextView.OnTouchListener conBtntouch = new TextView.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                conBtn.setBackgroundResource(R.drawable.callbtndown);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                conBtn.setBackgroundResource(R.drawable.callbtn);
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
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                takeBtn.setBackgroundResource(R.drawable.callbtn);
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
        takeEdt = (EditText) findViewById(R.id.editView6);
        waitNumRcv = (RecyclerView) findViewById(R.id.waitNumRcv);
        conNum_tv = (TextView) findViewById(R.id.textView6);

        //按鈕變色
        conBtn.setOnTouchListener(conBtntouch);
        takeBtn.setOnTouchListener(takeBtntouch);

        //設定按鍵事件
        conBtn.setOnClickListener(this);
        takeBtn.setOnClickListener(this);
        takeEdt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    takeEdt.setText("");
                }
                return false;
            }
        });

        waitNumberArrayList = new ArrayList<>();
        storeName = getIntent().getStringExtra("storeName");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        waitNumRcv.setLayoutManager(layoutManager);

        reFresh();
    }

    private void processWaitNumber(int processRound, int processNumber, String[] oriWaitNumber) {

        //處理含有完整 1 processRound的等待號碼處理
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

        //處理不滿1個 processRound的等待號碼處理
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
        urlToolgetNum = new URLtool(URLtool.getUrlWaitNumber(storeName), this);
        urlToolgetNum.setOnCompleted(new URLtool.OnCompletedListener() {
            @Override
            public void OnCompleted(String httpResult) {
                Log.d("result", httpResult);
                String[] httpStatus = httpResult.split("/");

                if(httpStatus[0].equals("200")) {
                    //因為正則表示式的關係，利用空格String Array會多一項，要刪除掉
                    String[] result = URLtool.getWaitNum(httpStatus[1]);
                    String[] resultRemoved = new String[result.length-1];

                    System.arraycopy(result, 1, resultRemoved, 0, result.length-1);

                    //設定依序叫號號碼
                    setConNum(resultRemoved[0]);

                    //依序將等待號碼傳入Adapter，取得等待號碼處理次數
                    int processRound = resultRemoved.length / 5;
                    int procerrTime = resultRemoved.length % 5;

                    processWaitNumber(processRound, procerrTime, resultRemoved);
                    WaitNumAdapter waitNumAdapter = new WaitNumAdapter(CallActivity.this, waitNumberArrayList, new WaitNumAdapter.WaitNumCallback() {
                        @Override
                        public void onCallBack(String result) {
                            Log.d("result", result);
                            urlToolCallNum = new URLtool(URLtool.getUrlCallNumber(storeName, result), CallActivity.this);
                            urlToolCallNum.setOnCompleted(new URLtool.OnCompletedListener() {
                                @Override
                                public void OnCompleted(String httpResult) {
                                    waitNumberArrayList.clear();
                                    reFresh();
                                }
                            });

                            urlToolCallNum.execute();

                        }
                    });
                    waitNumRcv.setAdapter(waitNumAdapter);

                }

            }
        });

        urlToolgetNum.execute();
    }

    private void setConNum(String conNum) {
        int takeNum = Integer.valueOf(conNum);
        if(takeNum < 10) {
            conNum_tv.setText("00" + String.valueOf(takeNum));
        } else if(takeNum < 100) {
            conNum_tv.setText("0" + String.valueOf(takeNum));
        } else {
            conNum_tv.setText(String.valueOf(takeNum));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button4: //自訂叫號
//                if(!takeEdt.getText().toString().equals("")) {
//                    int takeEdtInt = Integer.valueOf(takeEdt.getText().toString());
//                    String takeNum;
//
//                    if(takeEdtInt < 10) {
//                        takeNum = "0" + String.valueOf(takeEdtInt);
//                    } else {
//                        takeNum = String.valueOf(takeEdtInt);
//                    }
//
//                    urlToolCallNum = new URLtool(URLtool.getUrlCallNumber(storeName, takeNum), CallActivity.this);
//                    urlToolCallNum.setOnCompleted(new URLtool.OnCompletedListener() {
//                        @Override
//                        public void OnCompleted(String httpResult) {
//                            waitNumberArrayList.clear();
//                            reFresh();
//                        }
//                    });
//
//                    urlToolCallNum.execute();
//                    takeEdt.setText("");
//                }
                SocketTool.createSocket();

                break;

            case R.id.button6: //依序叫號
                int conNumInt = Integer.valueOf(conNum_tv.getText().toString());
                String conNum;

                if(conNumInt < 10) {
                    conNum = "0" + String.valueOf(conNumInt);
                } else {
                    conNum = String.valueOf(conNumInt);
                }

                urlToolCallNum = new URLtool(URLtool.getUrlCallNumber(storeName, conNum), CallActivity.this);
                urlToolCallNum.setOnCompleted(new URLtool.OnCompletedListener() {
                    @Override
                    public void OnCompleted(String httpResult) {
                        waitNumberArrayList.clear();
                        reFresh();
                    }
                });

                urlToolCallNum.execute();
                break;
        }
    }
}
