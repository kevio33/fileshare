package com.kevin.pdfscan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.kevin.pdfscan.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mainActivityBinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)


        val options = GmsDocumentScannerOptions.Builder()
            .setGalleryImportAllowed(true) //是否支持从相册导入
            .setPageLimit(2)//设置篇幅大小
            .setResultFormats(RESULT_FORMAT_JPEG, RESULT_FORMAT_PDF)//设置结果格式
            .setScannerMode(SCANNER_MODE_FULL)//要用到的功能，这选择全部
            .build()

        val scanner = GmsDocumentScanning.getClient(options)
        val scannerLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {//启动扫描程序 activity
                result ->
            run {
                if (result.resultCode == RESULT_OK) {
                    val result =
                        GmsDocumentScanningResult.fromActivityResultIntent(result.data)
                    result?.getPages()?.let { pages ->
                        for (page in pages) {
                            val imageUri = pages.get(0).getImageUri()
                        }
                    }
                    result?.getPdf()?.let { pdf ->
                        val pdfUri = pdf.getUri()
                        val pageCount = pdf.getPageCount()
                    }
                }
            }
        }


        mainActivityBinding.scanBtn.setOnClickListener {
            scanner.getStartScanIntent(this)
                .addOnSuccessListener { intentSender ->
                    scannerLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
                    Toast.makeText(applicationContext,"success",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
//                    Toast.makeText(applicationContext,it.message,Toast.LENGTH_LONG).show()
                    Log.d("2024-3-16", it.message.toString())
                }
        }


    }
}