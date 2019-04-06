package com.example.user.framework;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.user.Utils.DialogUtil;

/**
 * Created by USER on 2019/4/6.
 */

public class ParentActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;

    public void showLoading() {
        progressDialog = DialogUtil.createProcessDialog(this, "提示訊息", "請稍後...");
        progressDialog.show();
    }

    public void cancelLoading() {
        progressDialog.dismiss();
    }
}
