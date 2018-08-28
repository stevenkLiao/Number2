package com.example.user.nummachine2;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter<NewAdapter.ViewHolder> mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context;
    private ArrayList<NewsInfo> NewsInfos;
    private TextView addBtn, delBtn;
    private ImageView DiaView;

    //Touch物件，控制按鈕變色
    TextView.OnTouchListener TVtouch = new TextView.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                addBtn.setTextColor(Color.parseColor("#dddddd"));
                addBtn.setBackgroundResource(R.drawable.titledown);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                addBtn.setTextColor(Color.parseColor("#000000"));
                addBtn.setBackgroundResource(R.drawable.title);
            }
            return false;
        }
    };

    //Touch物件，控制按鈕變色
    TextView.OnTouchListener TVtouch2 = new TextView.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.e("touch", "down");
                delBtn.setTextColor(Color.parseColor("#dddddd"));
                delBtn.setBackgroundResource(R.drawable.titledown);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.e("touch", "up");
                delBtn.setTextColor(Color.parseColor("#000000"));
                delBtn.setBackgroundResource(R.drawable.title);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        addBtn = (TextView) findViewById(R.id.textView19);
        delBtn = (TextView) findViewById(R.id.textView20);
        //DiaView = (ImageView) findViewById(R.id.imageView13);
        addBtn.setOnClickListener(this);
        //DiaView.setOnClickListener(this);
        //delBtn = (TextView) findViewById(R.id.textView20);

        //按鈕變色
        addBtn.setOnTouchListener(TVtouch);
        delBtn.setOnTouchListener(TVtouch2);

        context = this;
        NewsInfos = new ArrayList<NewsInfo>();
        NewsInfos.add(new NewsInfo("買醫松醫", "aaa"));
        NewsInfos.add(new NewsInfo("buy one get one", "bbb"));

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NewAdapter(context, NewsInfos);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.textView19){
            Log.e("dialog","show");
            LayoutInflater dialogLi = LayoutInflater.from(context);
            View NewsView = dialogLi.inflate(R.layout.addnews, null);
            DiaView = (ImageView) NewsView.findViewById(R.id.imageView13);
            AlertDialog.Builder NewsDialogBuilder = new AlertDialog.Builder(context);
            NewsDialogBuilder.setView(NewsView);
            DiaView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(NewsActivity.this,"DD",Toast.LENGTH_SHORT).show();
                }
            });
            View titleView = dialogLi.inflate(R.layout.titleview, null);
            NewsDialogBuilder
                    .setTitle("新增最新消息")
                    .setCustomTitle(titleView);

            final Dialog testdialog = NewsDialogBuilder.create();
            testdialog.show();
            //AlertDialog test =  NewsDialogBuilder.create().getWindow().setBackgroundDrawableResource(R.color.colorOrange);
        } else if (v.getId() == R.id.imageView13) {
            Toast.makeText(this,"DD",Toast.LENGTH_SHORT).show();
        } else {
            Log.e("aa","ss");
        }
    }
}
