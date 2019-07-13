package com.example.user.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.example.user.CommonData.CommonData;
import com.example.user.framework.ParentActivity;
import com.example.user.nummachine2.R;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 電文相關使用class
 */

public class URLUtil extends AsyncTask<Void, Void, String> {

    /** 電文逾時秒數 */
    private static int connectTimeout = 300;

    private OnCompletedListener mListener = null;
    private Context mContext;

    /** 網址 */
    private String mUrl = "";

    /** CODE */
    public static final String NO_INTERNET = "NO_INTERNET";

    /**
     * 建構子
     * @param Url 部份網址
     */
    public URLUtil(String Url, Context ctx) {
        mUrl = Url;
        mContext = ctx;
    }

    private ProgressDialog progressDialog;

    public interface OnCompletedListener {
        void OnCompleted(String httpResult);
    }

    public void setOnCompleted(OnCompletedListener listener) {
        mListener = listener;
    }

    @Override
    protected String doInBackground(Void... params) {

        int statusCode = 0; //判斷是否有網路 0：無網路 ｜ 1：有網路
        String rep = null;
        String strStatusCode = null;

        HttpURLConnection conn = null;
        try {

            String path = mUrl;
            conn = (HttpURLConnection) new URL(path).openConnection();

            conn.setUseCaches(false);
            conn.setReadTimeout(connectTimeout * 1000);
            conn.setConnectTimeout(connectTimeout * 1000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "text/plain; charset=utf-8");

            if (SystemUtility.checkNetworkEnable(mContext)) {
                String respMessage = conn.getResponseMessage();
                statusCode = conn.getResponseCode();
                InputStream is = conn.getInputStream();
                rep = getStringFromInputStream(is);

                return String.valueOf(statusCode) + "/" + rep;

            } else {
                return NO_INTERNET;

            }

        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String httpResult) {

        if(httpResult.equals(NO_INTERNET)) {
            DialogUtil.showPostiveDialog(mContext, mContext.getResources().getString(R.string.NoInterNetAndCheck), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {

            mListener.OnCompleted(httpResult);
        }
    }

    static public String getStringFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        // 一定要写len=is.read(buffer)
        // 如果while((is.read(buffer))!=-1)则无法将数据写入buffer中
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        is.close();
        String state = os.toString();// 把流中的数据转换成字符串,采用的编码是utf-8(模拟器默认编码)
        os.close();
        return state;
    }

    static public String getUrlForLogin(String strEmail, String strPW) {
        return CommonData.SERVER_IP + "/" + CommonData.LOG_IN_API +
                "?" + "email=" + strEmail + "&" + "password=" + strPW;
    }

    static public String getUrlForRegister(String regMail, String regPws, String regStoreName) {
        return CommonData.SERVER_IP + "/" + CommonData.REGISTER_API +
                "?" + "email=" + regMail + "&" + "password=" + regPws +
                "&" + "storename=" + regStoreName;
    }

    static public String getUrlForQueryName(String queryName) {
        return CommonData.SERVER_IP + "/" + CommonData.QUERY_STORE_NAME_API +
               "?" + "email=" + queryName;
    }

    static public String getUrlWaitNumber(String storeName) {
        return CommonData.SERVER_IP + "/" + CommonData.QUERY_WAIT_NUM_API +
                "?" + "storeName=" + storeName + "&" + "tableName=" + CommonData.TABLE_NAME;
    }

    static public String getUrlLastWaitNumber(String storeTableName) {
        return CommonData.SERVER_IP + "/" + CommonData.QUERY_LAST_WAIT_NUM_API +
                "?" + "tableName=" + storeTableName;
    }

    static public String getUrlCallNumber(String storeName, String callnum) {
        return CommonData.SERVER_IP + "/" + CommonData.CALL_NUM_API +
                "?" + "storeName=" + storeName + "&" + "callNum=" + callnum +
                "&" + "tableName=" + CommonData.TABLE_NAME;
    }

    static public String getHttpResult(String httpResultOri) {
        String[] split_line = httpResultOri.split("/");
        return split_line[1];
    }

    static public String getInitTable(String storetableName, String storeName) {
        return CommonData.SERVER_IP + "/" + CommonData.INIT_API +
                "?" + "tableName=" + storetableName + "&" + "storeName=" + storeName;
    }

}
