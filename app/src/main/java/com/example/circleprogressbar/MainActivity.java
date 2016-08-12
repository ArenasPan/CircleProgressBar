package com.example.circleprogressbar;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private int progress = 0;
    private CircleProgressBar progressBar;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    progressBar.setProgress(progress);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (CircleProgressBar) findViewById(R.id.progress);
//        progressBar.setProgress(50);
        MyThread thread = new MyThread();
        thread.start();
    }

    class MyThread extends Thread{
        @Override
        public void run() {
            while (true){
                if (progress < 100) {
                    progress += 10;
                } else {
                    progress = 0;
                }
                mHandler.sendEmptyMessage(1);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
