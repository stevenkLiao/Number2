package com.example.user.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.CommonData.CommonData;
import com.example.user.Utils.DialogUtil;
import com.example.user.Utils.URLUtil;
import com.example.user.framework.ParentActivity;
import com.example.user.nummachine2.R;

public class MainFlameActivity extends ParentActivity implements View.OnClickListener

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


        //取得信箱，並透過信箱取得店名
        Intent intent = getIntent();
        strEmail = intent.getStringExtra("email");

        final URLUtil urlTool;
        urlTool = new URLUtil(URLUtil.getUrlForQueryName(strEmail), this);

        urlTool.setOnCompleted(new URLUtil.OnCompletedListener() {
            @Override
            public void OnCompleted(String httpResult) {

                cancelLoading();
                String[] result = httpResult.split("/");

                //判斷連線成功，且店名查詢後不為空字串
                if(result[0].equals("200") && !result[0].equals("")) {

                    //設置店名
                    tvStoreName.setText(result[1]);
                    storeName = result[1];

                    //重置該店的table
                    URLUtil urlToolForInit = new URLUtil(URLUtil.getInitTable("store_info", storeName), MainFlameActivity.this);
                    urlToolForInit.setOnCompleted(new URLUtil.OnCompletedListener() {
                        @Override
                        public void OnCompleted(String httpResult) {
                            //重置完畢，將最新號碼歸零
                            CommonData.lastNum = 0;
                            cancelLoading();
                        }
                    });
                    urlToolForInit.execute();
                    showLoading();
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
        showLoading();
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

            case R.id.ll_call:
                //傳送店名給CallActivity
                Intent intent = new Intent(MainFlameActivity.this, CallActivity.class);
                intent.putExtra("storeName", storeName);
                startActivity(intent);

                break;

            case R.id.ll_board:
                //傳送店名給PadActivity
                Intent intent2 = new Intent(MainFlameActivity.this, PadActivity.class);
                intent2.putExtra("storeName", storeName);
                startActivity(intent2);

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
