package com.example.user.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;

import com.example.user.nummachine2.R;

public class DialogUtil {

    public static void showNormalDialog(Context context, String title, String msg, DialogInterface.OnClickListener listener, DialogInterface.OnClickListener nglistener) {
        AlertDialog.Builder builder;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);

        } else {
            builder = new AlertDialog.Builder(context);
        }

        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("確定", listener);
        builder.setNegativeButton("取消", nglistener);

        builder.create().show();

    }

    public static void showPostiveDialog(Context context, String msg, DialogInterface.OnClickListener posListener) {
        AlertDialog.Builder builder;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);

        } else {
            builder = new AlertDialog.Builder(context);
        }

        builder.setTitle(context.getResources().getString(R.string.DialogTitle));
        builder.setMessage(msg);
        builder.setPositiveButton("確定", posListener);

        builder.create().show();
    }
}
