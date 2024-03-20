package com.kevin.servicetest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import com.kevin.servicetest.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;

    ServiceConnection serviceConnection;


    MyCountService myCountService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        activityMainBinding.bindserviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MyCountService.class);
                bindService(intent,serviceConnection, Service.BIND_AUTO_CREATE);
            }
        });

        activityMainBinding.unbindserviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myCountService != null){
                    unbindService(serviceConnection);
                    myCountService = null;
                }
            }
        });

        activityMainBinding.getCountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myCountService!=null){
                    int count = myCountService.getCount();
                    activityMainBinding.countTxt.setText(count+"");
                }
            }
        });



        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                MyCountService.LocalBinder localBinder = (MyCountService.LocalBinder) iBinder;
                myCountService = localBinder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                myCountService = null;
            }
        };
    }
}