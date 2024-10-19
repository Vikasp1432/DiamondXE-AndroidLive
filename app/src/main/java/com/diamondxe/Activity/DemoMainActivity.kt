package com.diamondxe.Activity

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.ListPopupWindow
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.diamondxe.R
import com.google.android.material.snackbar.Snackbar
import com.jaredrummler.materialspinner.MaterialSpinner


class DemoMainActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {


    val ANDROID_VERSIONS: Array<String> = arrayOf(
        "Cupcake",
        "Donut",
        "Eclair",
        "Froyo",
        "Gingerbread",
        "Honeycomb",
        "Ice Cream Sandwich",
        "Jelly Bean",
        "KitKat",
        "Lollipop",
        "Marshmallow",
        "Nougat",
        "Oreo"
    )
    var languages = arrayOf("Java", "PHP", "Kotlin", "Javascript", "Python", "Swift", "Javascript", "Python", "Swift", "Javascript", "Python", "Swift", "Javascript", "Python", "Swift", "Javascript", "Python", "Swift", "Javascript", "Python", "Swift", "Javascript", "Python", "Swift", "Javascript", "Python", "Swift", "Javascript", "Python", "Swift", "Javascript", "Python", "Swift", "Javascript", "Python", "Swift", "Javascript", "Python", "Swift", "Javascript", "Python", "Swift", "Javascript", "Python", "Swift", "Javascript", "Python", "Swift", "Javascript", "Python", "Swift", "Javascript", "Python", "Swift", "Javascript", "Python", "Swift", "Javascript", "Python", "Swift", "Javascript", "Python", "Swift", "Javascript", "Python", "Swift")
    val NEW_SPINNER_ID = 1
   lateinit var  mySpinner:Spinner
   lateinit var linearLayout: LinearLayout

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_demo_main)
        var aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mySpinner=findViewById(R.id.mySpinner)
        linearLayout=findViewById(R.id.linearLayout)

        with(mySpinner)
        {
            adapter = aa
            setSelection(0, false)
            setSpinnerDropDownHeight(mySpinner,50)
            onItemSelectedListener = this@DemoMainActivity
            prompt = "Select your favourite language"
            gravity = Gravity.CENTER

        }


        try {
            val popupField = Spinner::class.java.getDeclaredField("mPopup")
            popupField.isAccessible = true


            // Get the ListPopupWindow instance
            val popupWindow = popupField[mySpinner] as ListPopupWindow


            // Set the desired height for the dropdown (for example, 300px)
            popupWindow.height = 300
        } catch (e: NoClassDefFoundError) {
            // silently fail...
        } catch (e: ClassCastException) {
        } catch (e: NoSuchFieldException) {
        } catch (e: IllegalAccessException) {
        }

        //setSpinnerDropDownHeight(mySpinner, 100)


        val spinner = Spinner(this)
        spinner.id = NEW_SPINNER_ID

        val ll = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        ll.setMargins(10, 40, 10, 10)
        linearLayout.addView(spinner)

        aa = ArrayAdapter(this, R.layout.spinner_right_aligned, languages)
        aa.setDropDownViewResource(R.layout.spinner_right_aligned)

        with(spinner)
        {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = this@DemoMainActivity
            layoutParams = ll
            prompt = "Select your favourite language"
            setPopupBackgroundResource(R.color.color_4C4E57)

        }
        setSpinnerDropDownHeight(spinner, 100)

        ///////////
        val spinner1 = findViewById<View>(R.id.spinner) as MaterialSpinner
        spinner1.setItems("Ice Cream Sandwich", "Jelly Bean", "KitKat", "Lollipop", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow", "Marshmallow")

        //spinner1.setItems(ANDROID_VERSIONS)

        spinner1.setOnItemSelectedListener { view, position, id, item ->
            Snackbar.make(view, "Clicked $item", Snackbar.LENGTH_LONG).show()
        }

        spinner1.setOnNothingSelectedListener {
            Snackbar.make(spinner1, "Nothing selected", Snackbar.LENGTH_LONG).show()
        }


        ////////////////////////
        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        autoCompleteTextView.setScrollbarFadingEnabled(false);

        // List of 25 items to display in the dropdown
        val intensities = arrayOf(
            "Faint", "Very Light", "Light", "Fancy", "Deep", "Dark",
            "Item 7", "Item 8", "Item 9", "Item 10", "Item 11",
            "Item 12", "Item 13", "Item 14", "Item 15", "Item 16",
            "Item 17", "Item 18", "Item 19", "Item 20", "Item 21",
            "Item 22", "Item 23", "Item 24", "Item 25"
        )


        // Set the adapter for the dropdown list
        val adapter = ArrayAdapter(
            this,
            R.layout.custom_dropdown_item,
            R.id.item,
            intensities,

        )

        // Attach the adapter to the AutoCompleteTextView
        autoCompleteTextView.setAdapter(adapter)
        val thumbDrawable = ColorDrawable(ContextCompat.getColor(this, R.color.yellow))
        val trackDrawable = ColorDrawable(ContextCompat.getColor(this, R.color.teal_700))

// Set the vertical scrollbar thumb and track color
        autoCompleteTextView.isVerticalScrollBarEnabled = true
        autoCompleteTextView.setVerticalScrollbarThumbDrawable(thumbDrawable)
        autoCompleteTextView.setVerticalScrollbarTrackDrawable(trackDrawable)

        autoCompleteTextView.setDropDownBackgroundDrawable(ContextCompat.getDrawable(this,
            R.drawable.spinner_popup_bg))
        autoCompleteTextView.dropDownHeight = 350
        autoCompleteTextView.dropDownVerticalOffset=1
        autoCompleteTextView.scrollBarStyle=R.style.CustomAutoCompleteTextView
        autoCompleteTextView.scrollIndicators
        autoCompleteTextView.scrollBarSize=23
        autoCompleteTextView.scrollBarDefaultDelayBeforeFade=90
        autoCompleteTextView.setOnClickListener { v: View? -> autoCompleteTextView.showDropDown() }
        autoCompleteTextView.setScrollbarFadingEnabled(false)
        autoCompleteTextView.isCursorVisible = true
        autoCompleteTextView.verticalScrollbarWidth
        autoCompleteTextView.isVerticalScrollBarEnabled=true
// Access and modify the PopupWindow to enforce scrollbar visibility
        try {
            val field = AutoCompleteTextView::class.java.getDeclaredField("mPopup")
            field.isAccessible = true
            val popupWindow = field.get(autoCompleteTextView) as ListView

            // Ensure the scrollbar is always visible
            popupWindow.isVerticalScrollBarEnabled = true
            popupWindow.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            popupWindow.isFastScrollEnabled = true
            popupWindow.setScrollBarDefaultDelayBeforeFade(0)  // Ensure scrollbar stays visible
            popupWindow.setScrollbarFadingEnabled(false)

           /* popupWindow.listView?.isVerticalScrollBarEnabled = true
            popupWindow.listView?.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY*/

        } catch (e: Exception) {
            e.printStackTrace()
        }

        /////////////////////////////////////////////////////////
        val languages = resources.getStringArray(R.array.programming_languages)
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        val arrayAdapter = ArrayAdapter(this, R.layout.custom_dropdown_item, R.id.item, languages)
        // get reference to the autocomplete text view
        val autocompleteTV = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        // set adapter to the autocomplete tv to the arrayAdapter
        autocompleteTV.setAdapter(arrayAdapter)
        /////////////////////////////////
        val items = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4")

        // Adapter for dropdown

        // Assign adapter to the AutoCompleteTextView inside TextInputLayout
        val dropdownMenu = findViewById<AutoCompleteTextView>(R.id.spinner_fancy_intensity_view)
        val adapter1 = object : ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items) {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)

                // Apply custom scrollbar color on the ListView (dropdown)
                val listView = parent as ListView
                listView.isVerticalScrollBarEnabled = true
                listView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
                listView.scrollBarSize = 10 // Custom scrollbar size

                // Set custom scrollbar thumb drawable
                listView.setOnScrollChangeListener { _, _, _, _, _ ->
                    listView.setVerticalScrollbarThumbDrawable(
                        ContextCompat.getDrawable(context, R.drawable.scrollbar_custom)
                    )
                }

                return view
            }
        }

        dropdownMenu.setAdapter(adapter1)
        dropdownMenu.dropDownHeight = dpToPx(200)


    }

    fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
    private fun setSpinnerDropDownHeight(spinner: Spinner, height: Int) {
        try {
            val popup = Spinner::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true

            val popupWindow = popup.get(spinner) as android.widget.ListPopupWindow
            popupWindow.height = height // Set your desired height here
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
        showToast(message = "Nothing selected")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when (view?.id) {
            1 -> showToast(message = "Spinner 2 Position:${position} and language: ${languages[position]}")
            else -> {
                showToast(message = "Spinner 1 Position:${position} and language: ${languages[position]}")
            }
        }
    }

    private fun showToast(context: Context = applicationContext, message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
    }
}