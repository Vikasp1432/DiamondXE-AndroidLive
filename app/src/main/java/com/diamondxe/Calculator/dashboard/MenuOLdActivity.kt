package com.dxe.calc.dashboard

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.diamondxe.R
import com.diamondxe.databinding.ActivityMenuBinding
import com.dxe.calc.CurrencyListActivity
import com.dxe.calc.WebViewActivity
import com.dxe.calc.login.RapnetLoginBottomSheet

class MenuOLdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rlBack.setOnClickListener { finish() }

        binding.labelGiaReport.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("url", "https://www.gia.edu/report-check-landing")
            intent.putExtra("title", getString(R.string.txt_gia_report))
            startActivity(intent)
        }

        binding.labelIgiReport.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("url", "https://www.igi.org/verify-your-report/en")
            intent.putExtra("title", getString(R.string.txt_igi_report))
            startActivity(intent)
        }

        binding.labelRapnetLogin.setOnClickListener {
            RapnetLoginBottomSheet().show(supportFragmentManager, RapnetLoginBottomSheet.TAG)
        }

        binding.labelCurrency.setOnClickListener { startActivity(Intent(this, CurrencyListActivity::class.java)) }

        try {
            val pInfo: PackageInfo =
                packageManager.getPackageInfo(packageName, 0)
            val version: String = pInfo.versionName
            val res: Resources = resources
            val text: String = res.getString(R.string.txt_app_version, version)
            binding.tvAppVersion.text = text
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }


}