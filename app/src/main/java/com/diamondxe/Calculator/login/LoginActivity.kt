package com.dxe.calc.login

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import androidx.appcompat.app.AppCompatActivity
import com.diamondxe.R
import com.diamondxe.databinding.ActivityLoginBinding
import com.dxe.calc.dashboard.CalculatorActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rlShowHidePassword.setOnClickListener {
            showHidePassword()
        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, CalculatorActivity::class.java))
        }

    }

    private fun showHidePassword() {
        if (isPasswordVisible) {
            binding.edtPassword.transformationMethod = null
            binding.ivPasswordVisibility.background = getDrawable(R.drawable.hide_password)
        } else {
            binding.edtPassword.transformationMethod = PasswordTransformationMethod()
            binding.ivPasswordVisibility.background = getDrawable(R.drawable.show_password)
        }
        isPasswordVisible = !isPasswordVisible
    }

}