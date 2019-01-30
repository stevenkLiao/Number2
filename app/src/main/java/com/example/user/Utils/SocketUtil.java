package com.example.user.Utils;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import com.example.user.Utils.DialogUtil;
import com.example.user.nummachine2.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketUtil {

    private static Context mContext;

    private static Handler mainHandler;

    private static Socket socket;

    private static ExecutorService threadPool;

    private static InputStream is;

    private static InputStreamReader isr;

    private static BufferedReader br;

    private static String response;

    private static OutputStream outputStream;

    public void initContext(Context mContext) {
        this.mContext = mContext;
    }

    public static void initSocketTool() {
        threadPool = Executors.newCachedThreadPool();
    }

    public static ExecutorService getInstance() {
        return threadPool;
    }


    public static void
    createAndSendSocket(final String sendMsg, final Handler handler, final Context mContext) {

        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("220.135.192.24", 12345);

                    //new 出一個新的Socket後，每兩秒發出號碼查詢
                    sendSocket(sendMsg, handler, mContext);

                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        });

    }


    public static void sendSocket(final String sendMsg, Handler handler, Context mContext) {
        mainHandler = handler;

        //判斷
        if(socket == null) {
            mainHandler.obtainMessage(1, mContext.getResources().getString(R.string.SocketNotExist)).sendToTarget();

            return;
        }
        threadPool.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    while (true) {

                        outputStream = socket.getOutputStream();
                        outputStream.write(sendMsg.getBytes());
                        outputStream.flush();

                        is = socket.getInputStream();
                        isr = new InputStreamReader(is);
                        br = new BufferedReader(isr);

                        response = br.readLine();
                        mainHandler.obtainMessage(0, response).sendToTarget();
                        Thread.sleep(2000);
                        Log.d("liao", response);
                    }

                } catch (IOException e) {
                    e.printStackTrace();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public static void closeSocket() {
        try {
            outputStream.close();
            br.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
