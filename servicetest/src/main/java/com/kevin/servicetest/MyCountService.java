package com.kevin.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyCountService extends Service {


    private final static String TAG = "wzj";
    private int count;
    private boolean quit;
    private Thread thread;
    private LocalBinder binder = new LocalBinder();
    public MyCountService() {
    }

    public class LocalBinder extends Binder {
        // 声明一个方法，getService。（提供给客户端调用）
        MyCountService getService() {
            // 返回当前对象LocalService,这样我们就可在客户端调用Service的公共方法了
            return MyCountService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service is invoke Created");
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 每间隔一秒count加1 ，直到quit为true。
                while (!quit) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count++;
                }
            }
        });
        thread.start();
    }

    public int getCount(){
        return count;
    }

    @Override
    public void onDestroy() {
        this.quit = true;//结束线程
        super.onDestroy();
    }
}