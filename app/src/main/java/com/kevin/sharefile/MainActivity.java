package com.kevin.sharefile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.kevin.sharefile.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private ActivityMainBinding activityMainBinding;

    private String text;




    private final String TAG = "kevinYang";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        onClick();
//        String package_name = getPackageName();
//        String path_internal_storage = getFilesDir().toString();
//        String path_cache = getCacheDir().toString();
//        String path_external = String.valueOf(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
//        String path_external_storage = Environment.getExternalStorageDirectory().toString();
//        String path_external_storage_public = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
//        Log.w(TAG+"package name",package_name);
//        Log.w(TAG+"path_internal_storage",path_internal_storage);
//        Log.w(TAG+"path_cache",path_cache);
//        Log.w(TAG+"path_external",path_external);
//        Log.w(TAG+"path_external_storage",path_external_storage);
//        Log.w(TAG+"path_external_storage_public",path_external_storage_public);
//
//        Log.w(TAG+"state",Environment.getExternalStorageState());

    }

    private void onClick() {

        activityMainBinding.shareText.setOnClickListener(this);
        activityMainBinding.sharePicAndText.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int view_id = view.getId();

        if (view_id == R.id.shareText){
            text = (String) activityMainBinding.textView.getText();
            Intent intent = new Intent(Intent.ACTION_SEND);//设置action
            intent.setType("text/plain");//设置传输类型
            intent.putExtra(Intent.EXTRA_TEXT,text);

            startActivity(Intent.createChooser(intent,"分享文本"));
        }else if(view_id == R.id.sharePicAndText){
            Bitmap bitmap = getBitMap(R.mipmap.forests);
            String path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)+"/forest.jpg";
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(path);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);//将bitmap流写入到外部私有存储区
                if (outputStream!=null){
                    outputStream.flush();//更新
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }finally {
                try {
                    outputStream.close();//管理写出流
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            sharePic(path);
        }
    }

    //读取图片的uri，并且分享
    public void sharePic(String path){
        File file = new File(path);
        //获取文件的uri
        Uri imageUri = FileProvider.getUriForFile(this,"com.kevin.sharefile.fileprovider",file);
        //intent传递
        Intent intent = new Intent(Intent.ACTION_SEND);

        //mime类型为image，表示分享图片
        intent.setType("image/*");
        //使用extra_stream传递图片的uri
        intent.putExtra(Intent.EXTRA_STREAM,imageUri);

        //同时传递描述文字
        text = (String) activityMainBinding.textView.getText();
        intent.putExtra(Intent.EXTRA_TEXT,text);

        startActivity(Intent.createChooser(intent,"分享图片和文字"));

    }

    //获取图片的bitmap
    public Bitmap getBitMap(int id){

        Resources resources = getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(resources,id);
        return bitmap;
    }
}