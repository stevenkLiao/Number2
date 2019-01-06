package com.example.user.nummachine2;

import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.EnumMap;
import java.util.Map;

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
        SocketTool.createAndSendSocket(storeName, socketHandler, PadActivity.this);

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
            switch (msg.what) {
                case 0:
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    try {


                        Bitmap bit = encoder.encodeBitmap(msg.obj.toString(), BarcodeFormat.QR_CODE, 250, 250);
                        ivCode.setImageBitmap(bit);

                    } catch (WriterException e) {
                        e.printStackTrace();

                    }
                    break;
            }
        }
    };
}
