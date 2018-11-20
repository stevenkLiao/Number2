package com.example.user.nummachine2;

import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketTool {

    private static Handler mainHandler;

    private static Socket socket;

    private static ExecutorService threadPool;

    private static InputStream is;

    private static InputStreamReader isr;

    private static BufferedReader br;

    private static String response;

    private static OutputStream outputStream;

    public static void initSocketTool() {
        threadPool = Executors.newCachedThreadPool();
    }

    public static ExecutorService getInstance() {
        return threadPool;
    }


    public static void createSocket() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    socket = new Socket("220.135.192.24", 12345);

                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        });

    }

//    public static void receiverSocket(PadActivity.SocketCallback socketCallback) {
//        threadPool.execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//
//                        is = socket.getInputStream();
//                        isr = new InputStreamReader(is);
//                        br = new BufferedReader(isr);
//
//                        response = br.readLine();
//                        Log.d("liao", response + "aa");
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//        });
//    }

    public static void sendSocket(final String sendMsg, Handler handler) {
        mainHandler = handler;
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
