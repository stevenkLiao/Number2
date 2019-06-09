package com.example.user.Activity;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.user.Utils.DialogUtil;
import com.example.user.Utils.URLUtil;
import com.example.user.Adapter.WaitNumAdapter;
import com.example.user.nummachine2.R;

import java.util.ArrayList;
import java.util.List;

public class CallActivity extends AppCompatActivity {

    private RecyclerView waitNumRcv;
    private String storeName;
    private List<WaitNumAdapter.WaitNumberArray> waitNumberArrayList;
    private WaitNumAdapter waitNumAdapter;
    private URLUtil urlToolCallNum, urlToolgetNum;
    private Handler handler;
    private Runnable refreshRunnable;
    private String tmpHttpResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        waitNumRcv = (RecyclerView) findViewById(R.id.waitNumRcv);

        //宣告等待號碼用的List
        waitNumberArrayList = new ArrayList<>();
        storeName = getIntent().getStringExtra("storeName");

        //初始化Rcv
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        waitNumRcv.setLayoutManager(layoutManager);
        initWaitAdapter();
        waitNumRcv.setAdapter(waitNumAdapter);

        initRefreshRunnable();
        reFresh();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(refreshRunnable);
        finish();
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

    private void initRefreshRunnable() {
        handler = new Handler();
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                reFresh();
            }
        };
    }

    private void reFresh() {
        urlToolgetNum = new URLUtil(URLUtil.getUrlWaitNumber(storeName), this);
        urlToolgetNum.setOnCompleted(new URLUtil.OnCompletedListener() {
            @Override
            public void OnCompleted(String httpResult) {
                //得到的http要在跟之前的結果比較，不一樣才會更新
                if(!tmpHttpResult.equals(httpResult)) {
                    tmpHttpResult = httpResult;
                    waitNumberArrayList.clear();
                } else {
                    handler.postDelayed(refreshRunnable, 1000);
                    return;
                }

                String[] httpStatus = httpResult.split("/");
                //等待號碼以空白做分隔
                String[] waitNumArraySplit = httpStatus[1].split(" ");
                String[] waitNumArray = new String[waitNumArraySplit.length-2];

                //將取得的waitNum做切割，第一項為空白，第二項為00，都要拿掉
                System.arraycopy(waitNumArraySplit, 2, waitNumArray, 0, waitNumArraySplit.length-2);

                if(httpStatus[0].equals("200")) {

                    if(httpStatus[1].equals("error")) {
                        DialogUtil.showPostiveDialog(CallActivity.this, getResources().getString(R.string.ConnectionError), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        return;

                    //回傳成功，將等候號碼傳入adapter中
                    } else {
                        //判斷是否有等待號碼，如果有會進行等待數字處理
                        if(waitNumArray.length != 0) {
                            int processRound = waitNumArray.length / 5;
                            int processTime = waitNumArray.length % 5;
                            processWaitNumber(processRound, processTime, waitNumArray);
                        }
                        waitNumAdapter.notifyDataSetChanged();

                    }
                }
                //當一次號碼要求結束後，隔2秒會再發一次做刷新，開始循環
                handler.postDelayed(refreshRunnable, 1000);
            }
        });

        urlToolgetNum.execute();
    }

    private void initWaitAdapter() {
        waitNumAdapter = new WaitNumAdapter(CallActivity.this, waitNumberArrayList, new WaitNumAdapter.WaitNumCallback() {
            @Override
            public void onCallBack(String result) {
                urlToolCallNum = new URLUtil(URLUtil.getUrlCallNumber(storeName, result), CallActivity.this);
                urlToolCallNum.setOnCompleted(new URLUtil.OnCompletedListener() {
                    @Override
                    public void OnCompleted(String httpResult) {
                        reFresh();
                    }
                });

                urlToolCallNum.execute();

            }
        });
    }
}
