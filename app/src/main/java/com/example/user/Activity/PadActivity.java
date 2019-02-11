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
import com.example.user.nummachine2.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class PadActivity extends AppCompatActivity {

    private ImageView ivCode;
    private String storeName;
    private boolean isFirstGetIn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pad);

        ivCode = (ImageView) findViewById(R.id.imageView3);
        storeName = getIntent().getStringExtra("storeName");

        SocketUtil.initSocketTool();

        //建立一個新的Socket並以每兩秒發送號碼查詢
        SocketUtil.createAndSendSocket(storeName, socketHandler, PadActivity.this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //判斷是否為第一次的Socket鍵結
        if(isFirstGetIn) {
            isFirstGetIn = false;

        } else {
            SocketUtil.initSocketTool();

            //建立一個新的Socket並以每兩秒發送號碼查詢
            SocketUtil.createAndSendSocket(storeName, socketHandler, PadActivity.this);

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        SocketUtil.closeSocket();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SocketUtil.closeSocket();
    }

    private final Handler socketHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String socketMsg = getNumFromQrCode(msg.obj.toString());

                    if(socketMsg.equals("no")) {
                        DialogUtil.showPostiveDialog(PadActivity.this, getResources().getString(R.string.NoWaitNumber), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    } else {

                        BarcodeEncoder encoder = new BarcodeEncoder();
                        try {


                            Bitmap bit = encoder.encodeBitmap(msg.obj.toString(), BarcodeFormat.QR_CODE, 250, 250);
                            ivCode.setImageBitmap(bit);

                        } catch (WriterException e) {
                            e.printStackTrace();

                        }
                    }
                    break;
            }
        }
    };

    //整理qrCode所得到的資訊
    private String getNumFromQrCode(String reTurnMsg) {
        String[] strArray = reTurnMsg.split(":");
        
        return strArray[1];

    }

}
