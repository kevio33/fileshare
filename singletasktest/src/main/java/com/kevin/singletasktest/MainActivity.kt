package com.kevin.singletasktest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kevin.singletasktest.databinding.ActivityMainBinding


/**
 * singleTask案例
 */
class MainActivity : AppCompatActivity() {

    lateinit var bindingActivity: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingActivity = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingActivity.root)


        bindingActivity.btnActivity2.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                val intent = Intent(this@MainActivity,Activity2::class.java)
                startActivity(intent)
            }
        })

        bindingActivity.btnActivity3.setOnClickListener {
            val intent = Intent(this@MainActivity,Activity3::class.java)
            startActivity(intent)
        }
    }
}