package com.example.user.nummachine2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ChooseActivity extends AppCompatActivity implements View.OnClickListener {

    TextView store_tv, consu_tv;
    TextView Tittle;
    private static final int REQUEST_STORAGE = 0;
    private static final int REQUEST_CAMERA = 1;

    //Touch物件，控制按鈕變色
    TextView.OnTouchListener TVtouch = new TextView.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                //store_tv.setTextColor(Color.parseColor("#ffffff"));
                store_tv.setBackgroundResource(R.drawable.titledown);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                //store_tv.setTextColor(Color.parseColor("#ffffff"));
                store_tv.setBackgroundResource(R.drawable.title);
            }
            return false;
        }
    };

    //Touch物件，控制按鈕變色
    TextView.OnTouchListener TVtouch2 = new TextView.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                //consu_tv.setTextColor(Color.parseColor("#dddddd"));
                consu_tv.setBackgroundResource(R.drawable.titledown);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                //consu_tv.setTextColor(Color.parseColor("#000000"));
                consu_tv.setBackgroundResource(R.drawable.title);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        store_tv = (TextView) findViewById(R.id.textView8);
        consu_tv = (TextView) findViewById(R.id.textView7);
        Tittle = (TextView) findViewById(R.id.textView4);
        store_tv.setOnClickListener(this);
        consu_tv.setOnClickListener(this);

        /*權限請求*/
        /*
        int permission_st = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permission_st != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_STORAGE
            );
        }
        */

        int permission_cam = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        if(permission_cam != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {android.Manifest.permission.CAMERA},
                    REQUEST_CAMERA
            );
        }

        //按鈕變色

        store_tv.setOnTouchListener(TVtouch);
        consu_tv.setOnTouchListener(TVtouch2);

        Tittle.setTypeface(Typeface.createFromAsset(getAssets()
                , "fonts/cwTeXYen-zhonly.ttf"));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.textView8:
                Intent intent = new Intent(this, LogActivity.class);
                startActivity(intent);
                break;

            case R.id.textView7:
                intent = new Intent(this, qrCodeActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                break;
            case REQUEST_CAMERA:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                break;

        }
    }
}
