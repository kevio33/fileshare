package com.kevin.bluetoothtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kevin.bluetoothtest.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "kevin20240402";

    private Context context = this;
    ActivityMainBinding activityMainBinding;
    BluetoothAdapter bTAdatper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        activityMainBinding.openBluetooth.setOnClickListener(this);
        activityMainBinding.searchDeviceBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        //开启蓝牙
        if (view.getId() == R.id.open_bluetooth) {
            isBluetoothEnable(0);
        } else if (view.getId() == R.id.search_device_btn) {

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            registerReceiver(broadcastReceiver, intentFilter);
            isBluetoothEnable(1);

        }
    }


    //开启蓝牙
    public void isBluetoothEnable(int requestCode){
        bTAdatper = BluetoothAdapter.getDefaultAdapter();
        if (bTAdatper == null) {
            Toast.makeText(this, "当前设备不支持蓝牙功能", Toast.LENGTH_SHORT).show();
        }
        if (!bTAdatper.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                //动态开启权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1);
                return;
            }
        }
        //开启被其它蓝牙设备发现的功能
//        if (bTAdatper.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
//            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//            //设置为一直开启
//            i.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);//设置为0，可以让蓝牙设备一直处于可发现状态。当我们需要设置具体可被发现的时间时，最多只能设置300秒。
//            startActivity(i);
//        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            //动态开启权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 2);
            return;
        }
        if(requestCode == 1){
            //搜索周围设备
            bTAdatper.startDiscovery();
        }
    }

    //注册一个广播,接收搜索到的设备信息
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //避免重复添加已经绑定过的设备
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1);
                    return;
                }
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    //此处的adapter是列表的adapter，不是BluetoothAdapter
//                    adapter.add(device);
//                    adapter.notifyDataSetChanged();
                    Log.i(TAG,device.toString());

                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Toast.makeText(MainActivity.this,"开始搜索",Toast.LENGTH_SHORT).show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Toast.makeText(MainActivity.this,"搜索完毕",Toast.LENGTH_SHORT).show();
            }
        }
    };


    //申请运行时权限返回结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG,"bluetooth permission pass");
            } else {
                Log.e(TAG, "onRequestPermissionsResult: 申请权限已拒绝");
            }
        }else if(requestCode ==2){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG,"bluetooth scan permission pass");
            } else {
                Log.e(TAG, "onRequestPermissionsResult: 申请蓝牙扫描权限已拒绝");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}