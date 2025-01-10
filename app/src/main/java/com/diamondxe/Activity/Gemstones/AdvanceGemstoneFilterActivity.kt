package com.diamondxe.Activity.Gemstones

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import com.diamondxe.Adapter.GemOriginAdapter
import com.diamondxe.Adapter.GemTreatmentAdapter
import com.diamondxe.Adapter.GemstoneCuttingAdapter
import com.diamondxe.Adapter.GemstoneItemAdapter
import com.diamondxe.Adapter.GemstoneShapesAdapter
import com.diamondxe.Beans.GemstoneAttri.GemCuttingAttribute
import com.diamondxe.Beans.GemstoneAttri.GemItemsAttribute
import com.diamondxe.Beans.GemstoneAttri.GemShapeAttribute
import com.diamondxe.Beans.GemstoneAttri.GemTreatmentAttribute
import com.diamondxe.Interface.RecyclerInterface
import com.diamondxe.Network.SuperActivity
import com.diamondxe.R
import com.diamondxe.Utils.Constant
import com.diamondxe.databinding.ActivityAdvanceGemstoneFilterBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import org.json.JSONObject

class AdvanceGemstoneFilterActivity : SuperActivity() , RecyclerInterface{
    lateinit var binding: ActivityAdvanceGemstoneFilterBinding
    val gemShapeAttribute= ArrayList<GemShapeAttribute>()
    val gemItemsAttribute= ArrayList<GemItemsAttribute>()
    val gemTreatmentAttribute= ArrayList<GemTreatmentAttribute>()
    val gemCuttingAttribute= ArrayList<GemCuttingAttribute>()

    val shapseItems = listOf("Oval", "Cushion",
        "Octagonal","Pear","Round","Heart","Rectangle","Mixed Shapes",
        "Fancy","Triangular","Hexagonal","Marquise","Sugarloaf")
    val treatmentItems = listOf("Untreated", "Glass-Filled","Heated","Beryllium-Diffused")
    val Items = listOf("Precious Gemstone", "Semi Precious Gemstone")
    val cuttingStyleItems = listOf("Faceted", "Cabochon")
    private lateinit var adapterTre: GemTreatmentAdapter
    private lateinit var adapter: GemOriginAdapter
    private lateinit var adapterCutting: GemstoneCuttingAdapter
    private lateinit var adapterItem: GemstoneItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityAdvanceGemstoneFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backImg.setOnClickListener(this)
        binding.applyFilterTv.setOnClickListener(this)
       // Constant.shapsGem= ArrayList(shapseItems)

        //////////////////////////////  TREATMENT  //////////////////////////////
        treatmentItems.forEach { shape ->
            gemTreatmentAttribute.add(GemTreatmentAttribute(shape, false))
        }
        Constant.gemTreatmentAttributeArrayList= ArrayList(gemTreatmentAttribute)

        //////////////////////////////  SHAPES  //////////////////////////////
        shapseItems.forEach { shape ->
            gemShapeAttribute.add(GemShapeAttribute(shape, false))
        }
        Constant.shapsGemarraylist= ArrayList(gemShapeAttribute)

        //////////////////////////////  ITEM  //////////////////////////////
        Items.forEach { shape ->
            gemItemsAttribute.add(GemItemsAttribute(shape, false))
        }
        Constant.gemItemsAttributeArrayList= ArrayList(gemItemsAttribute)

        //////////////////////////////  CUTTING  //////////////////////////////
        cuttingStyleItems.forEach { shape ->
            gemCuttingAttribute.add(GemCuttingAttribute(shape, false))
        }
        Constant.gemCuttingAttributeArrayList= ArrayList(gemCuttingAttribute)
        ///////////////////////////////////////////////////////////////////////////////

        // Origin
        adapter = GemOriginAdapter(
            list = Constant.gemOriginAttributeArrayList,
            context = this,
            recyclerInterface = this
        )
        binding.recyclerOrigin.adapter = adapter
        val flexboxLayoutManagerShaes = FlexboxLayoutManager(this).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
            justifyContent = JustifyContent.FLEX_START
        }
        binding.recyclerOrigin.layoutManager = flexboxLayoutManagerShaes
        binding.recyclerOrigin.itemAnimator = DefaultItemAnimator()
        binding.recyclerOrigin.isNestedScrollingEnabled = false


        // Treatment
         adapterTre = GemTreatmentAdapter(
            list =Constant.gemTreatmentAttributeArrayList,
            context = this,
            recyclerInterface = this
        )
        binding.recyclerTreatmentView.adapter = adapterTre

        val flexboxLayoutManager = FlexboxLayoutManager(this).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
            justifyContent = JustifyContent.FLEX_START
        }
        binding.recyclerTreatmentView.layoutManager = flexboxLayoutManager
        binding.recyclerTreatmentView.itemAnimator = DefaultItemAnimator()
        binding.recyclerTreatmentView.isNestedScrollingEnabled = false

        //Item
         adapterItem = GemstoneItemAdapter(
            list = Constant.gemItemsAttributeArrayList,
            context = this,
            recyclerInterface = this
        )
        binding.recyclerItemView.adapter = adapterItem

        val flexboxLayoutManagerItems = FlexboxLayoutManager(this).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
            justifyContent = JustifyContent.FLEX_START
        }
        binding.recyclerItemView.layoutManager = flexboxLayoutManagerItems
        binding.recyclerItemView.itemAnimator = DefaultItemAnimator()
        binding.recyclerItemView.isNestedScrollingEnabled = false


        // Cutting Style
         adapterCutting = GemstoneCuttingAdapter(
            list = Constant.gemCuttingAttributeArrayList,
            context = this,
            recyclerInterface = this
        )
        binding.recyclerCuttingStyleView.adapter = adapterCutting

        val flexboxLayoutManagerCutting = FlexboxLayoutManager(this).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
            justifyContent = JustifyContent.FLEX_START
        }
        binding.recyclerCuttingStyleView.layoutManager = flexboxLayoutManagerCutting
        binding.recyclerCuttingStyleView.itemAnimator = DefaultItemAnimator()
        binding.recyclerCuttingStyleView.isNestedScrollingEnabled = false

    }

    override fun getSuccessResponce(jsonObject: JSONObject?, service_ID: Int) {

    }

    override fun getErrorResponce(error: String?, service_ID: Int) {

    }

    override fun onClick(p0: View?) {
       when(p0!!.id)
       {
           R.id.back_img ->{
               finish()
           }
           R.id.apply_filter_tv ->{

               val selectedShapeAttributeItems = gemShapeAttribute.filter { it.isSelected }.joinToString(",") { it.name }
               Log.e("selectedShapeAttributeItems","472......${selectedShapeAttributeItems}")
               Constant.selectedGemShapes=selectedShapeAttributeItems

               val selectedTreatmentAttributeItems = gemTreatmentAttribute.filter { it.isSelected }.joinToString(",") { it.name }
               Log.e("selectedTreatmentAttributeItems","472......${selectedTreatmentAttributeItems}")
               Constant.selectedGemTreatment=selectedTreatmentAttributeItems

               val selecteditemAttribute = gemItemsAttribute.filter { it.isSelected }.joinToString(",") { it.name }
               Log.e("selecteditemAttribute","472......${selecteditemAttribute}")
               Constant.selectedItemsAttribute=selecteditemAttribute

               val selectedCuttingAttributeItems = gemCuttingAttribute.filter { it.isSelected }.joinToString(",") { it.name }
               Log.e("selectedCuttingAttributeItems","472......${selectedCuttingAttributeItems}")
               Constant.selectedCuttingAttribute=selectedCuttingAttributeItems

               finish()
           }
       }
    }

    override fun itemClick(position: Int, action: String?) {
        Log.e("action", "191.......${action?.lowercase()}")
        when (action?.lowercase()) {
            "gemorigin" -> {
                Constant.gemOriginAttributeArrayList[position].isSelected =
                    !Constant.gemOriginAttributeArrayList[position].isSelected
                adapter.notifyDataSetChanged()
            }
            "gemtreatment" -> {

                Constant.gemTreatmentAttributeArrayList[position].isSelected = !Constant.gemTreatmentAttributeArrayList[position].isSelected
                adapterTre.notifyDataSetChanged()
            }
            "gemitem" -> {

                Constant.gemItemsAttributeArrayList[position].isSelected = !Constant.gemItemsAttributeArrayList[position].isSelected
                adapterItem.notifyDataSetChanged()
            }
            "gemcutting" -> {

                Constant.gemCuttingAttributeArrayList[position].isSelected = !Constant.gemCuttingAttributeArrayList[position].isSelected
                adapterCutting.notifyDataSetChanged()
            }
        }

    }
}