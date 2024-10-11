package com.dxe.calc.dashboard

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.diamondxe.R
import com.diamondxe.databinding.ActivityGetQuotationBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DecimalFormat

class GetQuotationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGetQuotationBinding
    private  var  getTaxAmountFinal:String =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityGetQuotationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backButton.setOnClickListener(){
            finish()
        }

        binding.shareQuo.setOnClickListener(){
            captureAndShareScreenshot()
        }

        binding.menu.setOnClickListener(){
            startActivity(Intent(this, QuotationsListActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }



        val date = intent.getStringExtra("date")?.takeIf { it.isNotBlank() } ?: "0.0"
        binding.currentdate.setText(date)

        val metalwt = intent.getStringExtra("metalwt")?.takeIf { it.isNotBlank() } ?: "0.0"
        val metalrategm = intent.getStringExtra("metalrategm")?.takeIf { it.isNotBlank() } ?: "0.0"
        val metalrateop = intent.getStringExtra("metalrateop")?.takeIf { it.isNotBlank() } ?: "0"

        val labour = intent.getStringExtra("labour")?.takeIf { it.isNotBlank() } ?: "0.0"
        val labourrategm = intent.getStringExtra("labourrategm")?.takeIf { it.isNotBlank() } ?: "0.0"
        val labourop = intent.getStringExtra("labourop")?.takeIf { it.isNotBlank() } ?: "0"

        val solitaire = intent.getStringExtra("solitaire")?.takeIf { it.isNotBlank() } ?: "0.0"
        val solitairerategm = intent.getStringExtra("solitairerategm")?.takeIf { it.isNotBlank() } ?: "0.0"
        val solitaireop = intent.getStringExtra("solitaireop")?.takeIf { it.isNotBlank() } ?: "0"

        val sidedia = intent.getStringExtra("sidedia")?.takeIf { it.isNotBlank() } ?: "0.0"
        val sidediarategm = intent.getStringExtra("sidediarategm")?.takeIf { it.isNotBlank() } ?: "0.0"
        val sidediaop = intent.getStringExtra("sidediaop")?.takeIf { it.isNotBlank() } ?: "0"

        val colstonewt = intent.getStringExtra("colstonewt")?.takeIf { it.isNotBlank() } ?: "0.0"
        val colstonerategm = intent.getStringExtra("colstonerategm")?.takeIf { it.isNotBlank() } ?: "0.0"
        val colstoneop = intent.getStringExtra("colstoneop")?.takeIf { it.isNotBlank() } ?: "0"

        val charges = intent.getStringExtra("charges")?.takeIf { it.isNotBlank() } ?: "0"
        val tax = intent.getStringExtra("tax")?.takeIf { it.isNotBlank() } ?: "0"
        var totalPrice = intent.getStringExtra("totalPrice")?.takeIf { it.isNotBlank() } ?: "0"


        val itemName = intent.getStringExtra("itemName")?.takeIf { it.isNotBlank() } ?: ""
        val radiobuttonName = intent.getStringExtra("radiobuttonName")?.takeIf { it.isNotBlank() } ?: ""
        val caret = intent.getStringExtra("caret")?.takeIf { it.isNotBlank() } ?: ""
        val chargeText = intent.getStringExtra("chargeText")?.takeIf { it.isNotBlank() } ?: ""

        val solitaireText = intent.getStringExtra("solitaireText")?.takeIf { it.isNotBlank() } ?: ""
        val sideDIAText = intent.getStringExtra("sideDIAText")?.takeIf { it.isNotBlank() } ?: ""

        val intentID = intent.getLongExtra("ID", -1L).takeIf { it != -1L }

        binding.itemname.setText(itemName)
        binding.itemcaert.setText(caret)
        binding.diamondname.setText(radiobuttonName)
        binding.othercharge.setText(chargeText)
        binding.sideDIA.setText(sideDIAText)
        binding.solitairetxt.setText(solitaireText)



        binding.editQuotation.setOnClickListener(){
            val intent = Intent(this, JewelleryActivity::class.java)
            intent.putExtra("ID", intentID)
            intent.putExtra("metalwt", metalwt)
            intent.putExtra("metalrategm", metalrategm)
            intent.putExtra("metalrateop", metalrateop)

            intent.putExtra("labour", labour)
            intent.putExtra("labourrategm", labourrategm)
            intent.putExtra("labourop", labourop)

            intent.putExtra("solitaire", solitaire)
            intent.putExtra("solitairerategm", solitairerategm)
            intent.putExtra("solitaireop", solitaireop)

            intent.putExtra("sidedia", sidedia)
            intent.putExtra("sidediarategm", sidediarategm)
            intent.putExtra("sidediaop", sidediaop)

            intent.putExtra("colstonewt", colstonewt)
            intent.putExtra("colstonerategm", colstonerategm)
            intent.putExtra("colstoneop", colstoneop)

            intent.putExtra("charges", charges)
            intent.putExtra("tax", tax)
            intent.putExtra("totalPrice", totalPrice)

            intent.putExtra("itemName", itemName)
            intent.putExtra("radiobuttonName", radiobuttonName)
            intent.putExtra("caret", caret)
            intent.putExtra("chargeText", charges)
            intent.putExtra("solitaireText", solitaireText)
            intent.putExtra("sideDIAText", sideDIAText)

            startActivity(intent)

        }
        val metalrateop1 = metalrateop!!.toDoubleOrNull() ?: 0.0
        val labourop1 = labourop!!.toDoubleOrNull() ?: 0.0
        val solitaireop1 = solitaireop!!.toDoubleOrNull() ?: 0.0
        val sidediaop1 = sidediaop!!.toDoubleOrNull() ?: 0.0
        val colstoneop1 = colstoneop!!.toDoubleOrNull() ?: 0.0
        val charges1 = charges!!.toDoubleOrNull() ?: 0.0
        val tax1 = tax!!.toDoubleOrNull() ?: 0.0
        Log.e("tax", "Tax Amount: $tax")
        if (tax!="0" )
        {
            Log.e("totalPrice","..."+totalPrice)
            val totalPrice1 = totalPrice?.toDoubleOrNull() ?: 0.0
           /* ye meko amount ni de raha h blank aa raha h correct code bejo*/

            val totalWithoutDiscount = metalrateop1 + labourop1 + solitaireop1 + sidediaop1 + colstoneop1 + charges1

            Log.e("totalWithoutDiscount", "Tax Amount: $totalWithoutDiscount")
            Log.e("totalPrice1", "Tax Amount: $totalPrice1")
            var getTaxAmount = totalPrice1 - totalWithoutDiscount

            Log.e("getTaxAmount", "Tax Amount: $getTaxAmount")

            val integerPart = getTaxAmount.toInt()
            val fractionalPart = getTaxAmount - integerPart
            val roundedOutput = if (fractionalPart > 0.50) {
                integerPart + 1
            } else {
                integerPart
            }

            Log.e("roundedOutput", "Rounded Output: $roundedOutput")
            getTaxAmountFinal=roundedOutput.toString()

        }
        else{
            getTaxAmountFinal="0.0"
        }


        val data = arrayOf(
            arrayOf("Details", "Weight", "Rate", "Total"),
            arrayOf("Metal", metalwt +"gms", metalrategm, formatAmountWithCommas(metalrateop)),
            arrayOf("Labour", labour+"gms", labourrategm, formatAmountWithCommas(labourop)),
            arrayOf("Solitaire wt.", solitaire+"ct", solitairerategm, formatAmountWithCommas(solitaireop)),
            arrayOf("Side DIA wt.", sidedia+"ct", sidediarategm, formatAmountWithCommas(sidediaop)),
            arrayOf("Col. Stone wt.", colstonewt+"ct", colstonerategm, formatAmountWithCommas(colstoneop)),
            arrayOf("Other charges", "--", "--", formatAmountWithCommas(charges)),
            arrayOf("Tax", "--", tax,formatAmountWithCommas(getTaxAmountFinal)),
            //arrayOf("Total Price (INR)*", "", "", totalPrice)
            arrayOf("Total Price (INR)*", "", "", formatAmountWithCommas(totalPrice))
        )
        binding.parentLinearLayout.setPadding(1, 2, 0, 1)
        binding.parentLinearLayout.background = createOuterBorder()

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(2, 2, 2, 2)
        binding.tableLayout.layoutParams = layoutParams
        val latoFont = ResourcesCompat.getFont(this, R.font.lato_regular)

        for ((index, row) in data.withIndex()) {
            val tableRow = TableRow(this)
            tableRow.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )


            for (cell in row) {
                val textView = TextView(this)
                textView.text = cell
                textView.setPadding(18, 25, 18, 25)
                textView.gravity = android.view.Gravity.START
                textView.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )

                if (index == 8) {
                    textView.textSize = 16f
                    textView.typeface = Typeface.create(latoFont, Typeface.BOLD)
                    textView.setTextColor(Color.BLACK)
                    textView.setBackgroundColor(Color.TRANSPARENT)
                    textView.background = null

                }
                else if (index == 0) {
                    textView.textSize = 12f
                    textView.setTextColor(Color.BLACK)
                    textView.typeface = Typeface.create(latoFont, Typeface.NORMAL)
                    textView.setBackgroundColor(Color.WHITE)
                    //textView.setBackgroundColor(getColor(R.color.color_F0F0F0))
                    textView.background = createCellHeaderBorder()
                } else {
                    textView.textSize = 13f
                    textView.setTextColor(Color.BLACK)
                    textView.typeface = Typeface.create(latoFont, Typeface.NORMAL)
                    textView.setBackgroundColor(Color.WHITE)
                    textView.background = createCellBorder()
                }

                tableRow.addView(textView)
            }

            binding.tableLayout.addView(tableRow)
        }

    }

    fun createOuterBorder(): GradientDrawable {
        val outerBorder = GradientDrawable()
        outerBorder.shape = GradientDrawable.RECTANGLE
        outerBorder.setStroke(4, getColor(R.color.color_CBC6C2))
        outerBorder.cornerRadius = 8f
        outerBorder.setColor(Color.WHITE)
        return outerBorder
    }

    fun createCellHeaderBorder(): GradientDrawable {
        val cellBorder = GradientDrawable()
        cellBorder.shape = GradientDrawable.RECTANGLE
        cellBorder.setStroke(1, getColor(R.color.color_CBC6C2))
        cellBorder.setColor(getColor(R.color.color_F0F0F0))
        return cellBorder
    }

    fun createCellBorder(): GradientDrawable {
        val cellBorder = GradientDrawable()
        cellBorder.shape = GradientDrawable.RECTANGLE
        cellBorder.setStroke(1, getColor(R.color.color_CBC6C2))
        cellBorder.setColor(Color.WHITE)
        return cellBorder
    }

    fun formatAmountWithCommas(amount: String): String {
        val formatter = DecimalFormat("#,##,###")
        val parsedAmount = amount.toBigDecimalOrNull() ?: return amount
        return formatter.format(parsedAmount)
    }


    private fun captureAndShareScreenshot() {

        val bitmap = Bitmap.createBitmap(binding.mainlayout.width, binding.mainlayout.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        binding.mainlayout.draw(canvas)


        val screenshotUri = saveImageToInternalStorage(bitmap)
        if (screenshotUri != null) {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, screenshotUri)
                type = "image/png"
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            val resolvedActivity = packageManager.resolveActivity(shareIntent, PackageManager.MATCH_DEFAULT_ONLY)
            if (resolvedActivity != null) {
                grantUriPermission(
                    resolvedActivity.activityInfo.packageName,
                    screenshotUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            startActivity(Intent.createChooser(shareIntent, "Share Screenshot"))
        } else {
            Log.e("Error", "Screenshot saving failed.")
        }
    }


    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri? {
        val file = File(getExternalFilesDir(null), "screenshot.png")
        return try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            FileProvider.getUriForFile(this, "${packageName}.fileprovider", file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}