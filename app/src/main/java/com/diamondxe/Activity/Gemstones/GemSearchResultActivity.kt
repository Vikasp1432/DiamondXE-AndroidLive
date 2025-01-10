package com.diamondxe.Activity.Gemstones

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.Transformation
import android.view.animation.TranslateAnimation
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diamondxe.Activity.DiamondDetailsActivity
import com.diamondxe.Activity.Gemstones.GemstoneAdapter.GemStoneSearchResultListAdapter
import com.diamondxe.Activity.Gemstones.GemstoneAdapter.GemstoneSearchResultAdapter
import com.diamondxe.Activity.HomeScreenActivity
import com.diamondxe.Activity.LoginScreenActivity
import com.diamondxe.Activity.MyCardListScreenActivity
import com.diamondxe.Adapter.CurrencyListAdapter
import com.diamondxe.Adapter.FilterApplied.ColorTypeAppliedFilterDataAdapter
import com.diamondxe.ApiCalling.ApiConstants
import com.diamondxe.ApiCalling.VollyApiActivity
import com.diamondxe.Beans.AttributeDetailsModel
import com.diamondxe.Beans.CountryListModel
import com.diamondxe.Beans.SearchGemstoneTypeModel
import com.diamondxe.Interface.RecyclerInterface
import com.diamondxe.Network.EndlessRecyclerViewScrollListener
import com.diamondxe.Network.SuperActivity
import com.diamondxe.R
import com.diamondxe.Utils.CommonUtility
import com.diamondxe.Utils.Constant
import com.diamondxe.Utils.Constant.searchType
import com.diamondxe.Utils.Constant.selectedPriceGem
import com.diamondxe.Utils.Constant.selectedWeightGem
import com.diamondxe.Utils.Constant.selectedshapeTypesItems
import com.diamondxe.Utils.Utils
import com.diamondxe.databinding.ActivitySearchResultBinding
import com.dxe.calc.dashboard.CalculatorActivity
import com.squareup.picasso.Picasso
import org.json.JSONObject

class GemSearchResultActivity : SuperActivity(), RecyclerInterface {

    private var activity: Activity? = null
    private var context: Context? = null
    var pageNo: Int = 1
    var stocklocation: String = ""
    var user_login: String = ""
    var checkOtherLocation: Int = 0
    lateinit var vollyApiActivity: VollyApiActivity
    lateinit var binding: ActivitySearchResultBinding
    var cardViewAndListViewParttenShow: String = "CardView"
    var layoutManager: LinearLayoutManager? = null
    var countryListAdapter: CurrencyListAdapter? = null
    var gemstoneSearchResultListAdapter: GemStoneSearchResultListAdapter? = null
    var gemstoneSearchResultAdapter: GemstoneSearchResultAdapter? = null
    lateinit var scrollListener: EndlessRecyclerViewScrollListener
    lateinit var colorTypeAppliedFilterDataAdapter: ColorTypeAppliedFilterDataAdapter
    var modelArrayList: ArrayList<SearchGemstoneTypeModel>? = null
    var localCurrencyArrayList: ArrayList<CountryListModel> = ArrayList()
    var selectedCurrencyValue = ""
    var selectedCurrencyCode = ""
    var selectedCurrencyDesc = ""
    var selectedCurrencyImage = ""
    private var isArrowDown = false
    var manageAPICall = ""
    private lateinit var attributeNames: List<AttributeDetailsModel>
    var lastPosition: Int = 0
    lateinit var urlParameter: java.util.HashMap<String, String>
    var attributeDetailsModels: java.util.ArrayList<AttributeDetailsModel> = java.util.ArrayList()

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivitySearchResultBinding.inflate(layoutInflater)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backImg.setOnClickListener(this)
        context = this.also { activity = it }

        binding.diamondtypeText.text=getText(R.string.gemstone_type)
        binding.cardViewImg.setOnClickListener(this)
        binding.listViewImg.setOnClickListener(this)
        binding.bottomBatInclude.wishlistRel.setOnClickListener(this)
        binding.bottomBatInclude.cartRel.setOnClickListener(this)
        binding.bottomBatInclude.homeRel.setOnClickListener(this)
        binding.bottomBatInclude.accountTv.setOnClickListener(this)
        binding.bottomBatInclude.accountImg.setOnClickListener(this)
        binding.bottomBatInclude.accountRel.setOnClickListener(this)
        binding.bottomBatInclude.dxeCalcRev.setOnClickListener(this)
        binding.bottomBatInclude.linEnquiry.setOnClickListener(this)
        binding.bottomBatInclude.countryLayoutRel.setOnClickListener(this)
        binding.bottomBatInclude.searchCircleCard.setOnClickListener(this)
        binding.bottomBatInclude.searchCircleCard1CrossCountry.setOnClickListener(this)
        binding.bottomBatInclude.bottomSearchIcon.setBackgroundResource(R.drawable.plus)
        binding.naturalTv.setOnClickListener(this)
        binding.stockLv.setOnClickListener(this)
        binding.grownTv.setOnClickListener(this)
        binding.sortImg.setOnClickListener(this)
        binding.dropdown.setOnClickListener(this)

        binding.filterTypeLin.setOnClickListener(this)
        modelArrayList = ArrayList()

        val layoutManagerAppliedFilter = LinearLayoutManager(activity)
        layoutManagerAppliedFilter.orientation = LinearLayoutManager.HORIZONTAL
        binding.recyclerViewApplyFilter.setLayoutManager(layoutManagerAppliedFilter)
        binding.recyclerViewApplyFilter.setNestedScrollingEnabled(false)

        // Log.e("colorType","FilterApploedArrayList.....336....."+Constant.colorTypeFilterApploedArrayList.size);
        colorTypeAppliedFilterDataAdapter = ColorTypeAppliedFilterDataAdapter(
            Constant.colorTypeFilterApploedArrayList, context,
            this
        )

        binding.recyclerViewApplyFilter.setAdapter(colorTypeAppliedFilterDataAdapter)
        binding.nestedView.setVisibility(View.GONE)
        binding.arrowImg.setImageResource(R.drawable.drop_up)
        isArrowDown = true
        layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.setLayoutManager(layoutManager)
        binding.recyclerView.isNestedScrollingEnabled = false

        gemstoneSearchResultAdapter =
            GemstoneSearchResultAdapter(context!!, "userRole", modelArrayList!!, this)
        binding.recyclerView.adapter = gemstoneSearchResultAdapter

        val layoutManagerCountryList = LinearLayoutManager(activity)

        //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.bottomBatInclude.recyclerViewCountryList.setLayoutManager(layoutManagerCountryList)
        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                Log.e("Load more", "...Gemsearch...######.........74")

                Log.e("Load more", ".1.....######.........74.." + modelArrayList!!.size)
                Log.e("Load more", ".2.....######.........74.." + Constant.lazyLoadingLimit)

                if (modelArrayList!!.size >= Constant.lazyLoadingLimit) {
                    //showLoadingIndicator(true);
                    Log.e("Load more", ".Inside if.......74..")
                    shimmerShow()
                    pageNo = pageNo + 1
                    onBindDetails(false)
                } else {
                }
            }
        }
        binding.recyclerView.addOnScrollListener(scrollListener)



        binding.bottomBatInclude.accountTv.setTextColor(
            ContextCompat.getColor(
                context!!,
                R.color.grey_light
            )
        )

        user_login = CommonUtility.getGlobalString(context, "user_login")

        // If User Login then  User Role Name
        if (!user_login.equals("", ignoreCase = true)) {
            val role = CommonUtility.getGlobalString(context, "login_user_role")
            if (role.equals("BUYER", ignoreCase = true)) {
                binding.bottomBatInclude.accountTv.setText(ApiConstants.USER_BUYER)
            } else if (role.equals("DEALER", ignoreCase = true)) {
                binding.bottomBatInclude.accountTv.setText(ApiConstants.USER_DEALER)
            } else {
            }
        } else {
            binding.bottomBatInclude.accountTv.setText(resources.getString(R.string.login))
        }

        getCurrencyData()

        binding.cardViewImg.setColorFilter(ContextCompat.getColor(context!!, R.color.purple_light))
        binding.listViewImg.setColorFilter(ContextCompat.getColor(context!!, R.color.black))
        CommonUtility.startZoomAnimation(binding.bottomBatInclude.bottomSearchIcon)

        if (searchType.equals(ApiConstants.NATURAL)) {
            naturalCardTabColorSet()
        } else {
            labGrownCardTabColorSet()
        }
        onAttributeCall(true)
        setFirstPositionCountry()
        showCardCount()
        updateLocalCurrencyList()

        binding.bottomBatInclude.homeImg.setColorFilter(
            ContextCompat.getColor(
                context!!,
                R.color.grey_light
            )
        )
        binding.bottomBatInclude.categoriesImg.setColorFilter(
            ContextCompat.getColor(
                context!!,
                R.color.grey_light
            )
        )
        binding.bottomBatInclude.wishImg.setColorFilter(
            ContextCompat.getColor(
                context!!,
                R.color.grey_light
            )
        )
        binding.bottomBatInclude.cartImg.setColorFilter(
            ContextCompat.getColor(
                context!!,
                R.color.grey_light
            )
        )
        binding.bottomBatInclude.accountImg.setColorFilter(
            ContextCompat.getColor(
                context!!,
                R.color.grey_light
            )
        )

        binding.bottomBatInclude.homeTv.setTextColor(
            ContextCompat.getColor(
                context!!,
                R.color.grey_light
            )
        )
        binding.bottomBatInclude.categoriesTv.setTextColor(
            ContextCompat.getColor(
                context!!,
                R.color.grey_light
            )
        )
        binding.bottomBatInclude.wishTv.setTextColor(
            ContextCompat.getColor(
                context!!,
                R.color.grey_light
            )
        )
        binding.bottomBatInclude.cartTv.setTextColor(
            ContextCompat.getColor(
                context!!,
                R.color.grey_light
            )
        )
        binding.bottomBatInclude.accountTv.setTextColor(
            ContextCompat.getColor(
                context!!,
                R.color.grey_light
            )
        )
    }

    fun getCurrencyData() {
        selectedCurrencyValue = CommonUtility.getGlobalString(context, "selected_currency_value")
        selectedCurrencyCode = CommonUtility.getGlobalString(context, "selected_currency_code")
        selectedCurrencyDesc = CommonUtility.getGlobalString(context, "selected_currency_desc")
        selectedCurrencyImage = CommonUtility.getGlobalString(context, "selected_currency_image")
    }


    fun showCardCount() {
        if (Constant.cardCount.equals("", ignoreCase = true) || Constant.cardCount.equals(
                "0",
                ignoreCase = true
            )
        ) {
            binding.bottomBatInclude.cartCountTv.setVisibility(View.GONE)
        } else {
            binding.bottomBatInclude.cartCountTv.setText(Constant.cardCount)
            binding.bottomBatInclude.cartCountTv.setVisibility(View.VISIBLE)
        }

        if (Constant.wishListCount.equals(
                "",
                ignoreCase = true
            ) || Constant.wishListCount.equals("0", ignoreCase = true)
        ) {
            binding.bottomBatInclude.wishListCountTv.setVisibility(View.GONE)
        } else {
            binding.bottomBatInclude.wishListCountTv.setText(Constant.wishListCount)
            binding.bottomBatInclude.wishListCountTv.setVisibility(View.VISIBLE)
        }
    }

    override fun onResume() {
        super.onResume()

        if (manageAPICall.equals("", ignoreCase = true)) {
            pageNo = 1
            onBindDetails(false)
        } else {
            manageAPICall = ""
        }
    }

    private fun shimmerShow() {
        if (cardViewAndListViewParttenShow.equals("CardView", ignoreCase = true)) {
            binding.shimmerFirst.imageCardViewShimmer.visibility = View.VISIBLE
        } else {
            binding.shimmerFirst.imageCardViewShimmer.visibility = View.GONE
        }
        // top_layout.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.shimmerViewContainer.visibility = View.VISIBLE
        binding.shimmerViewContainer.startShimmerAnimation()
    }

    private fun shimmerStop() {
        binding.shimmerViewContainer.stopShimmerAnimation()
        // top_layout.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.VISIBLE
        binding.shimmerViewContainer.visibility = View.GONE
    }


    fun onBindDetails(showLoader: Boolean) {
        val uuid = CommonUtility.getAndroidId(context)

        if (Utils.isNetworkAvailable(context)) {

            urlParameter = java.util.HashMap<String, String>()
            urlParameter["sessionId"] = "" + uuid
            urlParameter["limit"] = Constant.lazyLoadingLimit.toString()
            urlParameter["page"] = pageNo.toString()
            urlParameter["isLuxe"] = "0"
            urlParameter["stockNo"] = Constant.stockIdGemstone
            urlParameter["keyWord"] = Constant.searchKeyword
            urlParameter["category"] = searchType
            urlParameter["stone"] = selectedshapeTypesItems
            urlParameter["shape"] = Constant.selectedGemShapes
            urlParameter["cuttingStyle"] = Constant.selectedCuttingAttribute
            urlParameter["treatment"] = Constant.selectedGemTreatment
            urlParameter["dimensions"] = ""
            urlParameter["certificate"] = Constant.selectedCertificateItems
            urlParameter["returnable"] = Constant.isReturnable
            urlParameter["color"] = Constant.selectedColorItems
            urlParameter["origin"] = Constant.selectedOriginItems
            urlParameter["weight"] = selectedWeightGem
            urlParameter["weightFrom"] = Constant.weightFirst
            urlParameter["weightTo"] = Constant.weightThird
            urlParameter["price"] = selectedPriceGem
            urlParameter["priceFrom"] = Constant.priceFrm
            urlParameter["priceTo"] = Constant.priceTo
            urlParameter["searchLocation"] = stocklocation

            if (Constant.priceFrm.equals("", ignoreCase = true) && Constant.priceTo.equals(
                    "",
                    ignoreCase = true
                )
            ) {
                urlParameter["currValue"] = ""
            } else {
                if (!Constant.getCurrencyValue.equals("", ignoreCase = true)) {
                    urlParameter["currValue"] = Constant.getCurrencyValue
                } else {
                    urlParameter["currValue"] = "1"
                }
            }
            urlParameter["sortBy"] = Constant.sortingBy
            vollyApiActivity = VollyApiActivity(
                context,
                this,
                urlParameter,
                ApiConstants.GET_GEMSTONES,
                ApiConstants.GET_GEMSTONES_ID,
                showLoader,
                "GET"
            )

            if (pageNo == 1) {
                binding.errorTv.visibility = View.GONE
                shimmerShow()
            }

        } else {
            showToast(ApiConstants.MSG_INTERNETERROR)
            // recyclerNaturalGrownView.visibility = View.GONE
        }
    }



    fun onAttributeCall(showLoader: Boolean) {
        Log.e("BindDetails", "............CALL......................")
        val uuid = CommonUtility.getAndroidId(context)

        if (Utils.isNetworkAvailable(context)) {
            urlParameter = java.util.HashMap<String, String>()

            urlParameter["sessionId"] = "" + uuid
            vollyApiActivity = VollyApiActivity(
                context,
                this,
                urlParameter,
                ApiConstants.GET_ATTRIBUTES,
                ApiConstants.GET_ATTRIBUTES_ID,
                showLoader,
                "GET"
            )
        } else {
            showToast(ApiConstants.MSG_INTERNETERROR)
        }
    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun getSuccessResponce(jsonObject: JSONObject?, service_ID: Int) {
        if (pageNo == 1) {
            shimmerStop()
        } else {
            shimmerStop()
        }

        try {
            Log.e("------Diamond----- : ", "-Gem Search result..-------JSONObject-----**--- : $jsonObject")
            val jsonObjectData = jsonObject!!
            val message = jsonObjectData.optString("msg")
            when (service_ID) {
                ApiConstants.GET_GEMSTONES_ID -> {

                    if (jsonObjectData.optString("status").equals("1", ignoreCase = true)) {
                        val details = jsonObjectData.optJSONArray("details")
                        val avgDiamondPrice = jsonObjectData.optInt("check_other_locations")

                        checkOtherLocation = avgDiamondPrice
                        Log.e("checkOtherLocation", ",....1272...$checkOtherLocation")

                        if (pageNo == 1) {
                            if (modelArrayList!!.isNotEmpty()) {
                                modelArrayList!!.clear()
                            }
                        }
                        scrollListener.loading =
                            !(details == null || details.length() < Constant.lazyLoadingLimit)

                        if (pageNo > 1) {
                            if (modelArrayList!!.isNotEmpty()) {

                                if (cardViewAndListViewParttenShow.equals(
                                        "CardView",
                                        ignoreCase = true
                                    )
                                ) {
                                    gemstoneSearchResultAdapter?.notifyDataSetChanged()
                                } else {
                                    gemstoneSearchResultAdapter?.notifyDataSetChanged()
                                }
                            }
                        }

                        details?.let {
                            for (i in 0 until it.length()) {
                                val objectCodes = it.getJSONObject(i)
                                Log.e(
                                    "certificate_no", "...........${
                                        objectCodes.optString(
                                            "certificate_no"
                                        )
                                    }"
                                )
                                val model = SearchGemstoneTypeModel(
                                    stock_id = CommonUtility.checkString(objectCodes.optString("stock_id")),
                                    item_name = CommonUtility.checkString(objectCodes.optString("item_name")),
                                    category = CommonUtility.checkString(objectCodes.optString("category")),
                                    supplier_id = CommonUtility.checkString(objectCodes.optString("supplier_id")),
                                    cut_grade = CommonUtility.checkString(objectCodes.optString("cut_grade")),
                                    certificate_name = CommonUtility.checkString(
                                        objectCodes.optString(
                                            "certificate_name"
                                        )
                                    ),
                                    certificate_no = CommonUtility.checkString(
                                        objectCodes.optString(
                                            "certificate_no"
                                        )
                                    ),
                                    stock_no = CommonUtility.checkString(objectCodes.optString("stock_no")),
                                    growth_type = CommonUtility.checkString(objectCodes.optString("growth_type")),
                                    polish = CommonUtility.checkString(objectCodes.optString("polish")),
                                    symmetry = CommonUtility.checkString(objectCodes.optString("symmetry")),
                                    measurement = CommonUtility.checkString(objectCodes.optString("measurement")),
                                    fluorescence_intensity = CommonUtility.checkString(
                                        objectCodes.optString(
                                            "fluorescence_intensity"
                                        )
                                    ),
                                    carat = CommonUtility.checkString(objectCodes.optString("carat")),
                                    dia_wt = CommonUtility.checkString(objectCodes.optString("dia_wt")),
                                    color = CommonUtility.checkString(objectCodes.optString("color")),
                                    clarity = CommonUtility.checkString(objectCodes.optString("clarity")),
                                    shape = CommonUtility.checkString(objectCodes.optString("shape")),
                                    shade = CommonUtility.checkString(objectCodes.optString("shade")),
                                    table_perc = CommonUtility.checkString(objectCodes.optString("table_perc")),
                                    depth_perc = CommonUtility.checkString(objectCodes.optString("depth_perc")),
                                    luster = CommonUtility.checkString(objectCodes.optString("luster")),
                                    eye_clean = CommonUtility.checkString(objectCodes.optString("eye_clean")),
                                    treatment = CommonUtility.checkString(objectCodes.optString("treatment")),
                                    inscription = CommonUtility.checkString(objectCodes.optString("inscription")),
                                    diamond_image = CommonUtility.checkString(
                                        objectCodes.optString(
                                            "diamond_image"
                                        )
                                    ),
                                    diamond_video = CommonUtility.checkString(
                                        objectCodes.optString(
                                            "diamond_video"
                                        )
                                    ),
                                    discount = CommonUtility.checkString(objectCodes.optString("discount")),
                                    total_gst_perc = CommonUtility.checkString(
                                        objectCodes.optString(
                                            "total_gst_perc"
                                        )
                                    ),
                                    subtotal = CommonUtility.checkString(objectCodes.optString("subtotal")),
                                    total_price = CommonUtility.checkString(objectCodes.optString("total_price")),
                                    is_returnable = CommonUtility.checkString(
                                        objectCodes.optString(
                                            "is_returnable"
                                        )
                                    ),
                                    dxe_prefered = CommonUtility.checkString(objectCodes.optString("dxe_prefered")),
                                    status = CommonUtility.checkString(objectCodes.optString("status")),
                                    is_available_for_sale = CommonUtility.checkString(
                                        objectCodes.optString(
                                            "is_available_for_sale"
                                        )
                                    ),
                                    is_cart = CommonUtility.checkString(objectCodes.optString("is_cart")),
                                    is_wishlist = CommonUtility.checkString(objectCodes.optString("is_wishlist")),
                                    on_hold = CommonUtility.checkString(objectCodes.optString("on_hold")),
                                    tax = CommonUtility.checkString(objectCodes.optString("tax")),
                                    price_per_ct = CommonUtility.checkString(objectCodes.optString("price_per_ct")),
                                    shipping_charge = CommonUtility.checkString(
                                        objectCodes.optString(
                                            "shipping_charge"
                                        )
                                    ),
                                    platform_fee_amt = CommonUtility.checkString(
                                        objectCodes.optString(
                                            "platform_fee_amt"
                                        )
                                    ),
                                    dxe_markup = CommonUtility.checkString(objectCodes.optString("dxe_markup")),
                                    coupon_discount_perc = CommonUtility.checkDouble(
                                        objectCodes.optString(
                                            "coupon_discount_perc"
                                        )
                                    ),
                                    subtotal_after_coupon_discount = CommonUtility.checkString(
                                        objectCodes.optString("subtotal_after_coupon_discount")
                                    ),
                                    currencySymbol = ApiConstants.rupeesIcon,
                                    showingSubTotal = CommonUtility.checkString(
                                        objectCodes.optString(
                                            "subtotal"
                                        )
                                    ),
                                    setVisible = (details.length() - 1 == i)
                                )
                                /*if (details.length() - 1 == i) {
                                    model.setVisible(true)
                                } else {
                                    model.setVisible(false)
                                }*/
                                modelArrayList!!.add(model)
                            }
                        }

                        modelArrayList!!.forEach { model ->
                            val subTotalFormat = CommonUtility.currencyConverter(
                                selectedCurrencyValue,
                                selectedCurrencyCode,
                                model.subtotal
                            )
                            val getCurrencySymbol =
                                CommonUtility.getCurrencySymbol(selectedCurrencyCode)
                            model.showingSubTotal = subTotalFormat
                            model.currencySymbol = getCurrencySymbol
                        }

                        if (details != null) {
                            if (details == null && details.length() < Constant.lazyLoadingLimit) {
                                Log.e("lazyLoadingLimit", ".False....." + details.length())
                                scrollListener.loading = false
                            } else {
                                Log.e("lazyLoadingLimit", ".True.........." + details.length())
                                scrollListener.loading = true
                            }
                        }

                        if (pageNo == 1) {
                            if (cardViewAndListViewParttenShow.equals(
                                    "CardView",
                                    ignoreCase = true
                                )
                            ) {
                                gemstoneSearchResultAdapter = GemstoneSearchResultAdapter(
                                    context!!,
                                    "userRole",
                                    modelArrayList!!,
                                    this
                                )
                                binding.recyclerView.adapter = gemstoneSearchResultAdapter
                            } else {
                                gemstoneSearchResultListAdapter = GemStoneSearchResultListAdapter(
                                    context!!,
                                    "userRole",
                                    modelArrayList!!,
                                    this
                                )
                                binding.recyclerView.setAdapter(gemstoneSearchResultListAdapter)
                            }
                        } else {
                            if (cardViewAndListViewParttenShow.equals(
                                    "CardView",
                                    ignoreCase = true
                                )
                            ) {
                                gemstoneSearchResultAdapter?.notifyDataSetChanged()
                            } else {
                                gemstoneSearchResultListAdapter?.notifyDataSetChanged()
                            }
                        }
                    } else {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }

                }

                ApiConstants.GET_ATTRIBUTES_ID -> {
                    if (jsonObjectData.optString("status").equals("1", ignoreCase = true)) {
                        val details = jsonObjectData.getJSONArray("details")
                        for (i in 0 until details.length()) {
                            attributeDetailsModels.clear()
                            val objectCode = details.getJSONObject(i)

                            val AttribType = objectCode.getString("AttribType")

                            val attribDetails = objectCode.getJSONArray("AttribDetails")

                            for (j in 0 until attribDetails.length()) {
                                val innerObjectCodes = attribDetails.getJSONObject(j)

                                val model = AttributeDetailsModel()

                                model.attribId =
                                    CommonUtility.checkString(innerObjectCodes.optString("AttribId"))
                                model.attribTypeId =
                                    CommonUtility.checkString(innerObjectCodes.optString("AttribTypeId"))
                                model.attribType =
                                    CommonUtility.checkString(innerObjectCodes.optString("AttribType"))
                                model.attribCode =
                                    CommonUtility.checkString(innerObjectCodes.optString("AttribCode"))
                                model.sortOrder =
                                    CommonUtility.checkString(innerObjectCodes.optString("SortOrder"))
                                model.displayAttr =
                                    CommonUtility.checkString(innerObjectCodes.optString("DisplayAttr"))
                                // model.setFirstPosition(getFirstPosition(model, j));
                                model.isSelected = false

                                attributeDetailsModels.add(model)
                            }
                        }

                        // spinnerAttributes.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.purple_light), PorterDuff.Mode.SRC_ATOP);
                        attributeNames = ArrayList<AttributeDetailsModel>(attributeDetailsModels)

                        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                            this,
                            android.R.layout.simple_spinner_item,
                            convertAttributeListToNames(attributeNames)
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinnerAttributes.adapter=adapter
                        /*binding.spinnerAttributes.setOnClickListener {
                            Log.e("Click...","745..####....33323232......###########........")
                            binding.dimOverlay.visibility = View.VISIBLE
                            showCustomDropdown(it, attributeNames)
                        }*/
                        binding.spinnerAttributes.setOnTouchListener { v, event ->
                            Log.e("Click...", "745..####..........###########........")
                            if (event.action == MotionEvent.ACTION_UP) {
                                binding.dimOverlay.visibility = View.VISIBLE
                                showCustomDropdown(v, attributeNames)
                                true
                            } else {
                                false
                            }
                        }
                    }
                }

                ApiConstants.ADD_TO_CART_ID -> {
                    if (jsonObjectData.optString("status").equals("1", ignoreCase = true)) {
                        modelArrayList!![lastPosition].is_cart = "1"

                        Log.e(
                            "cardViewAndListViewParttenShow",
                            "654....${cardViewAndListViewParttenShow}"
                        )
                        if (cardViewAndListViewParttenShow.equals("ListView", ignoreCase = true)) {
                            gemstoneSearchResultListAdapter!!.notifyDataSetChanged()
                        } else {
                            gemstoneSearchResultAdapter!!.notifyDataSetChanged()
                        }

                        val jObjDetails = jsonObjectData.optJSONObject("details")
                        Constant.cardCount =
                            CommonUtility.checkString(jObjDetails.optString("cart_count"))
                        showCardCount()
                    } else if (jsonObjectData.optString("status").equals("0", ignoreCase = true)) {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show()
                    } else if (jsonObjectData.optString("status").equals("4", ignoreCase = true)) {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show()
                    }
                }

                ApiConstants.REMOVE_FROM_WISHLIST_ID -> {
                    if (jsonObjectData.optString("status").equals("1", ignoreCase = true)) {
                        modelArrayList!![lastPosition].is_wishlist = "0"
                        if (cardViewAndListViewParttenShow.equals("ListView", ignoreCase = true)) {
                            gemstoneSearchResultListAdapter!!.notifyDataSetChanged()
                        } else {
                            gemstoneSearchResultAdapter!!.notifyDataSetChanged()
                        }
                        val jObjDetails = jsonObjectData.optJSONObject("details")
                        Constant.wishListCount =
                            CommonUtility.checkString(jObjDetails.optString("wishlist_count"))
                        showCardCount()
                    } else if (jsonObjectData.optString("status").equals("0", ignoreCase = true)) {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show()
                    } else if (jsonObjectData.optString("status").equals("4", ignoreCase = true)) {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show()
                    }
                }

                ApiConstants.ADD_TO_WISHLIST_ID -> {
                    if (jsonObjectData.optString("status").equals("1", ignoreCase = true)) {
                        modelArrayList!![lastPosition].is_wishlist = "1"
                        if (cardViewAndListViewParttenShow.equals("ListView", ignoreCase = true)) {
                            gemstoneSearchResultListAdapter!!.notifyDataSetChanged()
                        } else {
                            gemstoneSearchResultAdapter!!.notifyDataSetChanged()
                        }

                        val jObjDetails = jsonObjectData.optJSONObject("details")
                        if (jObjDetails != null) {
                            Constant.wishListCount =
                                CommonUtility.checkString(jObjDetails.optString("wishlist_count"))
                        }
                        showCardCount()
                    }
                }
            }
        } catch (e: Exception) {

        } finally {
            if (checkOtherLocation == 1) {
                StockLocationNoDataFound(this);
            } else if (modelArrayList != null && modelArrayList!!.size <= 0) {
                binding.errorTv.setVisibility(View.VISIBLE);
                binding.errorTv.setText("" + ApiConstants.NO_RESULT_FOUND);
                binding.recyclerView.setVisibility(View.GONE);
            } else {
                if (dialogBuilder != null) {
                    if (alertDialog!!.isShowing) {
                        alertDialog!!.dismiss();
                        isDialogVisible = false;
                    }
                }
                binding.errorTv.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
            }
        }

    }

    var dialogBuilder: AlertDialog.Builder? = null
    var alertDialog: AlertDialog? = null
    private var isDialogVisible = false
    fun StockLocationNoDataFound(activity: Activity?) {
        if (isDialogVisible) {
            return
        }

        dialogBuilder = AlertDialog.Builder(activity)
        val inflater = LayoutInflater.from(activity)
        val dialogView = inflater.inflate(R.layout.stock_location_notfound_dialog, null)

        dialogBuilder!!.setView(dialogView)
        alertDialog = dialogBuilder!!.create()

        if (alertDialog!!.window != null) {
            alertDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        alertDialog!!.setCancelable(true)
        alertDialog!!.setCanceledOnTouchOutside(false)
        if(alertDialog!=null )
        {
            if (!alertDialog!!.isShowing)
            {
                alertDialog!!.show()
            }

        }

        isDialogVisible = true

        val okbutton = dialogView.findViewById<LinearLayout>(R.id.ok_button)
        okbutton.setOnClickListener {
            alertDialog!!.dismiss()
            isDialogVisible = false
        }

        val crossImg = dialogView.findViewById<ImageView>(R.id.cross_img)
        crossImg.setOnClickListener {
            alertDialog!!.dismiss()
            isDialogVisible = false
        }
    }

    fun setFirstPositionCountry() {
        if (!selectedCurrencyImage.equals("", ignoreCase = true)) {
            Picasso.with(context)
                .load(selectedCurrencyImage)
                .into(binding.bottomBatInclude.selectedCountryImg)
        } else {
        }

        binding.bottomBatInclude.selectedCountryName.setText(selectedCurrencyCode)
        binding.bottomBatInclude.selectedCountryDesc.setText(selectedCurrencyDesc)
    }

    private fun convertAttributeListToNames(models: List<AttributeDetailsModel>): List<String> {
        val names: MutableList<String> = java.util.ArrayList()
        for (model in models) {
            names.add(model.displayAttr)
        }
        return names
    }

    private fun showCustomDropdown(anchor: View, data: List<AttributeDetailsModel>) {
        val popupWindow = PopupWindow(this)
        val listView = ListView(this)
        val displayNames: MutableList<String> = data.map { it.displayAttr }.toMutableList()

        val paint = Paint().apply {
            textSize = resources.getDimensionPixelSize(android.R.dimen.app_icon_size).toFloat()
        }
        val maxWidth = displayNames.maxOfOrNull { paint.measureText(it).toInt() } ?: 0
        val calculatedWidth = maxWidth.coerceAtMost(550)

        popupWindow.setOnDismissListener {
            binding.dimOverlay.visibility = View.GONE
        }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            displayNames
        )
        listView.adapter = adapter

        popupWindow.contentView = listView
        popupWindow.width = calculatedWidth
        popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
        popupWindow.isFocusable = true
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        popupWindow.showAsDropDown(anchor)

        listView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedModel = data[position]
                Log.e("Dropdown", "Selected item details: ${selectedModel.displayAttr}")
                Log.e("Dropdown", "Selected item details: ${selectedModel.attribCode}")
                binding.spinnerAttributes.setSelection(position)
                selectedPriceGem = ""
                selectedWeightGem = ""
                stocklocation = selectedModel.attribCode
                Log.e("stocklocation", "834...####...$stocklocation")
                onBindDetails(false)
                binding.dimOverlay.visibility = View.GONE
                popupWindow.dismiss()
            }
    }

    /*private fun showCustomDropdown(anchor: View, data: List<AttributeDetailsModel>) {
        val popupWindow = PopupWindow(this)
        val listView = ListView(this)
        val displayNames: MutableList<String> = java.util.ArrayList()
        for (model in data) {
            displayNames.add(model.displayAttr)
        }
        popupWindow.setOnDismissListener {
            // Hide the overlay when the popup is dismissed
            binding.dimOverlay.setVisibility(View.GONE)
        }
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            displayNames
        )
        listView.adapter = adapter

        popupWindow.contentView = listView
        popupWindow.width = anchor.width
        popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
        popupWindow.isFocusable = true
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        popupWindow.showAsDropDown(anchor)
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                val selectedModel =
                    data[position]
                Log.e("Dropdown", "Selected item details: " + selectedModel.displayAttr)
                Log.e("Dropdown", "Selected item details: " + selectedModel.attribCode)
                binding.spinnerAttributes.setSelection(position)
                selectedPriceGem = ""
                selectedWeightGem = ""
                stocklocation = selectedModel.attribCode
                Log.e("stocklocation", "834...####...$stocklocation")
                onBindDetails(false)
                binding.dimOverlay.setVisibility(View.GONE)
                popupWindow.dismiss()
            }
    }*/

    override fun getErrorResponce(error: String?, service_ID: Int) {
        if (pageNo == 1) {
            shimmerStop()
        } else {
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.back_img -> {
                finish()
            }

            R.id.home_rel -> {
                Constant.manageFragmentCalling = ApiConstants.HOME_FRAGMENT
                intent = Intent(activity, HomeScreenActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }

            R.id.wishlist_rel -> {
                Constant.manageFragmentCalling = ApiConstants.WISHLIST_FRAGMENT
                intent = Intent(activity, HomeScreenActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }

            R.id.cart_rel -> {
                Constant.manageFragmentCalling = ApiConstants.CART_FRAGMENT
                intent = Intent(activity, HomeScreenActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }

            R.id.account_tv -> {
                user_login = CommonUtility.getGlobalString(activity, "user_login")
                if (user_login.equals("yes", ignoreCase = true)) {
                    Constant.manageFragmentCalling = ApiConstants.ACCOUNT_FRAGMENT
                    intent = Intent(activity, HomeScreenActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                } else {
                    intent = Intent(context, LoginScreenActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
            }

            R.id.account_img -> {
                user_login = CommonUtility.getGlobalString(activity, "user_login")
                if (user_login.equals("yes", ignoreCase = true)) {
                    Constant.manageFragmentCalling = ApiConstants.ACCOUNT_FRAGMENT
                    intent = Intent(activity, HomeScreenActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                } else {
                    intent = Intent(context, LoginScreenActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
            }

            R.id.account_rel -> {
                user_login = CommonUtility.getGlobalString(activity, "user_login")
                if (user_login.equals("yes", ignoreCase = true)) {
                    Constant.manageFragmentCalling = ApiConstants.ACCOUNT_FRAGMENT
                    intent = Intent(activity, HomeScreenActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                } else {
                    intent = Intent(context, LoginScreenActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
            }

            R.id.card_view_img -> {
                cardViewAndListViewParttenShow = "CardView"

                binding.cardViewImg.setColorFilter(
                    ContextCompat.getColor(
                        context!!,
                        R.color.purple_light
                    )
                )
                binding.listViewImg.setColorFilter(ContextCompat.getColor(context!!, R.color.black))

                cardViewFormatAdapterSet()
            }

            R.id.list_view_img -> {
                cardViewAndListViewParttenShow = "ListView"

                binding.cardViewImg.setColorFilter(ContextCompat.getColor(context!!, R.color.black))
                binding.listViewImg.setColorFilter(
                    ContextCompat.getColor(
                        context!!,
                        R.color.purple_light
                    )
                )
                listViewFormatAdapterSet()
            }

            R.id.sort_img -> {
                initiatePopupWindow()
            }

            R.id.natural_tv -> {
                naturalCardTabColorSet()
            }

            R.id.grown_tv -> {
                labGrownCardTabColorSet()
            }

            R.id.dxe_calc_rev -> {
                Constant.manageFragmentCalling = ApiConstants.DXE_CALC
                val intent1 = Intent(context, CalculatorActivity::class.java)
                startActivity(intent1)
                overridePendingTransition(0, 0)
            }
            R.id.stock_lv -> {
                // binding.dimOverlay.visibility = View.VISIBLE
                // binding.spinnerAttributes.performClick()
                binding.dimOverlay.visibility = View.VISIBLE
                showCustomDropdown(binding.spinnerAttributes, attributeNames)
            }
            R.id.dropdown_ -> {
                // binding.dimOverlay.visibility = View.VISIBLE
                // binding.spinnerAttributes.performClick()
                binding.dimOverlay.visibility = View.VISIBLE
                showCustomDropdown(binding.spinnerAttributes, attributeNames)
            }

            R.id.search_circle_card -> {
                binding.translucentBackground.setVisibility(View.VISIBLE)

                bottomBarClickableFalse()

                binding.bottomBatInclude.bottomSearchIcon.setImageResource(R.drawable.cross)
                binding.bottomBatInclude.bottomSearchIcon.setColorFilter(
                    ContextCompat.getColor(
                        context!!,
                        R.color.white
                    )
                )
                binding.bottomBatInclude.bottomSearchIcon.animate().rotation(45f).setDuration(300)
                    .start()

                binding.bottomBatInclude.cardPopup1Country.visibility=View.VISIBLE // Option Popup Show
                binding.bottomBatInclude.curveRelCountry.visibility=View.VISIBLE // Bottom Cut Center Position Layout show
                binding.bottomBatInclude.searchCircleCard.visibility=View.GONE // Bottom Plus Icon Hide
                binding.bottomBatInclude.searchCircleCard1CrossCountry.visibility=View.VISIBLE // Bottom Cross Icon show
                binding.bottomBatInclude.showPopupRelCountry.visibility=View.VISIBLE // Bottom Cross Icon show
                binding.bottomBatInclude.cardPopup1Country.visibility=View.VISIBLE // Selected Country View Layout
                val popup1Animation: Animation = createPopupAnimation()
                binding.bottomBatInclude.cardPopup1Country.startAnimation(popup1Animation)

            }

            R.id.lin_enquiry -> {
                val intentcall = Intent(Intent.ACTION_DIAL)
                intentcall.setData(Uri.parse("tel:9892003399"))
                startActivity(intentcall)
            }

            R.id.country_layout_rel -> {

                //card_popup_list_country.setVisibility(View.GONE);
                if (binding.bottomBatInclude.recyclerViewCountryList.visibility == View.VISIBLE) {
                    binding.bottomBatInclude.recyclerViewCountryList.visibility=View.GONE
                    binding.bottomBatInclude.countryDropImg.setImageResource(R.drawable.down)
                    setBottomCountryPopupMargin()
                } else {
                    binding.bottomBatInclude.countryDropImg.setImageResource(R.drawable.up)
                    binding.bottomBatInclude.recyclerViewCountryList.visibility=View.VISIBLE
                    setBottomCountryPopupListMargin()
                }
            }

            R.id.search_circle_card1_cross_country -> {

                hideCurrencyListLayout()
            }

            R.id.filter_type_lin -> {
                if (isArrowDown) {
                    binding.arrowImg.setImageResource(R.drawable.drop_down)
                    binding.nestedView.visibility=View.VISIBLE
                } else {
                    binding.arrowImg.setImageResource(R.drawable.drop_up)
                    binding.nestedView.visibility=View.GONE
                }
                isArrowDown = !isArrowDown
            }
        }
    }

    fun updateLocalCurrencyList() {
        // Clear the local currency list to remove any previously added currencies
        localCurrencyArrayList.clear()

        //Log.e("------selectedCurrencyCode : ", selectedCurrencyCode.toString());

        // Iterate through the list of all currencies
        for (i in Constant.currencyArrayList.indices) {
            // Check if the current currency is not the selected currency
            if (!Constant.currencyArrayList[i].currency.equals(
                    selectedCurrencyCode,
                    ignoreCase = true
                )
            ) {
                // Add the currency to the local currency list
                localCurrencyArrayList.add(Constant.currencyArrayList[i])
                // Log.e("------selectedCurrencyCode----IF : ", Constant.currencyArrayList.get(i).getCurrency().toString());
            } else {
                // Log.e("------selectedCurrencyCode----Else : ", Constant.currencyArrayList.get(i).getCurrency().toString());
            }
        }

        countryListAdapter = CurrencyListAdapter(localCurrencyArrayList, context, this)
        binding.bottomBatInclude.recyclerViewCountryList.setAdapter(countryListAdapter)
    }

    fun hideCurrencyListLayout() {
        binding.bottomBatInclude.bottomSearchIcon.setImageResource(R.drawable.plus)
        binding.bottomBatInclude.bottomSearchIcon.animate().rotation(0f).setDuration(300).start()

        setBottomCountryPopupMargin()

        binding.translucentBackground.visibility=View.GONE
        binding.bottomBatInclude.recyclerViewCountryList.visibility=View.GONE

        bottomBarClickableTrue()

        // Hide popups
        binding.bottomBatInclude.cardPopup1Country.visibility=View.GONE
        binding.bottomBatInclude.curveRelCountry.visibility=View.GONE
        binding.bottomBatInclude.searchCircleCard1CrossCountry.visibility=View.GONE
        binding.bottomBatInclude.searchCircleCard.visibility=View.VISIBLE
        binding.bottomBatInclude.showPopupRelCountry.visibility=View.GONE
    }

    private fun bottomBarClickableTrue() {
        binding.bottomBatInclude.homeRel.isEnabled=true
        binding.bottomBatInclude.categoryRel.isEnabled=true
        binding.bottomBatInclude.wishlistRel.isEnabled=true
        binding.bottomBatInclude.cartRel.isEnabled=true
        binding.bottomBatInclude.accountRel.isEnabled=true
        binding.bottomBatInclude.homeRel.isClickable = true
        binding.bottomBatInclude.categoryRel.isClickable = true
        binding.bottomBatInclude.wishlistRel.isClickable = true
        binding.bottomBatInclude.cartRel.isClickable = true
        binding.bottomBatInclude.accountRel.isClickable = true
    }

    private fun setBottomCountryPopupMargin() {
        // search_circle_card1_cross_country Icon
        val layoutParams =
            binding.bottomBatInclude.searchCircleCard1CrossCountry.layoutParams as RelativeLayout.LayoutParams
        val marginBottomInPx =
            resources.getDimensionPixelSize(R.dimen.dimen_8)
        layoutParams.bottomMargin = marginBottomInPx

        // curve_rel_country Layout
        val layoutParams1 =
            binding.bottomBatInclude.curveRelCountry.layoutParams as RelativeLayout.LayoutParams
        val marginBottomInPx1 =
            resources.getDimensionPixelSize(R.dimen.dimen_3)
        layoutParams1.bottomMargin = marginBottomInPx1

        // show_popup_rel_country Layout
        val layoutParams2 =
            binding.bottomBatInclude.showPopupRelCountry.layoutParams as RelativeLayout.LayoutParams
        val marginBottomInPx2 =
            resources.getDimensionPixelSize(R.dimen.dimen_5)
        layoutParams2.bottomMargin = marginBottomInPx2

        // lin_enquiry Layout
        val layoutParams4 =
            binding.bottomBatInclude.linEnquiry.layoutParams as RelativeLayout.LayoutParams
        val marginTopInPx3 =
            resources.getDimensionPixelSize(R.dimen.dimen_1)
        layoutParams4.topMargin = marginTopInPx3

        // Set updated layout parameters
        binding.bottomBatInclude.searchCircleCard1CrossCountry.layoutParams = layoutParams
        binding.bottomBatInclude.curveRelCountry.layoutParams = layoutParams1
        binding.bottomBatInclude.showPopupRelCountry.layoutParams = layoutParams2
        binding.bottomBatInclude.linEnquiry.layoutParams = layoutParams4

        expand(binding.bottomBatInclude.showPopupRelCountry, 425)
    }

    // Country List View Popup Margin Set
    private fun setBottomCountryPopupListMargin() {
        // search_circle_card1_cross_country Icon
        val layoutParams =
            binding.bottomBatInclude.searchCircleCard1CrossCountry.layoutParams as RelativeLayout.LayoutParams
        val marginBottomInPx =
            resources.getDimensionPixelSize(R.dimen.dimen_8)
        layoutParams.bottomMargin = marginBottomInPx

        // curve_rel_country Layout
        val layoutParams1 =
            binding.bottomBatInclude.curveRelCountry.layoutParams as RelativeLayout.LayoutParams
        val marginBottomInPx1 =
            resources.getDimensionPixelSize(R.dimen.dimen_3)
        layoutParams1.bottomMargin = marginBottomInPx1

        // show_popup_rel_country Layout
        val layoutParams2 =
            binding.bottomBatInclude.showPopupRelCountry.layoutParams as RelativeLayout.LayoutParams
        val marginBottomInPx2 =
            resources.getDimensionPixelSize(R.dimen.dimen_5)
        layoutParams2.bottomMargin = marginBottomInPx2

        // lin_enquiry Layout
        val layoutParams4 =
            binding.bottomBatInclude.linEnquiry.layoutParams as RelativeLayout.LayoutParams
        val marginTopInPx3 =
            resources.getDimensionPixelSize(R.dimen.dimen_8)
        layoutParams4.topMargin = marginTopInPx3

        binding.bottomBatInclude.searchCircleCard1CrossCountry.layoutParams = layoutParams
        binding.bottomBatInclude.curveRelCountry.layoutParams = layoutParams1

        expand(binding.bottomBatInclude.showPopupRelCountry, 1350)
    }

    private fun expand(v: View, newHeight: Int) {
        val currentHeight = v.measuredHeight
        val heightDifference = newHeight - currentHeight

        val animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                v.layoutParams.height =
                    currentHeight + (heightDifference * interpolatedTime).toInt()
                v.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        animation.duration = 300L
        v.startAnimation(animation)
    }


    private fun createPopupAnimation(): Animation {
        val translateAnimation: Animation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 1f,
            Animation.RELATIVE_TO_SELF, 0f
        )
        translateAnimation.duration = 300

        val alphaAnimation: Animation = AlphaAnimation(0f, 1f)
        alphaAnimation.duration = 300

        val animationSet: Animation = AnimationSet(true)
        (animationSet as AnimationSet).addAnimation(translateAnimation)
        animationSet.addAnimation(alphaAnimation)

        return animationSet
    }

    private fun bottomBarClickableFalse() {
        binding.bottomBatInclude.homeRel.isEnabled=false
        binding.bottomBatInclude.categoryRel.isEnabled = false
        binding.bottomBatInclude.wishlistRel.isEnabled = false
        binding.bottomBatInclude.cartRel.isEnabled = false
        binding.bottomBatInclude.accountRel.isEnabled = false
        binding.bottomBatInclude.homeRel.isClickable = false
        binding.bottomBatInclude.categoryRel.isClickable = false
        binding.bottomBatInclude.wishlistRel.isClickable = false
        binding.bottomBatInclude.cartRel.isClickable = false
        binding.bottomBatInclude.accountRel.isClickable = false
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    fun labGrownCardTabColorSet() {
        binding.naturalTv.setBackgroundResource(R.drawable.border_dark_purple)
        binding.grownTv.setBackgroundResource(R.drawable.background_gradient)

        binding.naturalTv.setTextColor(ContextCompat.getColor(context!!, R.color.purple_light))
        binding.grownTv.setTextColor(ContextCompat.getColor(context!!, R.color.white))

        binding.cardViewNatural.cardElevation=0f
        binding.cardViewNatural.cardElevation=0f
        binding.cardViewNatural.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                R.color.bg_color
            )
        )
        binding.cardViewNatural.setCardBackgroundColor(
            ContextCompat.getColor(
                context!!,
                R.color.bg_color
            )
        )
        binding.cardViewNatural.outlineAmbientShadowColor = ContextCompat.getColor(
            context!!,
            R.color.bg_color
        )
        binding.cardViewNatural.outlineSpotShadowColor = ContextCompat.getColor(
            context!!,
            R.color.bg_color
        )

        binding.cardViewGrown.cardElevation = 37f
        binding.cardViewGrown.cardElevation = 18f
        binding.cardViewGrown.outlineAmbientShadowColor = ContextCompat.getColor(
            context!!,
            R.color.purple_gradient_bottom
        )
        binding.cardViewGrown.outlineSpotShadowColor = ContextCompat.getColor(
            context!!,
            R.color.purple_gradient_bottom
        )

        getCurrencyData()
        Constant.searchType = ApiConstants.LAB_GROWN
        pageNo = 1
        onBindDetails(true)
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    fun naturalCardTabColorSet() {
        binding.naturalTv.setBackgroundResource(R.drawable.background_gradient)
        binding.grownTv.setBackgroundResource(R.drawable.border_dark_purple)

        binding.naturalTv.setTextColor(ContextCompat.getColor(context!!, R.color.white))
        binding.grownTv.setTextColor(ContextCompat.getColor(context!!, R.color.purple_light))

        binding.cardViewNatural.cardElevation = 37f
        binding.cardViewNatural.cardElevation = 18f
        binding.cardViewNatural.outlineAmbientShadowColor = ContextCompat.getColor(
            context!!,
            R.color.purple_gradient_bottom
        )
        binding.cardViewNatural.outlineSpotShadowColor = ContextCompat.getColor(
            context!!,
            R.color.purple_gradient_bottom
        )

        binding.cardViewGrown.cardElevation = 0f
        binding.cardViewGrown.cardElevation = 0f
        binding.cardViewGrown.setCardBackgroundColor(
            ContextCompat.getColor(
                context!!,
                R.color.bg_color
            )
        )
        binding.cardViewGrown.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                R.color.bg_color
            )
        )
        binding.cardViewGrown.outlineAmbientShadowColor = ContextCompat.getColor(
            context!!,
            R.color.bg_color
        )
        binding.cardViewGrown.outlineSpotShadowColor = ContextCompat.getColor(
            context!!,
            R.color.bg_color
        )

        getCurrencyData()
        searchType = ApiConstants.NATURAL
        pageNo = 1
        onBindDetails(true)
    }

    private var mDropdown: PopupWindow? = null
    var mInflater: LayoutInflater? = null
    private fun initiatePopupWindow(): PopupWindow? {
        return try {
            mDropdown?.let {
                if (it.isShowing) {
                    it.dismiss()
                }
            }

            val inflater = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layout = inflater.inflate(R.layout.custom_menu_sorting, null)

            val recentlyAdded = layout.findViewById<TextView>(R.id.recently_added)
            val priceLowHigh = layout.findViewById<TextView>(R.id.price_low_high)
            val priceHighLow = layout.findViewById<TextView>(R.id.price_high_low)
            val sizeLowHigh = layout.findViewById<TextView>(R.id.size_low_high)
            val sizeHighLow = layout.findViewById<TextView>(R.id.size_high_low)

            recentlyAdded.text = getString(R.string.recently_added)
            priceLowHigh.text = getString(R.string.price_low_high)
            priceHighLow.text = getString(R.string.price_high_low)
            sizeLowHigh.text = getString(R.string.size_low_high)
            sizeHighLow.text = getString(R.string.size_high_low)

            when (Constant.sortingBy) {
                "RecentlyAdd" -> {
                    recentlyAdded.setBackgroundResource(R.drawable.background_sorting_selected)
                    recentlyAdded.setTextColor(ContextCompat.getColor(context!!, R.color.black))
                }

                "PriceLow" -> {
                    priceLowHigh.setBackgroundResource(R.drawable.background_sorting_selected)
                    priceLowHigh.setTextColor(ContextCompat.getColor(context!!, R.color.black))
                }

                "PriceHigh" -> {
                    priceHighLow.setBackgroundResource(R.drawable.background_sorting_selected)
                    priceHighLow.setTextColor(ContextCompat.getColor(context!!, R.color.black))
                }

                "SizeLow" -> {
                    sizeLowHigh.setBackgroundResource(R.drawable.background_sorting_selected)
                    sizeLowHigh.setTextColor(ContextCompat.getColor(context!!, R.color.black))
                }

                "SizeHigh" -> {
                    sizeHighLow.setBackgroundResource(R.drawable.background_sorting_selected)
                    sizeHighLow.setTextColor(ContextCompat.getColor(context!!, R.color.black))
                }
            }

            layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            mDropdown = PopupWindow(
                layout,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                true
            )

            binding.sortImg.let {
                mDropdown?.showAsDropDown(it, 5, -100)
            } ?: run {
                Log.e("PopupWindow", "sortImg is null")
            }

            recentlyAdded.setOnClickListener {
                handleSortingSelection("RecentlyAdd")
            }

            priceLowHigh.setOnClickListener {
                handleSortingSelection("PriceLow")
            }

            priceHighLow.setOnClickListener {
                handleSortingSelection("PriceHigh")
            }

            sizeLowHigh.setOnClickListener {
                handleSortingSelection("SizeLow")
            }

            sizeHighLow.setOnClickListener {
                handleSortingSelection("SizeHigh")
            }

            mDropdown
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun handleSortingSelection(sortingBy: String) {
        Constant.sortingBy = sortingBy
        if (mDropdown?.isShowing == true) {
            mDropdown?.dismiss()
        } else {
            Log.e("PopupWindow", "mDropdown is null or not showing")
        }
        apiCallForSortingValue()
    }

    private fun apiCallForSortingValue() {
        pageNo = 1
        onBindDetails(true)
    }


    private fun listViewFormatAdapterSet() {
        gemstoneSearchResultListAdapter =
            GemStoneSearchResultListAdapter(context!!, "userRole", modelArrayList!!, this)
        binding.recyclerView.setAdapter(gemstoneSearchResultListAdapter)
    }

    private  fun cardViewFormatAdapterSet() {
        gemstoneSearchResultAdapter =
            GemstoneSearchResultAdapter(context!!, "userRole", modelArrayList!!, this)
        binding.recyclerView.adapter = gemstoneSearchResultAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun itemClick(position: Int, action: String?) {
        Log.e("action", ".333..........$action")

        when (action?.lowercase()) {


            "searchdiamonddetails" -> {
                binding.bottomBatInclude.bottomSearchIcon.setImageResource(R.drawable.plus)
                binding.bottomBatInclude.bottomSearchIcon.animate().rotation(0f).setDuration(300)
                    .start()

                binding.bottomBatInclude.showPopupRelCountry.visibility=View.GONE
                binding.bottomBatInclude.searchCircleCard1CrossCountry.visibility=View.GONE
                binding.bottomBatInclude.searchCircleRel.visibility=View.VISIBLE
                binding.bottomBatInclude.recyclerViewCountryList.visibility=View.GONE
                binding.bottomBatInclude.cardPopup1Country.visibility=View.GONE

                setBottomCountryPopupMargin()

                val model: SearchGemstoneTypeModel = modelArrayList!![position]

                Log.e("----getCertificate_no--#################------- : ", model.certificate_no)
                /*val firstCertificate = model.certificate_no.split(", ").firstOrNull()
                Log.e("firstCertificate : ",""+ firstCertificate);*/
                Constant.manageClickEventForRedirection = ""
                CommonUtility.setGlobalString(context, "certificate_number", model.certificate_no)
                val intent = Intent(activity, DiamondDetailsActivity::class.java)
                if (searchType == "dxeluxe") {
                    intent.putExtra("intentvalue", "dxeluxe")
                }
                intent.putExtra("activityvalue", "gemstone")
                startActivity(intent)
                overridePendingTransition(0, 0)
            }

            "countrytype" -> {

                val model = localCurrencyArrayList[position]
                val resultModel = modelArrayList?.get(position)

                if (!model.image.isNullOrEmpty()) {
                    Picasso.with(context)
                        .load(model.image)
                        .into(binding.bottomBatInclude.selectedCountryImg)
                }

                binding.bottomBatInclude.selectedCountryName.text = model.currency
                binding.bottomBatInclude.selectedCountryDesc.text = model.desc

                Constant.callHomeScreenOnResume =
                    "yes"
                Constant.callForBackScreenForUpdateCurrencySymbol =
                    "yes"

                CommonUtility.setGlobalString(context, "selected_currency_value", model.value)
                CommonUtility.setGlobalString(context, "selected_currency_code", model.currency)
                CommonUtility.setGlobalString(context, "selected_currency_desc", model.desc)
                CommonUtility.setGlobalString(context, "selected_currency_image", model.image)

                binding.translucentBackground.visibility = View.GONE
                bottomBarClickableTrue()

                getCurrencyData()
                Log.e("-------country_image------- :", model.image)
                setSubTotalAccordingCurrencyWise(model.value, model.currency)
                updateLocalCurrencyList()


            }

            "addtofavt" -> {
                lastPosition = position
                manageAPICall = "no"
                onAddToWishlistAPI(false, modelArrayList!![position].certificate_no)
            }

            "removefromfavt" -> {
                lastPosition = position
                manageAPICall = "no"
                onRemoveFromWishlistAPI(false, modelArrayList!![position].certificate_no)
            }

            "addtocart" -> {
                lastPosition = position
                if (modelArrayList!![position].is_cart.equals("1", ignoreCase = true)) {
                    Constant.manageClickEventForRedirection = ""
                    val intent = Intent(activity, MyCardListScreenActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                } else {
                    manageAPICall = "no"
                    onAddToCartAPI(false, modelArrayList!![position].certificate_no)
                }
            }

            "filterappliedtype" -> {

                val allowedFilterTypes = listOf(
                    "SearchFrm",
                    "PriceFrm",
                    "PriceTo",
                    "weightFirst",
                    "weightThird",
                    "stockID",
                    "Returnable",
                    "Shape",
                    "Color",
                    "Certificate",
                    "Origin",
                    "GemShapes",
                    "Treatment",
                    "Item",
                    "CuttingStyle",
                    "SelectPrice",
                    "SelectWeight"
                )

                //add this to price and weight
                //"SelectPrice",
                //                    "SelectWeight"

                if (allowedFilterTypes.contains(Constant.colorTypeFilterApploedArrayList[position].filterType)) {
                    val removeType = Constant.colorTypeFilterApploedArrayList[position].filterType

                    //  Toast.makeText(activity, "removeType : " + removeType, Toast.LENGTH_SHORT).show();
                    Constant.colorTypeFilterApploedArrayList.removeAt(position)
                    colorTypeAppliedFilterDataAdapter.notifyDataSetChanged()

                    Constant.priceFrm = ""
                    Constant.weightFirst = ""
                    Constant.weightThird = ""
                    Constant.stockIdGemstone = ""
                    Constant.selectedshapeTypesItems = ""
                    Constant.selectedColorItems = ""
                    Constant.selectedCertificateItems = ""
                    Constant.selectedOriginItems = ""
                    Constant.selectedGemShapes = ""
                    Constant.selectedGemTreatment = ""
                    Constant.selectedItemsAttribute = ""
                    Constant.selectedCuttingAttribute = ""
                    Constant.searchKeyword = ""
                    Constant.priceTo = ""

                    when (removeType) {
                        "SearchFrm" -> Constant.searchKeyword = ""
                        "SelectPrice" -> Constant.selectedPriceGem = ""
                        "SelectWeight" -> Constant.selectedWeightGem = ""
                        "PriceTo" -> Constant.priceTo = ""
                        "weightFirst" -> Constant.weightFirst = ""
                        "weightThird" -> Constant.weightThird = ""
                        "stockID" -> Constant.stockIdGemstone = ""
                        "Returnable" -> Constant.isReturnable = ""
                        "Shape" -> Constant.selectedshapeTypesItems = ""
                        "Color" -> Constant.selectedColorItems = ""
                        "Certificate" -> Constant.selectedCertificateItems = ""
                        "Origin" -> Constant.selectedOriginItems = ""
                        "GemShapes" -> Constant.selectedGemShapes = ""
                        "Treatment" -> Constant.selectedGemTreatment = ""
                        "Item" -> Constant.selectedItemsAttribute = ""
                        "CuttingStyle" -> Constant.selectedCuttingAttribute = ""
                        "CaratFrom" -> Constant.caratFrm = ""
                        "CaratTo" -> Constant.caratTo = ""
                    }

                    if (Constant.colorTypeFilterApploedArrayList.size > 0) {
                        for (item in Constant.colorTypeFilterApploedArrayList) {
                            when {
                                item.filterType.equals("SelectPrice", ignoreCase = true) -> {
                                    selectedPriceGem = ""
                                }

                                item.filterType.equals("SelectWeight", ignoreCase = true) -> {
                                    selectedWeightGem = ""
                                }


                                item.filterType.equals("SearchFrm", ignoreCase = true) -> {
                                    Constant.searchKeyword = ""
                                }

                                item.filterType.equals("PriceFrm", ignoreCase = true) -> {
                                    Constant.priceFrm = ""
                                }

                                item.filterTypeTo.equals("PriceTo", ignoreCase = true) -> {
                                    Constant.priceTo = ""
                                }

                                item.filterType.equals("Shape", ignoreCase = true) -> {
                                    if (item.isSelected) {
                                        Constant.shapeDiamondValue =
                                            if (Constant.shapeDiamondValue.isEmpty()) {
                                                item.attribCode
                                            } else {
                                                "${Constant.shapeDiamondValue},${item.attribCode}"
                                            }
                                    }
                                }

                                item.filterType.equals("Color", ignoreCase = true) -> {
                                    if (item.isSelected) {
                                        Constant.colorValue = if (Constant.colorValue.isEmpty()) {
                                            item.attribCode
                                        } else {
                                            "${Constant.colorValue},${item.attribCode}"
                                        }
                                    }
                                }

                                item.filterType.equals("FancyColor", ignoreCase = true) -> {
                                    if (item.isSelected) {
                                        Constant.fancyColorValue =
                                            if (Constant.fancyColorValue.isEmpty()) {
                                                item.attribCode
                                            } else {
                                                "${Constant.fancyColorValue},${item.attribCode}"
                                            }
                                    }
                                }

                                item.filterType.equals("Clarity", ignoreCase = true) -> {
                                    if (item.isSelected) {
                                        Constant.clarityValue =
                                            if (Constant.clarityValue.isEmpty()) {
                                                item.attribCode
                                            } else {
                                                "${Constant.clarityValue},${item.attribCode}"
                                            }
                                    }
                                }

                                item.filterType.equals("Certificate", ignoreCase = true) -> {
                                    if (item.isSelected) {
                                        Constant.certificateValue =
                                            if (Constant.certificateValue.isEmpty()) {
                                                item.attribCode
                                            } else {
                                                "${Constant.certificateValue},${item.attribCode}"
                                            }
                                    }
                                }

                                item.filterType.equals("FluoRescence", ignoreCase = true) -> {
                                    if (item.isSelected) {
                                        Constant.fluorescenceValue =
                                            if (Constant.fluorescenceValue.isEmpty()) {
                                                item.attribCode
                                            } else {
                                                "${Constant.fluorescenceValue},${item.attribCode}"
                                            }
                                    }
                                }

                                item.filterType.equals("Cut", ignoreCase = true) -> {
                                    if (item.isSelected) {
                                        Constant.cutValue = if (Constant.cutValue.isEmpty()) {
                                            item.attribCode
                                        } else {
                                            "${Constant.cutValue},${item.attribCode}"
                                        }
                                    }
                                }

                                item.filterType.equals("Polish", ignoreCase = true) -> {
                                    if (item.isSelected) {
                                        Constant.polishValue = if (Constant.polishValue.isEmpty()) {
                                            item.attribCode
                                        } else {
                                            "${Constant.polishValue},${item.attribCode}"
                                        }
                                    }
                                }

                                item.filterType.equals("Symmetry", ignoreCase = true) -> {
                                    if (item.isSelected) {
                                        Constant.symmetryValue =
                                            if (Constant.symmetryValue.isEmpty()) {
                                                item.attribCode
                                            } else {
                                                "${Constant.symmetryValue},${item.attribCode}"
                                            }
                                    }
                                }

                                item.filterType.equals("Technology", ignoreCase = true) -> {
                                    if (item.isSelected) {
                                        Constant.technologyValue =
                                            if (Constant.technologyValue.isEmpty()) {
                                                item.attribCode
                                            } else {
                                                "${Constant.technologyValue},${item.attribCode}"
                                            }
                                    }
                                }

                                item.filterType.equals("Eye", ignoreCase = true) -> {
                                    if (item.isSelected) {
                                        Constant.eyeCleanValue =
                                            if (Constant.eyeCleanValue.isEmpty()) {
                                                item.attribCode
                                            } else {
                                                "${Constant.eyeCleanValue},${item.attribCode}"
                                            }
                                    }
                                }

                                item.filterType.equals("Shade", ignoreCase = true) -> {
                                    if (item.isSelected) {
                                        Constant.shadeValue = if (Constant.shadeValue.isEmpty()) {
                                            item.attribCode
                                        } else {
                                            "${Constant.shadeValue},${item.attribCode}"
                                        }
                                    }
                                }

                                item.filterType.equals("Luster", ignoreCase = true) -> {
                                    if (item.isSelected) {
                                        Constant.lusterValue = if (Constant.lusterValue.isEmpty()) {
                                            item.attribCode
                                        } else {
                                            "${Constant.lusterValue},${item.attribCode}"
                                        }
                                    }
                                }

                                else -> {
                                    when (removeType.lowercase()) {
                                        "fancycolorintensity" -> Constant.fancyColorIntensity = ""
                                        "fancycolorovertone" -> Constant.fancyColorOvertone = ""
                                        "tablefrom" -> Constant.tableFrm = ""
                                        "tableto" -> Constant.tableTo = ""
                                        "depthfromspinner" -> Constant.depthFrmSpinner = ""
                                        "depthtospinner" -> Constant.depthToSpinner = ""
                                    }
                                }
                            }
                        }
                    } else {

                        Constant.priceFrm = ""
                        Constant.weightFirst = ""
                        Constant.weightThird = ""
                        Constant.stockIdGemstone = ""
                        selectedshapeTypesItems = ""
                        Constant.selectedColorItems = ""
                        Constant.selectedCertificateItems = ""
                        Constant.selectedOriginItems = ""
                        Constant.selectedGemShapes = ""
                        Constant.selectedGemTreatment = ""
                        Constant.selectedItemsAttribute = ""
                        Constant.selectedCuttingAttribute = ""
                        Constant.searchKeyword = ""
                        Constant.priceTo = ""
                    }

                    pageNo = 1
                    onBindDetails(true)


                }

            }
        }
    }

    fun onAddToWishlistAPI(showLoader: Boolean, certificateNo: String) {
        val uuid = CommonUtility.getAndroidId(context)
        if (Utils.isNetworkAvailable(context)) {
            urlParameter = java.util.HashMap()

            urlParameter["certificateNo"] = "" + certificateNo
            urlParameter["source"] = ""
            urlParameter["sessionId"] = "" + uuid

            vollyApiActivity = VollyApiActivity(
                context,
                this,
                urlParameter,
                ApiConstants.ADD_TO_WISHLIST,
                ApiConstants.ADD_TO_WISHLIST_ID,
                showLoader,
                "POST"
            )
        } else {
            showToast(ApiConstants.MSG_INTERNETERROR)
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    private fun onRemoveFromWishlistAPI(showLoader: Boolean, certificateNo: String) {
        val uuid = CommonUtility.getAndroidId(context)
        if (Utils.isNetworkAvailable(context)) {
            urlParameter = java.util.HashMap()

            urlParameter["certificateNo"] = "" + certificateNo
            urlParameter["source"] = ""
            urlParameter["sessionId"] = "" + uuid

            vollyApiActivity = VollyApiActivity(
                context,
                this,
                urlParameter,
                ApiConstants.REMOVE_FROM_WISHLIST,
                ApiConstants.REMOVE_FROM_WISHLIST_ID,
                showLoader,
                "POST"
            )
        } else {
            showToast(ApiConstants.MSG_INTERNETERROR)
        }
    }

    private fun onAddToCartAPI(showLoader: Boolean, certificateNo: String) {
        val uuid = CommonUtility.getAndroidId(context)
        if (Utils.isNetworkAvailable(context)) {
            urlParameter = java.util.HashMap()

            urlParameter["certificateNo"] = "" + certificateNo
            urlParameter["sessionId"] = "" + uuid


            vollyApiActivity = VollyApiActivity(
                context,
                this,
                urlParameter,
                ApiConstants.ADD_TO_CART,
                ApiConstants.ADD_TO_CART_ID,
                showLoader,
                "POST"
            )
        } else {
            showToast(ApiConstants.MSG_INTERNETERROR)
            //recyclerNaturalGrownView.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setSubTotalAccordingCurrencyWise(value: String, currencyCode: String) {
        for (i in modelArrayList!!.indices) {
            val subTotalFormat =
                CommonUtility.currencyConverter(value, currencyCode, modelArrayList!![i].subtotal)
            val currencySymbol = CommonUtility.getCurrencySymbol(currencyCode)
            modelArrayList!![i].showingSubTotal = subTotalFormat
            modelArrayList!![i].currencySymbol = currencySymbol
        }

        if (cardViewAndListViewParttenShow.equals("CardView", ignoreCase = true)) {
            gemstoneSearchResultAdapter!!.notifyDataSetChanged()
        } else {
            gemstoneSearchResultListAdapter!!.notifyDataSetChanged()
        }

        currencyListCloseAfterCurrencySelect()
    }

    private fun currencyListCloseAfterCurrencySelect() {
        binding.bottomBatInclude.bottomSearchIcon.setImageResource(R.drawable.plus)
        binding.bottomBatInclude.bottomSearchIcon.animate().rotation(0f).setDuration(300).start()
        binding.bottomBatInclude.showPopupRelCountry.visibility = View.GONE
        binding.bottomBatInclude.searchCircleCard.visibility = View.VISIBLE
        binding.bottomBatInclude.searchCircleCard1CrossCountry.visibility = View.GONE
        binding.bottomBatInclude.recyclerViewCountryList.visibility = View.GONE
        binding.bottomBatInclude.cardPopup1Country.visibility = View.GONE
        setBottomCountryPopupMargin()
    }

    public override fun onPause() {
        binding.shimmerViewContainer.stopShimmerAnimation()
        super.onPause()
    }
}