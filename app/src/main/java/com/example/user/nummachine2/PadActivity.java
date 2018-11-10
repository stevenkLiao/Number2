package com.example.user.nummachine2;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pad);

        ivCode = (ImageView) findViewById(R.id.imageView3);

        SocketTool.initSocketTool();
        SocketTool.createSocket();
        SocketTool.sendSocket("test", socketHandler);

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
