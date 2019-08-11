package com.example.user.Activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.user.CommonData.CommonData;
import com.example.user.Utils.DialogUtil;
import com.example.user.Utils.SocketUtil;
import com.example.user.Utils.URLUtil;
import com.example.user.nummachine2.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;

import static com.example.user.Utils.URLUtil.getTimeStamp;

public class PadActivity extends AppCompatActivity {

    private ImageView ivCode;
    private String storeName;
    private URLUtil urlUtilLastWaitNum, urlUtilTimeStamp;
    private Handler mHandler;
    private Runnable getLastWaitNumQueryRunnable;
    private boolean isFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pad);

        ivCode = (ImageView) findViewById(R.id.imageView3);
        storeName = getIntent().getStringExtra("storeName");

        setLastWaitNumQueryRunnable();
        getTimeStamp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isFirstTime) {
            mHandler.post(getLastWaitNumQueryRunnable);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(getLastWaitNumQueryRunnable);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //取得等待號碼，加一後並顯示QR code
    private void setLastWaitNumToQRcode(String returnMsg) {

        //回傳200/00 01 02....要先做切割
        String[] strSplit = returnMsg.split("/");

        String waitNumPlus, qrCodeURL;
        String oriWaitNum = strSplit[1].replaceAll("\\n", "").replaceAll("\\r", "");
        int waitNumPlusInt = Integer.valueOf(oriWaitNum) + 1;
        //組合等待號碼String
        if(waitNumPlusInt < 10) {
            waitNumPlus = "0" + String.valueOf(waitNumPlusInt);
        } else {
            waitNumPlus = String.valueOf(waitNumPlusInt);
        }

        qrCodeURL = "http://220.135.192.24/numbermachine2.html?storeTableNameStr=" + CommonData.TABLE_NAME + "&storename=" +
                storeName + "&yourNum=" + waitNumPlus + "&timeStamp=" + CommonData.TIME_STAMP;

        //製作Barcode並顯示
        BarcodeEncoder encoder = new BarcodeEncoder();
        try {
            Bitmap bit = encoder.encodeBitmap(qrCodeURL, BarcodeFormat.QR_CODE, 250, 250);
            ivCode.setImageBitmap(bit);

        } catch (WriterException e) {
            e.printStackTrace();

        }
    }

    //定期發送等待號碼Query
    private void getLastWaitNumQuery() {
        if(isFirstTime) {
            mHandler = new Handler();
            isFirstTime = false;
        }
        mHandler.post(getLastWaitNumQueryRunnable);

    }

    //設定等待號碼Runnable
    private void setLastWaitNumQueryRunnable() {
        getLastWaitNumQueryRunnable = new Runnable() {
            @Override
            public void run() {

                urlUtilLastWaitNum = new URLUtil(URLUtil.getUrlLastWaitNumber(), PadActivity.this);
                urlUtilLastWaitNum.setOnCompleted(new URLUtil.OnCompletedListener() {
                    @Override
                    public void OnCompleted(String httpResult) {
                        //取得目前等待號碼後，加一後顯示QRcode以提供掃描，藉此，將加一後的等待號碼傳到後台的table
                        setLastWaitNumToQRcode(httpResult);
                        Log.d("liao", httpResult);
                        mHandler.postDelayed(getLastWaitNumQueryRunnable, 2000);

                    }
                });

                urlUtilLastWaitNum.execute();

            }
        };
    }

    //取得TimeStamp
    private void getTimeStamp() {
        urlUtilTimeStamp = new URLUtil(URLUtil.getTimeStamp(storeName), PadActivity.this);
        urlUtilTimeStamp.setOnCompleted(new URLUtil.OnCompletedListener() {
            @Override
            public void OnCompleted(String httpResult) {
                CommonData.setTimeStamp(httpResult.trim().split("/")[1]);
                getLastWaitNumQuery();
            }
        });

        urlUtilTimeStamp.execute();
    }

}