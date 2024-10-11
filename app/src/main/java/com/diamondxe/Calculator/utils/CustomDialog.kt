package com.dxe.calc.utils


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.FileProvider
import com.diamondxe.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CustomDialog(context: Context,val mm:String,val usa:String,
                   val eur:String,
                   val uk:String,val ind:String,) : Dialog(context) {

             var  cross :ImageView
             var  showring_button: RelativeLayout
    init {
        val view: View = LayoutInflater.from(context).inflate(R.layout.custom_dialog_ringsize, null)
        setContentView(view)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        val mm_tv: TextView = view.findViewById(R.id.mm_tv)
        val usa_tv: TextView = view.findViewById(R.id.usa_tv)
        val eur_tv: TextView = view.findViewById(R.id.eur_tv)
        val uk_tv: TextView = view.findViewById(R.id.uk_tv)
        val ind_tv: TextView = view.findViewById(R.id.ind_tv)
         cross = view.findViewById(R.id.cross)
        showring_button= view.findViewById(R.id.showring_button)


        showring_button.setOnClickListener(){
            captureAndShareScreenshot()
        }

        mm_tv.text =mm
            usa_tv.text =usa
            eur_tv.text =eur
            uk_tv.text =uk
            ind_tv.text =ind

        cross.setOnClickListener {
            dismiss()
        }

        val margin = 20
        val layoutParamsDialog = view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParamsDialog.setMargins(margin, margin, margin, margin)
        view.layoutParams = layoutParamsDialog

    }

    private fun captureAndShareScreenshot() {
        cross.visibility = View.INVISIBLE
        showring_button.visibility = View.INVISIBLE

        val layout = findViewById<LinearLayout>(R.id.layout)
        val bitmap = Bitmap.createBitmap(layout.width, layout.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        layout.draw(canvas)

        cross.visibility = View.VISIBLE
        showring_button.visibility = View.VISIBLE

        val screenshotUri = saveImageToInternalStorage(bitmap)
        if (screenshotUri != null) {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, screenshotUri)
                type = "image/png"
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            val resolvedActivity = context.packageManager.resolveActivity(shareIntent, PackageManager.MATCH_DEFAULT_ONLY)
            if (resolvedActivity != null) {
                context.grantUriPermission(
                    resolvedActivity.activityInfo.packageName,
                    screenshotUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share Screenshot"))
        } else {
            Log.e("Error", "Screenshot saving failed.")
        }
    }


    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri? {
        val file = File(context.getExternalFilesDir(null), "screenshotitem.png")
        return try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
