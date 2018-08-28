package com.example.user.nummachine2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.Utils.DialogUtil;
import com.google.firebase.auth.FirebaseAuth;

import static java.lang.Thread.sleep;

public class RegisterActivity extends AppCompatActivity {

    TextView uploadBtn, rigBtn;
    EditText email_et, pws_et, store_et;

    private String Email_st, Pws_st, Store_st;

    //CODE宣告
    private static final int FILE_SELECT_CODE = 0;

    //Touch物件，控制按鈕變色
    TextView.OnTouchListener TVtouch = new TextView.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                rigBtn.setBackgroundResource(R.drawable.titledown);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                rigBtn.setBackgroundResource(R.drawable.title);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        rigBtn = (TextView) findViewById(R.id.textView12);
        email_et = (EditText) findViewById(R.id.editText4);
        pws_et = (EditText) findViewById(R.id.editText3);
        store_et = (EditText) findViewById(R.id.editText5);

        //按鈕變色
        rigBtn.setOnTouchListener(TVtouch);

        //申請按鍵按下，傳送資料至server
        rigBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            Email_st = email_et.getText().toString();
            Pws_st = pws_et.getText().toString();
            Store_st = store_et.getText().toString();

            URLtool urLtool = new URLtool(URLtool.getUrlForRegister(Email_st, Pws_st, Store_st), RegisterActivity.this);

            urLtool.setOnCompleted(new URLtool.OnCompletedListener() {
                @Override
                public void OnCompleted(String httpResult) {
                    if(URLtool.getHttpResult(httpResult).equals("RegisterSucceed")) {
                        DialogUtil.showPostiveDialog(RegisterActivity.this, getResources().getString(R.string.RegisterSucceed), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(RegisterActivity.this, MainFlameActivity.class);
                                startActivity(intent);
                                RegisterActivity.this.finish();
                            }
                        });
                    } else {
                        DialogUtil.showPostiveDialog(RegisterActivity.this, getResources().getString(R.string.RegisterSucceed), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    }
                }
            });

            urLtool.execute();

            }
        });

    }

}
