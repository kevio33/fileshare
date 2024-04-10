package com.kevin.viewpagertest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.kevin.viewpagertest.databinding.ActivityMainBinding;
import com.kevin.viewpagertest.fragment.Fragment1;
import com.kevin.viewpagertest.fragment.Fragment2;
import com.kevin.viewpagertest.fragment.Fragment3;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//
    private static final String TAG = "MainActivity";
    private ActivityMainBinding activityMainBinding;

    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());

//        EdgeToEdge.enable(this);//自定义的工具类，用于帮助实现全屏展示效果
        setContentView(activityMainBinding.getRoot());
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        fragmentList.add(new Fragment1());
        fragmentList.add(new Fragment2());
        fragmentList.add(new Fragment3());

        activityMainBinding.viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), getLifecycle(), fragmentList));
//        activityMainBinding.viewPager.setAdapter(new MyPagerStateFragment(getSupportFragmentManager(),fragmentList));
        //绑定viewpaer和tab
        new TabLayoutMediator(activityMainBinding.tabLayout, activityMainBinding.viewPager, (tab, position) -> {
            tab.setText("frag"+(position+1));

        }).attach();

        getSupportFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {
            Log.i(TAG, "onCreate: "+bundle.getString("data"));
        });

//        activityMainBinding.tabLayout.setupWithViewPager(activityMainBinding.viewPager);

//        handler.sendEmptyMessageDelayed(0, 2000);//发送消息轮播图片
    }

    //处理轮播消息，进行轮播
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
////            super.handleMessage(msg);
//            if(activityMainBinding.viewPager.getCurrentItem() == fragmentList.size()-1){
//                activityMainBinding.viewPager.setCurrentItem((activityMainBinding.viewPager.getCurrentItem()+1) % fragmentList.size(),false);
//            }else{
//                activityMainBinding.viewPager.setCurrentItem((activityMainBinding.viewPager.getCurrentItem()+1) % fragmentList.size());
//            }
//
//            handler.sendEmptyMessageDelayed(0, 2000);
//        }
//    };
}