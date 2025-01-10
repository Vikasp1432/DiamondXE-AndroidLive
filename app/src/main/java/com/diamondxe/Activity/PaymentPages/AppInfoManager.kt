package com.diamondxe.Activity.PaymentPages

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

object AppInfoManager {

    fun getAppVersion(context:Context):String{
        return  try {
            val packageInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName ?: "Unknown"
        }
        catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            "Unknown"
        }
    }
}