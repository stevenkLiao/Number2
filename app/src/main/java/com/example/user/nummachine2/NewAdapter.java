package com.example.user.nummachine2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by StevenLiao on 2017/12/29.
 */

public class NewAdapter extends RecyclerView.Adapter<NewAdapter.ViewHolder> {
    List<NewsInfo> NewsInfoList;
    private static Context context;
    private static LayoutInflater inf = null;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView newtext;
        ImageView newimage;
        public ViewHolder(View itemView) {
            super(itemView);
            newtext = (TextView) itemView.findViewById(R.id.textView18);
            newimage = (ImageView) itemView.findViewById(R.id.imageView12);
        }
    }
    public NewAdapter(Context context, List<NewsInfo> NewsInfo) {
        NewsInfoList = NewsInfo;
        NewAdapter.context = context;
        if(context != null) {
            inf = LayoutInflater.from(context);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.newlayout, viewGroup, false);
        // ViewHolder參數一定要是Layout的根節點。
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        NewsInfo newsInfo = NewsInfoList.get(i);
        viewHolder.newtext.setText(newsInfo.getnews());
    }
    @Override
    public int getItemCount() {
        return NewsInfoList.size();
    }
}