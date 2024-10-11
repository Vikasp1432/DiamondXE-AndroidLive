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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MenuCustomDialog(context: Context, val txt24k:String, val txt22k:String,
                       val txt18k:String,
                       val txt14k:String, txt9k:String,) : Dialog(context) {

    var  cross : ImageView
    var  showring_button: RelativeLayout
    init {
        val view: View = LayoutInflater.from(context).inflate(R.layout.menushare_layout, null)
        setContentView(view)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        val txt_24kt: TextView = view.findViewById(R.id.txt_24kt)
        val txt_22kt: TextView = view.findViewById(R.id.txt_22kt)
        val txt_18kt: TextView = view.findViewById(R.id.txt_18kt)
        val txt_14kt: TextView = view.findViewById(R.id.txt_14kt)
        val txt_9kt: TextView = view.findViewById(R.id.txt_9kt)
        val goldratedate: TextView = view.findViewById(R.id.goldratedate)
        cross = view.findViewById(R.id.cross)
        showring_button= view.findViewById(R.id.showring_button)


        showring_button.setOnClickListener(){
            captureAndShareScreenshot()
        }

        val date = Date()
        val formatter = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault())
        val formattedDate = formatter.format(date)
        println(formattedDate)
        Log.e("formattedDate",",.63......."+formattedDate)
        goldratedate.setText("Gold Rate $formattedDate")

        txt_24kt.text =txt24k
        txt_22kt.text =txt22k
        txt_18kt.text =txt18k
        txt_14kt.text =txt14k
        txt_9kt.text =txt9k

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
        val file = File(context.getExternalFilesDir(null), "screenshotMe.png")
        return try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            Log.e("context.packageName : ", context.packageName.toString());
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
