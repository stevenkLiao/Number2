package com.example.user.nummachine2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.Utils.DialogUtil;

import org.w3c.dom.Text;

/**
 * Created by Mitake on 2018/3/7.
 */

public class MainFlameActivity extends AppCompatActivity implements View.OnClickListener

{
    //宣告view元件
    private LinearLayout ll_board, ll_call;
    private TextView tvStoreName;
    private String strEmail, storeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_flame);
        tvStoreName = (TextView) findViewById(R.id.textView14);


        Intent intent = getIntent();
        strEmail = intent.getStringExtra("email");

        URLtool urlTool;
        urlTool = new URLtool(URLtool.getUrlForQueryName(strEmail), this);

        urlTool.setOnCompleted(new URLtool.OnCompletedListener() {
            @Override
            public void OnCompleted(String httpResult) {
                Log.d("result", httpResult);
                String[] result = httpResult.split("/");
                if(result[0].equals("200")) {
                    tvStoreName.setText(result[1]);
                    storeName = result[1];
                } else {
                    DialogUtil.showPostiveDialog(MainFlameActivity.this, getResources().getString(R.string.WrongPW), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }

            }
        });

        urlTool.execute();

        initView();

        //設定touch觸發事件
        ll_call.setOnTouchListener(view_touch);
        ll_board.setOnTouchListener(view_touch);

        //設定click觸發事件
        ll_call.setOnClickListener(this);
        ll_board.setOnClickListener(this);
    }

    //按鍵觸發
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {

            case R.id.ll_board:
                startActivity(new Intent(MainFlameActivity.this, PadActivity.class));
                break;

            case R.id.ll_call:
                Intent intent = new Intent(this, CallActivity.class);
                intent.putExtra("storeName", storeName);
                startActivity(intent);
                break;
        }
    }

    //實現view元件
    private void initView() {
        ll_board = (LinearLayout) findViewById(R.id.ll_board);
        ll_call = (LinearLayout) findViewById(R.id.ll_call);
        tvStoreName = (TextView) findViewById(R.id.textView14);

    }

    //Touch物件，控制按鈕變色
    View.OnTouchListener view_touch = new TextView.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setBackgroundResource(R.drawable.waitnumdown);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.setBackgroundResource(R.drawable.waitnum);
            }
            return false;
        }
    };
}
