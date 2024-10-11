package com.dxe.calc

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.diamondxe.databinding.ActivitySplashBinding
import com.dxe.calc.dashboard.CalculatorActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed({
            startActivity(Intent(this, CalculatorActivity::class.java))
            finish()
        }, 1000)
       /* startActivity(Intent(this@SplashActivity, CalculatorActivity::class.java))
        finish()*/

    }


}