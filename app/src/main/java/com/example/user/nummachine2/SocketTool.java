package com.example.user.nummachine2;

import android.os.Handler;
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

    private Handler mainHandler;

    private static Socket socket;

    private static ExecutorService threadPool;

    private static InputStream is;

    private static InputStreamReader isr;

    BufferedReader br;

    String response;

    OutputStream outputStream;

    public void SocketTool() {
        threadPool = Executors.newCachedThreadPool();
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

    public static void receiverSocket() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
//                try {
//
//                }
            }
        });

    }
}
