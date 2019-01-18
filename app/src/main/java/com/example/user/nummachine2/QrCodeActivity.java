package com.example.user.nummachine2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Programmatically initialize the scanner view
        mScannerView = new ZXingScannerView(this);
        // Set the scanner view as the content view
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register ourselves as a handler for scan results.
        mScannerView.setResultHandler(this);
        // Start camera on resume
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera on pause
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        // Prints scan results
        Log.e("qr1", rawResult.getText());
        // Prints the scan format (qrcode, pdf417 etc.)
        Log.e("qr", rawResult.getBarcodeFormat().toString());
        //If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
        String testParam = rawResult.getText().toString();
//        setResult(RESULT_OK, intent);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {

            Intent intentHome = new Intent(this,ChooseActivity.class);
            startActivity(intentHome);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
