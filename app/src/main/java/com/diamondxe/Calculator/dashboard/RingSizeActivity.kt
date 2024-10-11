package com.dxe.calc.dashboard

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diamondxe.R
import com.diamondxe.databinding.ActivityRingSizeBinding
import com.dxe.calc.adapters.RingSizeAdapter
import com.dxe.calc.models.RingSize
import com.dxe.calc.utils.CustomDialog
import com.dxe.calc.utils.GridDrawable
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException


class RingSizeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRingSizeBinding
    private var selectedRingIndex: Int = 0
    private var selectSeekBarValue: Int = 0
    private lateinit var adapter: RingSizeAdapter
    private var debounceHandler: Handler = Handler(Looper.getMainLooper())
    private var debounceRunnable: Runnable? = null
    private lateinit var ringSizesList: List<RingSize>
    private   var mm:String =""
    private   var usa:String =""
    private   var eur:String  =""
    private   var uk:String =""
    private   var ind:String = ""
    var selectRing:Boolean=true
    private var isButtonClicked = false
    private var seekBarJob: Job? = null
    private var plusButtonJob: Job? = null
    private var updateUIJob: Job? = null

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRingSizeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener(){
            onBackPressed()
            finish()
        }
        binding.showringButton.setOnClickListener()
        {
            val customDialog = CustomDialog(this,mm,usa,eur,uk,ind)
            customDialog.show()
        }

        binding.ringselect.setOnClickListener()
        {
            selectRing=true
            binding.ringframe.visibility=View.VISIBLE
            binding.squareLayout.visibility=View.GONE
        }
        binding.fingerDeselect.setOnClickListener(){
            Log.e("Finger ","Deselect click.....")

        }

        binding.ringDeselect.setOnClickListener(){
            selectRing=true
            Log.e("Finger ","Deselect click.....")
            binding.fingerDeselect.visibility=View.GONE
            binding.ringDeselect.visibility=View.GONE
            binding.ringselect.visibility=View.VISIBLE
            binding.fingerselect.visibility=View.VISIBLE
            binding.ringframe.visibility=View.VISIBLE
            binding.squareLayout.visibility=View.GONE

            getSeekBarValue()


        }


        binding.fingerselect.setOnClickListener(){
            selectRing=false
            binding.fingerDeselect.visibility=View.VISIBLE
            binding.ringDeselect.visibility=View.VISIBLE
            binding.ringselect.visibility=View.GONE
            binding.fingerselect.visibility=View.GONE
            binding.ringframe.visibility=View.GONE
            binding.squareLayout.visibility=View.VISIBLE

            getSeekBarValue()
        }

        val gridDrawable = GridDrawable(
            gridSize = 40f,
            gridColor = Color.parseColor("#A089A2"),
            borderColor = Color.parseColor("#523355"),
            borderRadius = 25f
        )
        val layout = binding.mylayout
        layout.background = gridDrawable


        val jsonData = getJsonFromAssets(this, "sizes.json")
        val gson = Gson()
        val jsonObject = gson.fromJson(jsonData, JsonObject::class.java)
        val sizesArray = jsonObject.getAsJsonObject("data").getAsJsonArray("sizes")
        ringSizesList = gson.fromJson(sizesArray, Array<RingSize>::class.java).toList()


        val minusButton: RelativeLayout = binding.minusBtn
        val plusButton: ImageView = binding.plusBtn
        minusButton.setOnClickListener {
            if (isButtonClicked) return@setOnClickListener
            isButtonClicked = true
            Handler().postDelayed({ isButtonClicked = false }, 100)

            if (selectedRingIndex > 0) {
                selectedRingIndex--

                if(selectRing)
                {
                    updateUIForSelectedIndex(selectedRingIndex)
                }
                else
                {
                    updateUIForSelectedIndexSqure(selectedRingIndex)
                }
                mm=ringSizesList[selectedRingIndex].diameter_mm
                usa=ringSizesList[selectedRingIndex].usa
                eur=ringSizesList[selectedRingIndex].diameter_EUR
                uk=ringSizesList[selectedRingIndex].diameter_UK
                ind=ringSizesList[selectedRingIndex].india
             //   getSeekBarValue()

            }
        }

        plusButton.setOnClickListener {

            if (isButtonClicked) return@setOnClickListener
            isButtonClicked = true
            Handler().postDelayed({ isButtonClicked = false }, 100)

            if (selectedRingIndex < ringSizesList.size - 1) {
                selectedRingIndex++
                if(selectRing)
                {
                    updateUIForSelectedIndex(selectedRingIndex)
                }
                else
                {
                    updateUIForSelectedIndexSqure(selectedRingIndex)
                }
                mm=ringSizesList[selectedRingIndex].diameter_mm
                usa=ringSizesList[selectedRingIndex].usa
                eur=ringSizesList[selectedRingIndex].diameter_EUR
                uk=ringSizesList[selectedRingIndex].diameter_UK
                ind=ringSizesList[selectedRingIndex].india
               // getSeekBarValue()
            }
        }

        val recyclerView: RecyclerView = binding.rvRingsize
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RingSizeAdapter(ringSizesList, selectedRingIndex) { position, ringSize ->
            selectedRingIndex = position
            mm=ringSizesList[position].diameter_mm
            usa=ringSizesList[position].usa
            eur=ringSizesList[position].diameter_EUR
            uk=ringSizesList[position].diameter_UK
            ind=ringSizesList[position].india

            if(selectRing){
                binding.seekBar.progress = position
                adapter.updateSelectedIndex(position)
                adjustCircleSize(ringSize.diameter_mm.toFloat(),ringSize.india.toInt())
            }
            else
            {
                binding.seekBar.progress = position
                adapter.updateSelectedIndex(position)
                adjustSqureViewSize(ringSize.diameter_mm.toFloat(),ringSize.india.toInt())
            }
            selectSeekBarValue=position

        }

        recyclerView.adapter = adapter
        binding.seekBar.max = ringSizesList.size - 1
        binding.seekBar.progress = selectedRingIndex

        mm=ringSizesList[selectedRingIndex].diameter_mm
        usa=ringSizesList[selectedRingIndex].usa
        eur=ringSizesList[selectedRingIndex].diameter_EUR
        uk=ringSizesList[selectedRingIndex].diameter_UK
        ind=ringSizesList[selectedRingIndex].india

        val initialRingSize = ringSizesList[selectedRingIndex]
        adjustCircleSize(initialRingSize.diameter_mm.toFloat(),initialRingSize.india.toInt())
        adjustSqureViewSize(initialRingSize.diameter_mm.toFloat(),initialRingSize.india.toInt())




        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    Log.e("progress", "Progress: $progress")

                    selectSeekBarValue = progress
                    mm = ringSizesList[progress].diameter_mm
                    usa = ringSizesList[progress].usa
                    eur = ringSizesList[progress].diameter_EUR
                    uk = ringSizesList[progress].diameter_UK
                    ind = ringSizesList[progress].india

                    val selectedRingSize = ringSizesList[progress]
                    Log.e("selectedRingSize", "Selected Ring Size: $selectedRingSize")

                    if (selectRing) {
                        adjustCircleSize(selectedRingSize.diameter_mm.toFloat(), selectedRingSize.india.toInt())
                    } else {
                        adjustSqureViewSize(selectedRingSize.diameter_mm.toFloat(), selectedRingSize.india.toInt())
                    }

                    seekBarJob?.cancel()
                    seekBarJob = CoroutineScope(Dispatchers.Main).launch {
                        delay(100L)

                        if (progress != selectedRingIndex) {
                            selectedRingIndex = progress
                            adapter.updateSelectedIndex(progress)
                            recyclerView.scrollToPosition(progress)
                        }
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

    }


    fun adjustCircleSize(diameterMM: Float, ind: Int) {
        binding.circleView.adjustCircleSize(diameterMM,ind)
    }
    fun adjustSqureViewSize(diameterMM: Float, ind: Int) {
        binding.squreview.adjustSquareSize(diameterMM,ind)
    }


    fun getSeekBarValue()
    {
        binding.seekBar.progress=selectSeekBarValue
        val initialRingSize = ringSizesList[selectSeekBarValue]

        if(selectRing){
            adjustCircleSize(initialRingSize.diameter_mm.toFloat(),initialRingSize.india.toInt())
        }
        else{
            adjustSqureViewSize(initialRingSize.diameter_mm.toFloat(),initialRingSize.india.toInt())
        }

    }

    fun getJsonFromAssets(context: Context, fileName: String): String? {
        return try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun updateUIForSelectedIndex(index: Int) {
        binding.seekBar.progress = index
        //adapter.notifyItemChanged(index)
        val selectedRingSize = ringSizesList[index]
        Log.e("selectedRingSize","387....."+selectedRingSize.diameter_mm.toFloat())
        adjustCircleSize(selectedRingSize.diameter_mm.toFloat(),selectedRingSize.india.toInt())
        updateUIJob?.cancel()

        updateUIJob = CoroutineScope(Dispatchers.Main).launch {
            delay(100L)

            adapter.updateSelectedIndex(index)
            if (index in 0 until adapter.itemCount) {
                try {
                    binding.rvRingsize.post {
                        binding.rvRingsize.scrollToPosition(index)
                    }
                } catch (e: Exception) {
                    Log.e("Scroll Error", "Error scrolling to position $index: ${e.message}")
                }
            }
           // binding.rvRingsize.scrollToPosition(index)
        }

        selectSeekBarValue=index
        /*adapter.updateSelectedIndex(index)
        binding.rvRingsize.scrollToPosition(index)*/

    }

    private fun updateUIForSelectedIndexSqure(index: Int) {
        binding.seekBar.progress = index
        /*adapter.updateSelectedIndex(index)
        binding.rvRingsize.scrollToPosition(index)*/
       // adapter.notifyItemChanged(index)
        val selectedRingSize = ringSizesList[index]
        adjustSqureViewSize(selectedRingSize.diameter_mm.toFloat(),selectedRingSize.india.toInt())

        plusButtonJob?.cancel()

        plusButtonJob = CoroutineScope(Dispatchers.Main).launch {
            delay(100L)

            adapter.updateSelectedIndex(index)
           // binding.rvRingsize.scrollToPosition(index)
            if (index in 0 until adapter.itemCount) {
                try {
                    binding.rvRingsize.post {
                        binding.rvRingsize.scrollToPosition(index)
                    }
                } catch (e: Exception) {
                    Log.e("Scroll Error", "Error scrolling to position $index: ${e.message}")
                }
            }
        }
        selectSeekBarValue=index
    }

}