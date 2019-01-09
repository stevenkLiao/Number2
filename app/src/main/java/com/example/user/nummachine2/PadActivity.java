package com.example.user.nummachine2;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class PadActivity extends AppCompatActivity {

    private ImageView ivCode;
    private String storeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pad);

        ivCode = (ImageView) findViewById(R.id.imageView3);
        storeName = getIntent().getStringExtra("storeName");

        SocketTool.initSocketTool();

        //建立一個新的Socket並以每兩秒發送號碼查詢
        SocketTool.createSocket();

        SocketTool.sendSocket(storeName, socketHandler, PadActivity.this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        SocketTool.closeSocket();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SocketTool.closeSocket();
    }

    private final Handler socketHandler = new Handler() {
        public void handleMessage(Message msg) {
            String qrMsg = getNumFromQrCode(msg.obj.toString());

            switch (msg.what) {
                case 0:
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    try {

                        Bitmap bit = encoder.encodeBitmap(qrMsg, BarcodeFormat.QR_CODE, 250, 250);
                        ivCode.setImageBitmap(bit);

                    } catch (WriterException e) {
                        e.printStackTrace();

                    }
                    break;
            }
        }
    };

    //整理qrCode所得到的資訊
    private String getNumFromQrCode(String reTurnMsg) {
        String[] strArray = reTurnMsg.split(":");

        if(strArray[1].equals("no")) {
            return "noSocket";

        } else {
            return strArray[1];
        }
    }

    public interface createSocketCallback{
        public void onSocketCallback();
    }
}
