package com.diamondxe.Activity.Gemstones

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.diamondxe.Activity.HomeScreenActivity
import com.diamondxe.Activity.LoginScreenActivity
import com.diamondxe.Adapter.GemCertificateAdapter
import com.diamondxe.Adapter.GemColorAdapter
import com.diamondxe.Adapter.GemOriginAdapter
import com.diamondxe.Adapter.GemstoneShapesAdapter
import com.diamondxe.Adapter.GemstoneTypeListAdapter
import com.diamondxe.ApiCalling.ApiConstants
import com.diamondxe.ApiCalling.ApiConstants.INDIA_CURRENCY_CODE
import com.diamondxe.Beans.AttributeDetailsModel
import com.diamondxe.Beans.GemstoneAttri.GemColorAttribute
import com.diamondxe.Beans.GemstoneAttri.GemCretificateAttribute
import com.diamondxe.Beans.GemstoneAttri.GemOriginAttribute
import com.diamondxe.Beans.GemstoneAttri.GemShapeAttribute
import com.diamondxe.Beans.GemstoneType
import com.diamondxe.Interface.RecyclerInterface
import com.diamondxe.Network.SuperActivity
import com.diamondxe.R
import com.diamondxe.Utils.CommonUtility
import com.diamondxe.Utils.Constant
import com.diamondxe.Utils.Constant.selectedPriceGem
import com.diamondxe.Utils.Constant.selectedWeightGem
import com.diamondxe.Utils.Constant.selectedshapeTypesItems
import com.diamondxe.Utils.Utils
import com.diamondxe.databinding.ActivityGemstomeSearchBinding
import com.dxe.calc.dashboard.CalculatorActivity
import org.json.JSONObject

class GemstomeSearchActivity : SuperActivity(), RecyclerInterface {
    lateinit var binding: ActivityGemstomeSearchBinding
    val gemstoneshapeImageArrayList = ArrayList<GemstoneType>()
    val gemOriginAttribute = ArrayList<GemOriginAttribute>()
    val gemColorAttribute = ArrayList<GemColorAttribute>()
    val gemCretificateAttribute = ArrayList<GemCretificateAttribute>()
    lateinit var gemstoneAdapter: GemstoneTypeListAdapter
    val items = listOf("Total", "Price/CT")
    val itemsOrigin = listOf(
        "Myanmar (Burma)",
        "Sri Lanka (Ceylon)",
        "Thailand",
        "India",
        "Madagascar",
        "Tanzania",
        "Brazil",
        "Colombia",
        "Zambia",
        "Afghanistan",
        "Australia",
        "United States",
        "Russia",
        "Pakistan",
        "Kenya",
        "Nigeria",
        "Mozambique",
        "Malawi",
        "Ethiopia",
        "Others"
    )
    val itemsColor = listOf(
        "Red",
        "Black",
        "BI Color",
        "Blue",
        "Brown",
        "Green",
        "Yellow",
        "Grey",
        "Purple",
        "White",
        "Pink",
        "Multicolor",
        "Orange"
    )
    val itemsweight = listOf("Carat", "Ratti")

    val itemsCertificate = listOf(
        "ITLGR",
        "AGR",
        "GRS Color",
        "GIA",
        "IGI",
        "JBN",
        "IIGJ",
        "GSI",
        "IGTLJ",
        "GUBELIN",
        "C. DUNAIGRE",
        "OTHERS"
    )
    lateinit var activity: Activity
    lateinit var context: Context
    var searchType: String = ""
    var user_login: String = ""
    private lateinit var gemOriginAdapter: GemstoneShapesAdapter
    private lateinit var gemShapeadapter: GemCertificateAdapter
    private lateinit var gemColorAdapter: GemColorAdapter
    var isReturnableNo: Boolean = false
    var isReturnableYes: Boolean = false
    var selectedCurrencyValue: String = ""
    var selectedCurrencyCode: String = ""
    var selectedCurrencyDesc: String = ""
    var selectedCurrencyImage: String = ""
    var getCurrencySymbol: String = ""
    var WHITE: String = "white"
    val shapseItems = listOf("Oval", "Cushion",
        "Octagonal","Pear","Round","Heart","Rectangle","Mixed Shapes",
        "Fancy","Triangular","Hexagonal","Marquise","Sugarloaf")
    val gemShapeAttribute= ArrayList<GemShapeAttribute>()

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGemstomeSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this.also { activity = it }


        binding.backImg.setOnClickListener(this)
        binding.recyclerTypeView.setOnClickListener(this)
        binding.dropdownPricetypeImg.setOnClickListener(this)
        binding.advanceFiltersTv.setOnClickListener(this)
        binding.naturalTv.setOnClickListener(this)
        binding.grownTv.setOnClickListener(this)
        binding.yesTv.setOnClickListener(this)
        binding.noTv.setOnClickListener(this)
        binding.weightRv.setOnClickListener(this)
        binding.clearAllFilter.setOnClickListener(this)
        binding.weightTypeImg.setOnClickListener(this)
        binding.priceRelative.setOnClickListener(this)
        binding.bottomBatInclude.cartRel.setOnClickListener(this)
        binding.bottomBatInclude.wishlistRel.setOnClickListener(this)
        binding.bottomBatInclude.homeRel.setOnClickListener(this)
        binding.bottomBatInclude.accountTv.setOnClickListener(this)
        binding.bottomBatInclude.accountImg.setOnClickListener(this)
        binding.bottomBatInclude.accountRel.setOnClickListener(this)
        binding.bottomBatInclude.dxeCalcRev.setOnClickListener(this)
        binding.bottomBatInclude.searchCircleRel.setOnClickListener(this)


        Constant.shapsGemarraylist = ArrayList()
        Constant.gemTreatmentAttributeArrayList = ArrayList()
        Constant.gemItemsAttributeArrayList = ArrayList()
        Constant.gemCuttingAttributeArrayList = ArrayList()

        //////////////////////////////  SHAPES  //////////////////////////////
        shapseItems.forEach { shape ->
            gemShapeAttribute.add(GemShapeAttribute(shape, false))
        }
        Constant.shapsGemarraylist= ArrayList(gemShapeAttribute)

        binding.bottomBatInclude.accountTv.setTextColor(
            ContextCompat.getColor(
                context,
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

        getCurrencyData(this)

        /////////////////////////////////////////////////////////////////////////////////////

        itemsOrigin.forEach { shape ->
            gemOriginAttribute.add(GemOriginAttribute(shape, false))
        }
        Constant.gemOriginAttributeArrayList = ArrayList(gemOriginAttribute)

        itemsColor.forEach { shape ->
            gemColorAttribute.add(GemColorAttribute(shape, false))
        }
        Constant.gemColorAttributeArrayList = ArrayList(gemColorAttribute)


        itemsCertificate.forEach { shape ->
            gemCretificateAttribute.add(GemCretificateAttribute(shape, false))
        }
        Constant.gemCretificateAttributeArrayList = ArrayList(gemCretificateAttribute)
        ///////////////////////////////////////////////////////////////////////////////////////

        //Spinner setup
        val adapter = ArrayAdapter(this, R.layout.spinner_item, items)
        binding.spinnerAttributesPrice.adapter = adapter
        binding.spinnerAttributesPrice.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = items[position]
                    Log.e("selectedItem", "//..133...." + selectedItem.lowercase())
                    selectedPriceGem = selectedItem.lowercase()
                    // Toast.makeText(this@GemstomeSearchActivity, "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        val adapterweight = ArrayAdapter(this, R.layout.spinner_item, itemsweight)
        binding.weightSpinner.adapter = adapterweight
        binding.weightSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = itemsweight[position]
                Log.e("itemsweight", "//..133...." + selectedItem.lowercase())
                selectedWeightGem = selectedItem.lowercase()
                // Toast.makeText(this@GemstomeSearchActivity, "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        ////////////////// Origin list //////////////////
        gemOriginAdapter = GemstoneShapesAdapter(
            list = Constant.shapsGemarraylist,
            context = this,
            recyclerInterface = this
        )
        binding.recyclerOrigin.adapter = gemOriginAdapter

        val layoutManagerTechnology = LinearLayoutManager(activity).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        binding.recyclerOrigin.layoutManager = layoutManagerTechnology
        binding.recyclerOrigin.isNestedScrollingEnabled = false


        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!binding.searchEt.text.toString().trim().isEmpty()) {
                    binding.searchDiamondErrorTv.visibility = View.GONE
                    binding.clearSearch.visibility = View.VISIBLE
                } else {
                    binding.clearSearch.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.searchEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else {
                false
            }
        }

        selectedWeightGem = "carat"
        selectedPriceGem = "total"

        Constant.colorType = WHITE
        //Certificate
        gemShapeadapter = GemCertificateAdapter(
            list = Constant.gemCretificateAttributeArrayList,
            context = this,
            recyclerInterface = this
        )

        binding.recyclerCertificateView.adapter = gemShapeadapter

        val layoutManagerCre = LinearLayoutManager(activity).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        binding.recyclerCertificateView.layoutManager = layoutManagerCre
        binding.recyclerCertificateView.isNestedScrollingEnabled = false


        //Color
        gemColorAdapter = GemColorAdapter(
            list = Constant.gemColorAttributeArrayList,
            context = this,
            recyclerInterface = this
        )
        binding.recyclerColor.adapter = gemColorAdapter

        val layoutManagerColor = LinearLayoutManager(activity).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        binding.recyclerColor.layoutManager = layoutManagerColor
        binding.recyclerColor.isNestedScrollingEnabled = false

        getTypesList()
        showCardCount()

        binding.bottomBatInclude.bottomSearchIcon.setBackgroundResource(R.mipmap.bottom_search)
        CommonUtility.startZoomAnimation(binding.bottomBatInclude.bottomSearchIcon)

        /////////////////////////////////
        // Check Diamond Type Search and Select Tab
        if (Constant.searchType.equals(ApiConstants.NATURAL, ignoreCase = true)) {
            Constant.searchType = ApiConstants.NATURAL
            binding.naturalTv.setBackgroundResource(R.drawable.background_gradient)
            binding.grownTv.setBackgroundResource(R.drawable.border_dark_purple)

            binding.naturalTv.setTextColor(ContextCompat.getColor(context, R.color.white))
            binding.grownTv.setTextColor(ContextCompat.getColor(context, R.color.purple_light))

            binding.cardViewNatural.setCardElevation(37f)
            binding.cardViewNatural.setCardElevation(18f)
            binding.cardViewNatural.setOutlineAmbientShadowColor(
                ContextCompat.getColor(
                    context,
                    R.color.purple_gradient_bottom
                )
            )
            binding.cardViewNatural.setOutlineSpotShadowColor(
                ContextCompat.getColor(
                    context,
                    R.color.purple_gradient_bottom
                )
            )

            binding.cardViewGrown.setCardElevation(0f)
            binding.cardViewGrown.setCardElevation(0f)
            binding.cardViewGrown.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.bg_color
                )
            )
            binding.cardViewGrown.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.bg_color
                )
            )
            binding.cardViewGrown.setOutlineAmbientShadowColor(
                ContextCompat.getColor(
                    context,
                    R.color.bg_color
                )
            )
            binding.cardViewGrown.setOutlineSpotShadowColor(
                ContextCompat.getColor(
                    context,
                    R.color.bg_color
                )
            )
        } else if (Constant.searchType.equals(ApiConstants.LAB_GROWN, ignoreCase = true)) {
            Constant.searchType = ApiConstants.LAB_GROWN
            binding.naturalTv.setBackgroundResource(R.drawable.border_dark_purple)
            binding.grownTv.setBackgroundResource(R.drawable.background_gradient)

            binding.naturalTv.setTextColor(ContextCompat.getColor(context, R.color.purple_light))
            binding.grownTv.setTextColor(ContextCompat.getColor(context, R.color.white))


            binding.cardViewNatural.setCardElevation(0f)
            binding.cardViewNatural.setCardElevation(0f)
            binding.cardViewNatural.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.bg_color
                )
            )
            binding.cardViewNatural.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.bg_color
                )
            )
            binding.cardViewNatural.setOutlineAmbientShadowColor(
                ContextCompat.getColor(
                    context,
                    R.color.bg_color
                )
            )
            binding.cardViewNatural.setOutlineSpotShadowColor(
                ContextCompat.getColor(
                    context,
                    R.color.bg_color
                )
            )

            binding.cardViewGrown.setCardElevation(37f)
            binding.cardViewGrown.setCardElevation(18f)
            binding.cardViewGrown.setOutlineAmbientShadowColor(
                ContextCompat.getColor(
                    context,
                    R.color.purple_gradient_bottom
                )
            )
            binding.cardViewGrown.setOutlineSpotShadowColor(
                ContextCompat.getColor(
                    context,
                    R.color.purple_gradient_bottom
                )
            )
        }

        binding.bottomBatInclude.homeImg.setColorFilter(
            ContextCompat.getColor(
                context,
                R.color.grey_light
            )
        )
        binding.bottomBatInclude.categoriesImg.setColorFilter(
            ContextCompat.getColor(
                context,
                R.color.grey_light
            )
        )
        binding.bottomBatInclude.wishImg.setColorFilter(
            ContextCompat.getColor(
                context,
                R.color.grey_light
            )
        )
        binding.bottomBatInclude.cartImg.setColorFilter(
            ContextCompat.getColor(
                context,
                R.color.grey_light
            )
        )
        binding.bottomBatInclude.accountImg.setColorFilter(
            ContextCompat.getColor(
                context,
                R.color.grey_light
            )
        )

        binding.bottomBatInclude.homeTv.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.grey_light
            )
        )
        binding.bottomBatInclude.categoriesTv.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.grey_light
            )
        )
        binding.bottomBatInclude.wishTv.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.grey_light
            )
        )
        binding.bottomBatInclude.cartTv.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.grey_light
            )
        )
        binding.bottomBatInclude.accountTv.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.grey_light
            )
        )

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

    override fun getSuccessResponce(jsonObject: JSONObject?, service_ID: Int) {
    }

    override fun getErrorResponce(error: String?, service_ID: Int) {
    }

    private fun performSearch() {
        val query = binding.searchEt.text.toString().trim()
        if (query.equals("", ignoreCase = true)) {
            binding.searchDiamondErrorTv.visibility = View.VISIBLE
        } else {
            Constant.searchKeyword = binding.searchEt.text.toString().trim()
            Constant.priceFrm = binding.priceFrom.text.toString().trim()
            Constant.priceTo = binding.priceTo.text.toString().trim()
            Constant.weightFirst = binding.weightFirst.text.toString().trim()
            Constant.weightThird = binding.weightThird.text.toString().trim()
            Constant.stockIdGemstone = binding.stockId.text.toString().trim()
            sendFilterValue()
        }
    }


    private fun getTypesList() {
        gemstoneshapeImageArrayList.add(
            GemstoneType(
                image = "",
                attributecode = "All",
                isSelect = false,
                isFirstPosition = true,
                drawable = R.drawable.all_gemstone
            )
        )

        gemstoneshapeImageArrayList.add(
            GemstoneType(
                image = "",
                attributecode = "Yellow Sapphire",
                isSelect = false,
                isFirstPosition = true,
                drawable = R.drawable.yellow_sapphire
            )
        )

        gemstoneshapeImageArrayList.add(
            GemstoneType(
                image = "",
                attributecode = "Blue Sapphire",
                isSelect = false,
                isFirstPosition = true,
                drawable = R.drawable.blue_sapphire
            )
        )


        gemstoneshapeImageArrayList.add(
            GemstoneType(
                image = "",
                attributecode = "Emerald",
                isSelect = false,
                isFirstPosition = true,
                drawable = R.drawable.emerald
            )
        )

        gemstoneshapeImageArrayList.add(
            GemstoneType(
                image = "",
                attributecode = "Ruby",
                isSelect = false,
                isFirstPosition = true,
                drawable = R.drawable.ruby
            )
        )


        gemstoneshapeImageArrayList.add(
            GemstoneType(
                image = "",
                attributecode = "Red Coral",
                isSelect = false,
                isFirstPosition = true,
                drawable = R.drawable.red_coral
            )
        )


        gemstoneshapeImageArrayList.add(
            GemstoneType(
                image = "",
                attributecode = "Pearl",
                isSelect = false,
                isFirstPosition = true,
                drawable = R.drawable.pearl
            )
        )

        gemstoneshapeImageArrayList.add(
            GemstoneType(
                image = "",
                attributecode = "Cats Eye",
                isSelect = false,
                isFirstPosition = true,
                drawable = R.drawable.cats_eye
            )
        )

        gemstoneshapeImageArrayList.add(
            GemstoneType(
                image = "",
                attributecode = "Hessonite",
                isSelect = false,
                isFirstPosition = true,
                drawable = R.drawable.hessonite
            )
        )

        val layoutManagerShapeView = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        binding.recyclerTypeView.layoutManager = layoutManagerShapeView
        binding.recyclerTypeView.isNestedScrollingEnabled = false

        gemstoneAdapter = GemstoneTypeListAdapter(gemstoneshapeImageArrayList, this, this)
        binding.recyclerTypeView.adapter = gemstoneAdapter
    }


    @SuppressLint("NewApi")
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.back_img -> {
                finish()
            }
            R.id.price_relative ->{
                binding.spinnerAttributesPrice.performClick()
            }
            R.id.weight_rv ->{
                binding.weightSpinner.performClick()
            }

            R.id.dropdown_pricetype -> {
                binding.spinnerAttributesPrice.performClick()
            }

            R.id.advance_filters_tv -> {
                intent = Intent(this, AdvanceGemstoneFilterActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }

            R.id.natural_tv -> {
                Constant.searchType = ApiConstants.NATURAL
                binding.titleTv.setText(resources.getString(R.string.naturalDiamond))
                naturalCardTabColorSet()
            }

            R.id.grown_tv -> {
                Constant.searchType = ApiConstants.LAB_GROWN
                binding.titleTv.setText(resources.getString(R.string.labGrownDiamond))
                labGrownCardTabColorSet()
            }

            R.id.dxe_calc_rev -> {
                Constant.manageFragmentCalling = ApiConstants.DXE_CALC
                val intent1 = Intent(context, CalculatorActivity::class.java)
                startActivity(intent1)
                overridePendingTransition(0, 0)
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

            R.id.yes_tv -> {
                isReturnableNo = false
                if (!isReturnableYes) {
                    Constant.isReturnable = "1"
                    binding.yesTv.setBackgroundResource(R.drawable.background_gradient)
                    binding.noTv.setBackgroundResource(R.drawable.background_white)

                    binding.yesTv.elevation = 37f
                    binding.noTv.elevation = 0f

                    binding.yesTv.setTextColor(ContextCompat.getColor(context, R.color.white))
                    binding.noTv.setTextColor(ContextCompat.getColor(context, R.color.black))
                } else {
                    Constant.isReturnable = ""
                    binding.yesTv.setBackgroundResource(R.drawable.background_white)
                    binding.yesTv.elevation = 0f
                    binding.yesTv.setTextColor(ContextCompat.getColor(context, R.color.black))
                }
                isReturnableYes = !isReturnableYes
            }

            R.id.no_tv -> {
                isReturnableYes = false
                if (!isReturnableNo) {
                    Constant.isReturnable = "0"
                    binding.yesTv.setBackgroundResource(R.drawable.background_white)
                    binding.noTv.setBackgroundResource(R.drawable.background_gradient)

                    binding.noTv.elevation = 37f
                    binding.yesTv.elevation = 0f

                    binding.yesTv.setTextColor(ContextCompat.getColor(context, R.color.black))
                    binding.noTv.setTextColor(ContextCompat.getColor(context, R.color.white))
                } else {
                    Constant.isReturnable = ""
                    binding.noTv.setBackgroundResource(R.drawable.background_white)
                    binding.noTv.elevation = 0f
                    binding.noTv.setTextColor(ContextCompat.getColor(context, R.color.black))
                }
                isReturnableNo = !isReturnableNo
            }

            R.id.clear_all_filter -> {
                Utils.hideKeyboard(activity)
                //clearAllClick = "yes"
                clearAllDetails()
            }

            R.id.dropdown_pricetype_img -> {
                binding.spinnerAttributesPrice.performClick()
            }

            R.id.weight_type_img -> {
                binding.weightSpinner.performClick()
            }

            R.id.search_circle_rel -> {

                Constant.searchKeyword = binding.searchEt.text.toString().trim()
                Constant.priceFrm = binding.priceFrom.text.toString().trim()
                Constant.priceTo = binding.priceTo.text.toString().trim()
                Constant.weightFirst = binding.weightFirst.text.toString().trim()
                Constant.weightThird = binding.weightThird.text.toString().trim()
                Constant.stockIdGemstone = binding.stockId.text.toString().trim()

                /*Log.e("searchKeyword", "475......${Constant.searchKeyword}")
                Log.e("priceFrm", "476......${Constant.priceFrm}")
                Log.e("priceTo", "477......${Constant.priceTo}")
                Log.e("weightFirst", "478......${Constant.weightFirst}")
                Log.e("weightThird", "479......${Constant.weightThird}")
                Log.e("stockIdGemstone", "480......${Constant.stockIdGemstone}")*/


                val selectedOriginItems =
                    gemOriginAttribute.filter { it.isSelected }.joinToString(",") { it.name }
                Log.e("selectedOriginItems", "472......${selectedOriginItems}")
                Constant.selectedOriginItems = selectedOriginItems

                val selectedColorItems =
                    gemColorAttribute.filter { it.isSelected }.joinToString(",") { it.name }
                Log.e("selectedColorItems", "474......${selectedColorItems}")
                Constant.selectedColorItems = selectedColorItems

                val selectedCertificateItems =
                    gemCretificateAttribute.filter { it.isSelected }.joinToString(",") { it.name }
                Log.e("selectedCertificateItems", "477......${selectedCertificateItems}")
                Constant.selectedCertificateItems = selectedCertificateItems

                Log.e("GemstoneReturnValue", "482......${Constant.GemstoneReturnValue}")

                val selectedItems = gemstoneshapeImageArrayList
                    .filter { it.isSelect }
                    .joinToString(",") { it.attributecode }

                Log.e("selectedItems  Shape", "...489.....${selectedItems}")

                Log.e("selectedPrice  ", "...489.....${selectedPriceGem}")
                Log.e("selectedWeight  ", "...489.....${selectedWeightGem}")
                selectedshapeTypesItems = selectedItems

                Log.e("searchType", "...499.....${Constant.searchType}")

                Log.e("isReturnable", "...489.....${Constant.isReturnable}")

                //Advance search data
                Log.e("selectedGemShapes", "...495.....${Constant.selectedGemShapes}")
                Log.e("selectedGemTreatment", "...496.....${Constant.selectedGemTreatment}")
                Log.e("selectedItemsAttribute", "...497.....${Constant.selectedItemsAttribute}")
                Log.e("selectedCuttingAttribute", "...498.....${Constant.selectedCuttingAttribute}")
                sendFilterValue()

            }
        }
    }

    fun sendFilterValue() {
        Constant.colorTypeFilterApploedArrayList = java.util.ArrayList()

        if (!binding.searchEt.getText().toString().equals("", ignoreCase = true)) {
            // Create a new instance of AttributeDetailsModel
            val model = AttributeDetailsModel()
            // Set the display attribute using the constant search keyword
            model.displayAttr = Constant.searchKeyword
            // Mark this model as selected
            model.isSelected = true
            // Set the filter type to indicate it came from a search
            model.filterType = "SearchFrm"
            // Add the model to the list of applied color type filters
            Constant.colorTypeFilterApploedArrayList.add(model)
        }

        if (!selectedPriceGem.toString().equals("", ignoreCase = true)) {
            val model = AttributeDetailsModel()
            model.displayAttr = selectedPriceGem
            model.isSelected = true
            model.filterType = "SelectPrice"
            Constant.colorTypeFilterApploedArrayList.add(model)
        }
        if (!selectedWeightGem.toString().equals("", ignoreCase = true)) {
            val model = AttributeDetailsModel()
            model.displayAttr = selectedWeightGem
            model.isSelected = true
            model.filterType = "SelectWeight"
            Constant.colorTypeFilterApploedArrayList.add(model)
        }

        ///////=============================//////////////
        if (!binding.priceTo.getText().toString().equals("", ignoreCase = true)) {
            val model = AttributeDetailsModel()
            // Set the display attribute using the constant Price To keyword
            model.displayAttr = Constant.priceTo
            model.isSelected = true
            // Set the filter type to indicate it came from a Price To
            model.filterType = "PriceTo"
            Constant.colorTypeFilterApploedArrayList.add(model)
        }

        if (!binding.weightFirst.getText().toString().equals("", ignoreCase = true)) {
            val model = AttributeDetailsModel()
            // Set the display attribute using the constant Carat Frm keyword
            model.displayAttr = Constant.weightFirst
            model.isSelected = true
            // Set the filter type to indicate it came from a Carat Frm
            model.filterType = "weightFirst"
            Constant.colorTypeFilterApploedArrayList.add(model)
        }
        if (!binding.weightThird.getText().toString().equals("", ignoreCase = true)) {
            val model = AttributeDetailsModel()
            // Set the display attribute using the constant Carat To keyword
            model.displayAttr = Constant.weightThird
            model.isSelected = true
            // Set the filter type to indicate it came from a Carat To
            model.filterType = "weightThird"
            Constant.colorTypeFilterApploedArrayList.add(model)
        }

        if (!binding.stockId.getText().toString().equals("", ignoreCase = true)) {
            val model = AttributeDetailsModel()
            // Set the display attribute using the constant Carat To keyword
            model.displayAttr = Constant.stockIdGemstone
            model.isSelected = true
            // Set the filter type to indicate it came from a Carat To
            model.filterType = "stockID"
            Constant.colorTypeFilterApploedArrayList.add(model)
        }

        // Check if the isReturnable constant is not empty
        if (!Constant.isReturnable.equals("", ignoreCase = true)) {
            val model = AttributeDetailsModel()
            // Check if the isReturnable value is "1"
            if (Constant.isReturnable.equals("1", ignoreCase = true)) {
                // Set the display attribute to "Yes" if it is returnable
                model.displayAttr = "Yes"
            } else {
                // Set the display attribute to "No" if it is not returnable
                model.displayAttr = "No"
            }
            // Mark this model as selected
            model.isSelected = true
            // Set the filter type to indicate it pertains to returnability
            model.filterType = "Returnable"
            // Add the model to the list of applied color type filters
            Constant.colorTypeFilterApploedArrayList.add(model)
        }
        Constant.sortingBy = "PriceLow"
        // If Diamond Type is Blank Or Diamond Type is Natural is Selected Then Goto If Condition Other Wise Else Condition.


        //check this ======================
        Constant.selectedshapeTypesItems = ""

        for (i in gemstoneshapeImageArrayList.indices) {
            // Check if the current shape image is selected
            if (gemstoneshapeImageArrayList[i].isSelect) {
                // Create a new instance of AttributeDetailsModel to store attribute details
                val model = AttributeDetailsModel()

                // Set the display attribute and attribute code using the selected shape's attributes
                model.displayAttr = gemstoneshapeImageArrayList[i].attributecode
                model.attribCode = gemstoneshapeImageArrayList[i].attributecode

                // Set the filter type to "Shape"
                model.filterType = "Shape"
                model.isSelected = true // Mark the model as selected

                // Add the model to the colorTypeFilterAppliedArrayList
                Constant.colorTypeFilterApploedArrayList.add(model)
            } else {
            }
        }

        if (Constant.colorType.equals(WHITE)) {
            // Initialize the color value to an empty string
            Constant.selectedColorItems = ""

            // Iterate through the list of color types
            for (i in gemColorAttribute.indices) {
                // Check if the current color type is selected
                if (gemColorAttribute[i].isSelected) {
                    // Create a new model to store the selected attribute details
                    val model = AttributeDetailsModel()
                    model.displayAttr =
                        gemColorAttribute[i].name // Set display attribute
                    model.attribCode =
                        gemColorAttribute[i].name// Set attribute code
                    model.filterType = "Color" // Set filter type as "Color"
                    model.isSelected = true // Mark the model as selected
                    // Add the model to the filter applied list
                    Constant.colorTypeFilterApploedArrayList.add(model)
                } else {
                }
            }
            Log.e("colorSelectedValue :", Constant.colorValue.toString())
        }

        Constant.selectedCertificateItems = ""

        for (i in gemCretificateAttribute.indices) {
            // Check if the current certificate type is selected
            if (gemCretificateAttribute.get(i).isSelected) {
                val model = AttributeDetailsModel()
                // Set the display attribute and attribute code from the selected certificate type
                model.displayAttr = gemCretificateAttribute.get(i).name
                model.attribCode = gemCretificateAttribute.get(i).name
                // Specify the filter type as "Certificate"
                model.filterType = "Certificate"
                // Mark the attribute as selected
                model.isSelected = true

                // Update certificateValue: append the selected attribute code
                if (Constant.certificateValue.equals("", ignoreCase = true)) {
                    Constant.certificateValue = gemCretificateAttribute.get(i).name
                } else {
                    Constant.certificateValue =
                        Constant.certificateValue + "," + gemCretificateAttribute.get(i).name
                }
                // Add the model to the colorTypeFilterAppliedArrayList
                Constant.colorTypeFilterApploedArrayList.add(model)
            } else {
            }
        }

        //----------------------------Check diamondQualitySelected Means best. low. medium not blank and makeSelectedValue is selected.

        // Initialize technologyValue as an empty string
        Constant.selectedOriginItems = ""

        // Iterate through the list of technology types
        for (i in gemOriginAttribute.indices) {
            // Check if the current technology type is selected
            if (gemOriginAttribute[i].isSelected) {
                val model = AttributeDetailsModel()
                model.displayAttr =
                    gemOriginAttribute[i].name // Set display attribute
                model.attribCode =
                    gemOriginAttribute[i].name // Set attribute code
                model.filterType = "Origin" // Set filter type to "Technology"
                model.isSelected = true // Mark this model as selected

                // Check if technologyValue is empty
                if (Constant.technologyValue.equals("", ignoreCase = true)) {
                    // If empty, initialize with the current attribute code
                    Constant.technologyValue = gemOriginAttribute[i].name
                } else {
                    // If not empty, append the current attribute code, separated by a comma
                    Constant.technologyValue =
                        Constant.technologyValue + "," + gemOriginAttribute[i].name
                }
                // Add the model to the colorTypeFilterApplied list
                Constant.colorTypeFilterApploedArrayList.add(model)
            } else {
            }
        }
        //////////////////////////--Shapes--/////////////////////////////////////////

        // Initialize technologyValue as an empty string
        Constant.selectedGemShapes = ""

        // Iterate through the list of technology types
        for (i in Constant.shapsGemarraylist.indices) {
            // Check if the current technology type is selected
            if (Constant.shapsGemarraylist[i].isSelected) {
                val model = AttributeDetailsModel()
                model.displayAttr =
                    Constant.shapsGemarraylist[i].name // Set display attribute
                model.attribCode =
                    Constant.shapsGemarraylist[i].name // Set attribute code
                model.filterType = "GemShapes" // Set filter type to "Technology"
                model.isSelected = true // Mark this model as selected

                // Check if technologyValue is empty
                if (Constant.technologyValue.equals("", ignoreCase = true)) {
                    // If empty, initialize with the current attribute code
                    Constant.technologyValue = Constant.shapsGemarraylist[i].name
                } else {
                    // If not empty, append the current attribute code, separated by a comma
                    Constant.technologyValue =
                        Constant.technologyValue + "," + Constant.shapsGemarraylist[i].name
                }
                // Add the model to the colorTypeFilterApplied list
                Constant.colorTypeFilterApploedArrayList.add(model)
            } else {
            }
        }

        //////////////////////////--Treatment--/////////////////////////////////////////

        Constant.selectedGemTreatment = ""

        for (i in Constant.gemTreatmentAttributeArrayList.indices) {
            // Check if the current technology type is selected
            if (Constant.gemTreatmentAttributeArrayList[i].isSelected) {
                val model = AttributeDetailsModel()
                model.displayAttr =
                    Constant.gemTreatmentAttributeArrayList[i].name
                model.attribCode =
                    Constant.gemTreatmentAttributeArrayList[i].name
                model.filterType = "Treatment"
                model.isSelected = true

                // Check if technologyValue is empty
                if (Constant.technologyValue.equals("", ignoreCase = true)) {
                    // If empty, initialize with the current attribute code
                    Constant.technologyValue = Constant.gemTreatmentAttributeArrayList[i].name
                } else {
                    // If not empty, append the current attribute code, separated by a comma
                    Constant.technologyValue =
                        Constant.technologyValue + "," + Constant.gemTreatmentAttributeArrayList[i].name
                }
                // Add the model to the colorTypeFilterApplied list
                Constant.colorTypeFilterApploedArrayList.add(model)
            } else {
            }
        }

        //////////////////////////--ITEM--/////////////////////////////////////////

        // Initialize technologyValue as an empty string
        Constant.selectedItemsAttribute = ""

        // Iterate through the list of technology types
        for (i in Constant.gemItemsAttributeArrayList.indices) {
            // Check if the current technology type is selected
            if (Constant.gemItemsAttributeArrayList[i].isSelected) {
                val model = AttributeDetailsModel()
                model.displayAttr =
                    Constant.gemItemsAttributeArrayList[i].name // Set display attribute
                model.attribCode =
                    Constant.gemItemsAttributeArrayList[i].name // Set attribute code
                model.filterType = "Item" // Set filter type to "Technology"
                model.isSelected = true // Mark this model as selected

                // Check if technologyValue is empty
                if (Constant.technologyValue.equals("", ignoreCase = true)) {
                    // If empty, initialize with the current attribute code
                    Constant.technologyValue = Constant.gemItemsAttributeArrayList[i].name
                } else {
                    // If not empty, append the current attribute code, separated by a comma
                    Constant.technologyValue =
                        Constant.technologyValue + "," + Constant.gemItemsAttributeArrayList[i].name
                }
                // Add the model to the colorTypeFilterApplied list
                Constant.colorTypeFilterApploedArrayList.add(model)
            } else {
            }
        }

        /////////////////--Cutting style--.///////////////
        Constant.selectedCuttingAttribute = ""

        // Iterate through the list of technology types
        for (i in Constant.gemCuttingAttributeArrayList.indices) {
            if (Constant.gemCuttingAttributeArrayList[i].isSelected) {
                val model = AttributeDetailsModel()
                model.displayAttr =
                    Constant.gemCuttingAttributeArrayList[i].name
                model.attribCode =
                    Constant.gemCuttingAttributeArrayList[i].name
                model.filterType = "CuttingStyle"
                model.isSelected = true

                if (Constant.technologyValue.equals("", ignoreCase = true)) {
                    // If empty, initialize with the current attribute code
                    Constant.technologyValue = Constant.gemCuttingAttributeArrayList[i].name
                } else {
                    // If not empty, append the current attribute code, separated by a comma
                    Constant.technologyValue =
                        Constant.technologyValue + "," + Constant.gemCuttingAttributeArrayList[i].name
                }
                // Add the model to the colorTypeFilterApplied list
                Constant.colorTypeFilterApploedArrayList.add(model)
            } else {
            }
        }

        Log.e("searchType", "@@@@..........." + Constant.searchType)
        Log.e("NATURAL", "@@@@@@@@..........." + ApiConstants.NATURAL)
        if (Constant.searchType.equals("") || Constant.searchType.equals(ApiConstants.NATURAL)) {
            Constant.searchType = ApiConstants.NATURAL
        } else {
            Constant.searchType = ApiConstants.LAB_GROWN
        }
        Log.e("searchType", "...634........" + Constant.searchType)

        val intent = Intent(activity, GemSearchResultActivity::class.java)
        startActivity(intent)

    }

    fun getCurrencyData(context: Context) {
        selectedCurrencyValue = CommonUtility.getGlobalString(context, "selected_currency_value")
        selectedCurrencyCode = CommonUtility.getGlobalString(context, "selected_currency_code")
        selectedCurrencyDesc = CommonUtility.getGlobalString(context, "selected_currency_desc")
        selectedCurrencyImage = CommonUtility.getGlobalString(context, "selected_currency_image")

        if (!selectedCurrencyCode.equals("", ignoreCase = true)) {
            getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode)
            Constant.getCurrencySymbol = getCurrencySymbol
        } else {
            selectedCurrencyCode = INDIA_CURRENCY_CODE
            getCurrencySymbol = CommonUtility.getCurrencySymbol(selectedCurrencyCode)
            Constant.getCurrencySymbol = getCurrencySymbol
        }
        binding.currencyTo.text = Constant.getCurrencySymbol
        binding.currencyText.text = Constant.getCurrencySymbol

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearAllDetails() {

        binding.spinnerAttributesPrice.setSelection(0)
        binding.weightSpinner.setSelection(0)


        binding.priceFrom.setText("")
        binding.priceFrom.hint = "From"

        binding.priceTo.setText("")
        binding.priceTo.hint = "To"

        binding.weightFirst.setText("")
        binding.weightFirst.hint = "1"

        binding.weightThird.setText("")
        binding.weightThird.hint = "3"

        binding.stockId.setText("")
        binding.stockId.hint = "Stock ID"
        setOriginSelectRemove(gemOriginAttribute)
        setCertificateSelectRemove(gemCretificateAttribute)
        setColorSelectRemove(gemColorAttribute)
        setTypeSelectRemove(gemstoneshapeImageArrayList)
        gemOriginAdapter.notifyDataSetChanged()
        gemShapeadapter.notifyDataSetChanged()
        gemColorAdapter.notifyDataSetChanged()
        gemstoneAdapter.notifyDataSetChanged()

        binding.yesTv.setBackgroundResource(R.drawable.background_white)
        binding.noTv.setBackgroundResource(R.drawable.background_white)
        binding.noTv.elevation = 0f
        binding.yesTv.elevation = 0f
        binding.yesTv.setTextColor(ContextCompat.getColor(context, R.color.black))
        binding.noTv.setTextColor(ContextCompat.getColor(context, R.color.black))


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setTypeSelectRemove(list: ArrayList<GemstoneType>) {
        list.forEach {
            it.isSelect = false
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setOriginSelectRemove(list: ArrayList<GemOriginAttribute>) {
        list.forEach {
            it.isSelected = false
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setColorSelectRemove(list: ArrayList<GemColorAttribute>) {
        list.forEach {
            it.isSelected = false
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setCertificateSelectRemove(list: ArrayList<GemCretificateAttribute>) {
        list.forEach {
            it.isSelected = false
        }
    }


    @RequiresApi(Build.VERSION_CODES.P)
    fun labGrownCardTabColorSet() {
        binding.naturalTv.apply {
            setBackgroundResource(R.drawable.border_dark_purple)
            setTextColor(ContextCompat.getColor(context, R.color.purple_light))
        }

        binding.grownTv.apply {
            setBackgroundResource(R.drawable.background_gradient)
            setTextColor(ContextCompat.getColor(context, R.color.white))
        }

        binding.cardViewNatural.apply {
            cardElevation = 0f
            setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color))
            setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_color))
            outlineAmbientShadowColor = ContextCompat.getColor(context, R.color.bg_color)
            outlineSpotShadowColor = ContextCompat.getColor(context, R.color.bg_color)
        }

        binding.cardViewGrown.apply {
            cardElevation = 37f
            cardElevation = 18f
            outlineAmbientShadowColor =
                ContextCompat.getColor(context, R.color.purple_gradient_bottom)
            outlineSpotShadowColor = ContextCompat.getColor(context, R.color.purple_gradient_bottom)
        }
    }


    @RequiresApi(Build.VERSION_CODES.P)
    fun naturalCardTabColorSet() {
        binding.naturalTv.setBackgroundResource(R.drawable.background_gradient)
        binding.grownTv.setBackgroundResource(R.drawable.border_dark_purple)

        binding.naturalTv.setTextColor(ContextCompat.getColor(context, R.color.white))
        binding.grownTv.setTextColor(ContextCompat.getColor(context, R.color.purple_light))

        binding.cardViewNatural.apply {
            cardElevation = 37f
            setOutlineAmbientShadowColor(
                ContextCompat.getColor(
                    context,
                    R.color.purple_gradient_bottom
                )
            )
            setOutlineSpotShadowColor(
                ContextCompat.getColor(
                    context,
                    R.color.purple_gradient_bottom
                )
            )
        }

        binding.cardViewGrown.apply {
            cardElevation = 0f
            setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_color))
            setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color))
            setOutlineAmbientShadowColor(ContextCompat.getColor(context, R.color.bg_color))
            setOutlineSpotShadowColor(ContextCompat.getColor(context, R.color.bg_color))
        }

        searchTypeIntent()
    }

    fun searchTypeIntent() {
        if (searchType == "dxeluxe") {
            binding.cardViewGrown.apply {
                alpha = 0.4f
                isClickable = false
            }
            binding.grownTv.apply {
                isClickable = false
                setBackgroundResource(R.drawable.disable_bg)
                setTextColor(ContextCompat.getColor(context, R.color.grey_light))
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun itemClick(position: Int, action: String?) {
        when (action?.lowercase()) {

            "shapeimage" -> {
                val selectedItem = gemstoneshapeImageArrayList[position]

                if (position == 0) {
                    val selectAll = !selectedItem.isSelect
                    for (i in gemstoneshapeImageArrayList.indices) {
                        gemstoneshapeImageArrayList[i] =
                            gemstoneshapeImageArrayList[i].copy(isSelect = selectAll)
                    }
                } else {
                    val selectedItemSelected = selectedItem.isSelect
                    gemstoneshapeImageArrayList[position] =
                        selectedItem.copy(isSelect = !selectedItemSelected)

                    var allSelected = true
                    for (i in 1 until gemstoneshapeImageArrayList.size) {
                        if (!gemstoneshapeImageArrayList[i].isSelect) {
                            allSelected = false
                            break
                        }
                    }
                    gemstoneshapeImageArrayList[0] =
                        gemstoneshapeImageArrayList[0].copy(isSelect = allSelected)
                }
                gemstoneAdapter.notifyDataSetChanged()

            }

            "gemcertificate" -> {
                Constant.gemCretificateAttributeArrayList[position].isSelected =
                    !Constant.gemCretificateAttributeArrayList[position].isSelected
                gemShapeadapter.notifyDataSetChanged()
            }

            "gemcolor" -> {
                Constant.gemColorAttributeArrayList[position].isSelected =
                    !Constant.gemColorAttributeArrayList[position].isSelected
                gemColorAdapter.notifyDataSetChanged()
            }
            "gemshape" -> {

                Constant.shapsGemarraylist[position].isSelected = !Constant.shapsGemarraylist[position].isSelected
                gemOriginAdapter.notifyDataSetChanged()
            }
        }

    }
}