package com.example.user.Activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.user.Utils.DialogUtil;
import com.example.user.Utils.SocketUtil;
import com.example.user.Utils.URLUtil;
import com.example.user.nummachine2.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;

public class PadActivity extends AppCompatActivity {

    private ImageView ivCode;
    private String storeName;
    private URLUtil urlUtilLastWaitNum;
    private Handler mHandler;
    private Runnable getLastWaitNumQueryRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pad);

        ivCode = (ImageView) findViewById(R.id.imageView3);
        storeName = getIntent().getStringExtra("storeName");

        getLastWaitNumQuery();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    //整理qrCode所得到的資訊
    private String getNumFromQrCode(String reTurnMsg) {
        String[] strArray = reTurnMsg.split(":");

        return strArray[1];

    }

    //取得等待號碼，加一後並顯示QR code
    private void setLastWaitNumToQRcode(String returnMsg) {

        if(returnMsg.equals("no")) {
            DialogUtil.showPostiveDialog(PadActivity.this, getResources().getString(R.string.NoWaitNumber), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

        } else {
            int waitNumPlus = Integer.valueOf(returnMsg) + 1;

            BarcodeEncoder encoder = new BarcodeEncoder();
            try {

                Bitmap bit = encoder.encodeBitmap(Integer.toString(waitNumPlus), BarcodeFormat.QR_CODE, 250, 250);
                ivCode.setImageBitmap(bit);

            } catch (WriterException e) {
                e.printStackTrace();

            }

        }
    }

    //定期發送等待號碼Query
    private void getLastWaitNumQuery() {

        mHandler = new Handler();
        getLastWaitNumQueryRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d("liao", "LINE_100");
                urlUtilLastWaitNum = new URLUtil(URLUtil.getUrlLastWaitNumber(storeName), PadActivity.this);
                urlUtilLastWaitNum.setOnCompleted(new URLUtil.OnCompletedListener() {
                    @Override
                    public void OnCompleted(String httpResult) {
                        //setLastWaitNumToQRcode(httpResult);
                        mHandler.postDelayed(getLastWaitNumQueryRunnable, 2000);
                        Log.d("liao", "LINE_107");
                    }
                });

                urlUtilLastWaitNum.execute();
                Log.d("liao", "LINE_111");

            }
        };

        mHandler.post(getLastWaitNumQueryRunnable);

    }





}