package com.example.user.nummachine2;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.Utils.DialogUtil;

public class LogActivity extends AppCompatActivity implements View.OnClickListener{

    TextView logBtn;
    EditText edtEmail, edtPW;
    String strEmail, strPW;

    //Touch物件，控制按鈕變色
    TextView.OnTouchListener TVtouch = new TextView.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                logBtn.setBackgroundResource(R.drawable.titledown);

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                logBtn.setBackgroundResource(R.drawable.title);

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        logBtn = (TextView) findViewById(R.id.textView9);
        edtEmail = (EditText) findViewById(R.id.editText);
        edtPW = (EditText) findViewById(R.id.editText2);

        logBtn.setOnClickListener(this);

        //按鈕變色
        logBtn.setOnTouchListener(TVtouch);

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.textView9:
                //TODO 登入後端確認
                strEmail = edtEmail.getText().toString();
                strPW = edtPW.getText().toString();

                URLtool urlTool;
                urlTool = new URLtool(URLtool.getUrlForLogin(strEmail, strPW), this);

                urlTool.setOnCompleted(new URLtool.OnCompletedListener() {
                    @Override
                    public void OnCompleted(String httpResult) {
                        if(URLtool.getHttpResult(httpResult).equals("LoginSuccess")) {
                            Intent intent2 = new Intent(LogActivity.this, MainFlameActivity.class);
                            intent2.putExtra("email", strEmail);
                            startActivity(intent2);
                            LogActivity.this.finish();
                        } else {
                            DialogUtil.showPostiveDialog(LogActivity.this, getResources().getString(R.string.WrongPW), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        }

                    }
                });

                urlTool.execute();

                break;

            case R.id.textView10:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                this.finish();

                break;
        }
    }

}
