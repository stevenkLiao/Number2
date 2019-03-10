package com.example.user.Activity;

import android.content.DialogInterface;
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
import com.example.user.Utils.URLUtil;
import com.example.user.Adapter.WaitNumAdapter;
import com.example.user.nummachine2.R;

import java.util.ArrayList;
import java.util.List;

public class CallActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText takeEdt;
    private TextView conNum_tv;
    private Button conBtn, takeBtn;
    private RecyclerView waitNumRcv;
    private String storeName;
    private List<WaitNumAdapter.WaitNumberArray> waitNumberArrayList;
    private URLUtil urlToolCallNum, urlToolgetNum;

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

        //宣告等待號碼用的List
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
        urlToolgetNum = new URLUtil(URLUtil.getUrlWaitNumber(storeName), this);
        urlToolgetNum.setOnCompleted(new URLUtil.OnCompletedListener() {
            @Override
            public void OnCompleted(String httpResult) {
                Log.d("result", httpResult);
                String[] httpStatus = httpResult.split("/");
                String[] waitNumArraySplit = httpStatus[1].split(" ");
                String[] waitNumArray = new String[waitNumArraySplit.length-2];

                //將取得的waitNum做切割，第一項為空白，第二項為00，都要拿掉
                System.arraycopy(waitNumArraySplit, 2, waitNumArray, 0, waitNumArraySplit.length-2);

                if(httpStatus[0].equals("200")) {

                    //如果回傳空值，表示目前沒有等待號碼
                    if(httpStatus.length == 1) {
                        DialogUtil.showPostiveDialog(CallActivity.this, getResources().getString(R.string.NoWaitNumber), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        return;
                    } else if(httpStatus[1].equals("error")) {
                        DialogUtil.showPostiveDialog(CallActivity.this, getResources().getString(R.string.ConnectionError), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        return;

                    //回傳成功，將等候號碼傳入adapter中
                    } else {
                        int processRound = waitNumArray.length / 5;
                        int processTime = waitNumArray.length % 5;

                        //設定依序叫號
                        setConNum(waitNumArray[0]);

                        processWaitNumber(processRound, processTime, waitNumArray);
                        WaitNumAdapter waitNumAdapter = new WaitNumAdapter(CallActivity.this, waitNumberArrayList, new WaitNumAdapter.WaitNumCallback() {
                            @Override
                            public void onCallBack(String result) {
                                Log.d("result", result);
                                urlToolCallNum = new URLUtil(URLUtil.getUrlCallNumber(storeName, result), CallActivity.this);
                                urlToolCallNum.setOnCompleted(new URLUtil.OnCompletedListener() {
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
                if(!takeEdt.getText().toString().equals("")) {
                    int takeEdtInt = Integer.valueOf(takeEdt.getText().toString());
                    String takeNum;

                    //如果號碼小於十則須在前面補零
                    if(takeEdtInt < 10) {
                        takeNum = "0" + String.valueOf(takeEdtInt);
                    } else {
                        takeNum = String.valueOf(takeEdtInt);
                    }

                    urlToolCallNum = new URLUtil(URLUtil.getUrlCallNumber(storeName, takeNum), CallActivity.this);
                    urlToolCallNum.setOnCompleted(new URLUtil.OnCompletedListener() {
                        @Override
                        public void OnCompleted(String httpResult) {
                            waitNumberArrayList.clear();
                            reFresh();
                        }
                    });

                    urlToolCallNum.execute();
                    takeEdt.setText("");
                }

                break;

            case R.id.button6: //依序叫號
                int conNumInt = Integer.valueOf(conNum_tv.getText().toString());
                String conNum;

                if(conNumInt < 10) {
                    conNum = "0" + String.valueOf(conNumInt);
                } else {
                    conNum = String.valueOf(conNumInt);
                }

                urlToolCallNum = new URLUtil(URLUtil.getUrlCallNumber(storeName, conNum), CallActivity.this);
                urlToolCallNum.setOnCompleted(new URLUtil.OnCompletedListener() {
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
