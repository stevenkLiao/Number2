package com.example.user.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.nummachine2.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by USER on 2018/9/2.
 */

public class WaitNumAdapter extends RecyclerView.Adapter<WaitNumAdapter.ViewHolder>{
    private Context mContext;
    private LayoutInflater mInflater;
    private List<WaitNumberArray> WaitNumberArrayList;
    private String[] waitNumber;

    public WaitNumAdapter(Context context, List<WaitNumberArray> WaitNumberArrayList) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.WaitNumberArrayList = WaitNumberArrayList;
    }

    @Override
    public WaitNumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.wait_num_item, parent, false);
        WaitNumAdapter.ViewHolder viewHolder = new WaitNumAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WaitNumAdapter.ViewHolder holder, int position) {
        waitNumber = WaitNumberArrayList.get(position).getWaitNumber();

        //設定等待號碼欄位
        setWaitNumber(holder.wait_num_tv1, waitNumber[1]);
        setWaitNumber(holder.wait_num_tv2, waitNumber[2]);
        setWaitNumber(holder.wait_num_tv3, waitNumber[3]);
        setWaitNumber(holder.wait_num_tv4, waitNumber[4]);
        setWaitNumber(holder.wait_num_tv5, waitNumber[5]);

    }

    @Override
    public int getItemCount() {
        return WaitNumberArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView wait_num_tv1, wait_num_tv2, wait_num_tv3, wait_num_tv4, wait_num_tv5;

        public ViewHolder(View itemView) {
            super(itemView);
            wait_num_tv1 = (TextView) itemView.findViewById(R.id.textView21);
            wait_num_tv2 = (TextView) itemView.findViewById(R.id.textView22);
            wait_num_tv3 = (TextView) itemView.findViewById(R.id.textView23);
            wait_num_tv4 = (TextView) itemView.findViewById(R.id.textView24);
            wait_num_tv5 = (TextView) itemView.findViewById(R.id.textView25);

            wait_num_tv1.setOnClickListener(this);
            wait_num_tv2.setOnClickListener(this);
            wait_num_tv3.setOnClickListener(this);
            wait_num_tv4.setOnClickListener(this);
            wait_num_tv5.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.textView21:

                    break;

                case R.id.textView22:

                    break;

                case R.id.textView23:

                    break;

                case R.id.textView24:

                    break;

                case R.id.textView25:

                    break;
            }
        }
    }

    public static class WaitNumberArray {
        private String[] waitNumber;

        public String[] getWaitNumber() {
            return waitNumber;
        }

        public void setWaitNumber(String[] waitNumber) {
            this.waitNumber = waitNumber;
        }
    }

    private void setWaitNumber(TextView waitNumber_tv, String waitNumber_str) {
        if(waitNumber_str!=null){
            waitNumber_tv.setVisibility(View.VISIBLE);
            waitNumber_tv.setText(waitNumber_str);
        }
    }

}
