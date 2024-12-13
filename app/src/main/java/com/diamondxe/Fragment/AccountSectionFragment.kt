package com.diamondxe.Fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.diamondxe.Activity.APISolutions.ApiSolutionRequestActivity
import com.diamondxe.Activity.Buyer.BuyerKYCVerificationActivity
import com.diamondxe.Activity.Buyer.BuyerKYCVerificationDocUploadActivity
import com.diamondxe.Activity.ChangePasswordActivity
import com.diamondxe.Activity.Dealer.CustomPaymentScreenActivity
import com.diamondxe.Activity.Dealer.DealerMarkupScreenActivity
import com.diamondxe.Activity.Dealer.KYCVerificationActivity
import com.diamondxe.Activity.Dealer.WalletScreenActivity
import com.diamondxe.Activity.LoginScreenActivity
import com.diamondxe.Activity.MyOrder.MyOrderListScreenActivity
import com.diamondxe.Activity.PersonalProfileActivity
import com.diamondxe.Activity.PlaceOrder.PlaceOrderScreenActivity
import com.diamondxe.Activity.SearchDiamondsActivity
import com.diamondxe.Activity.ShippingBillingAddressListActivity
import com.diamondxe.ApiCalling.ApiConstants
import com.diamondxe.ApiCalling.ApiConstants.CART
import com.diamondxe.ApiCalling.ApiConstants.USER_BUYER
import com.diamondxe.ApiCalling.ApiConstants.USER_DEALER
import com.diamondxe.ApiCalling.VollyApiActivity
import com.diamondxe.Interface.FragmentActionInterface
import com.diamondxe.Interface.RecyclerInterface
import com.diamondxe.Network.SuperFragment
import com.diamondxe.R
import com.diamondxe.Utils.CommonUtility
import com.diamondxe.Utils.Constant
import com.diamondxe.Utils.Utils
import com.diamondxe.databinding.ActivityAccountSectionBinding
import com.diamondxe.databinding.BottomBarBinding
import com.diamondxe.databinding.DialogLayoutLogoutBinding
import com.dxe.calc.dashboard.CalculatorActivity
import org.json.JSONObject

class AccountSectionFragment : SuperFragment(), RecyclerInterface, View.OnClickListener,
    FragmentActionInterface
{
    lateinit var binding: ActivityAccountSectionBinding
    var  userRole : String =""
    var  user_login:String=""
    lateinit var activity: Activity
    lateinit var appContext: Context
     var  isArrowDown:Boolean=false
     var document_status:String=""
    lateinit  var vollyApiActivity:VollyApiActivity
     lateinit var bottomBarBinding: BottomBarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityAccountSectionBinding.inflate(inflater, container, false)
        val bottomBarView = binding.root.findViewById<View>(R.id.bottom_bar_layout)
        if (bottomBarView != null) {
            bottomBarBinding = BottomBarBinding.bind(bottomBarView)
        } else {
            Log.e("BindingError", "bottom_bar_layout not found in activity_account_section.xml")
        }
        showCardCount()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appContext = context
        activity = context as Activity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickButtonFun()
        userRole = CommonUtility.getGlobalString(activity, "login_user_role")
        if (userRole.equals("DEALER", ignoreCase = true)) {
           binding.titleTv.text = getString(R.string.dealer_account)
           binding.dealerMarkUpRel.visibility = View.VISIBLE
           binding.customPaymentRel.visibility = View.VISIBLE
           binding.apiSolutionRel.visibility = View.VISIBLE
        } else if (userRole.equals("BUYER", ignoreCase = true)) {
            binding.titleTv.text = getString(R.string.buyer_account)
            binding.dealerMarkUpRel.visibility = View.GONE
            binding.customPaymentRel.visibility = View.VISIBLE
            // wallet_rel.visibility = View.GONE
            /* dealer_mark_up_rel.visibility = View.GONE
               custom_payment_rel.visibility = View.GONE */
          binding.apiSolutionRel.visibility = View.GONE
        } else {
            binding.titleTv.text = getString(R.string.account)
        }

        user_login = CommonUtility.getGlobalString(activity, "user_login")

        if (user_login.equals("yes", ignoreCase = true)) {
           binding.deleteAccountRel.setBackgroundColor(ContextCompat.getColor(appContext, R.color.transparent))
           binding.myDeleteTv.setTextColor(ContextCompat.getColor(appContext, R.color.purple_light))
           binding.img10.setColorFilter(ContextCompat.getColor(appContext, R.color.purple_light))
        } else {
            binding.deleteAccountRel.setBackgroundColor(ContextCompat.getColor(appContext, R.color.light_gray1))
            binding.img10.setColorFilter(ContextCompat.getColor(appContext, R.color.shimmer_color))
            binding.myDeleteTv.setTextColor(ContextCompat.getColor(appContext, R.color.shimmer_color))
        }

        if (user_login.isNotEmpty()) {
            val role = CommonUtility.getGlobalString(context, "login_user_role")
            when (role?.toUpperCase()) {
                "BUYER" -> bottomBarBinding.accountTv.text = USER_BUYER
                "DEALER" -> bottomBarBinding.accountTv.text = USER_DEALER
                else -> {}
            }
        } else {
            bottomBarBinding.accountTv.text = getString(R.string.login)
        }

        bottomBarBinding.searchCircleRel.setOnClickListener(this)
        bottomBarBinding.bottomSearchIcon.setBackgroundResource(R.mipmap.bottom_search)

// Search Zoom-In and Zoom-Out Animation
        CommonUtility.startZoomAnimation(bottomBarBinding.bottomSearchIcon)

    }



    override fun getSuccessResponce(jsonObject: JSONObject?, service_ID: Int) {
        TODO("Not yet implemented")
    }

    override fun getErrorResponce(error: String?, service_ID: Int) {
        TODO("Not yet implemented")
    }

    fun clickButtonFun()
    {
        binding.walletRel.setOnClickListener(this)
        binding.myOrderRel.setOnClickListener(this)
        binding.changePasswordRel.setOnClickListener(this)
        binding.kycVerificationRel.setOnClickListener(this)
        binding.profileRel.setOnClickListener(this)
        binding.addressRel.setOnClickListener(this)
        binding.myAccountRel.setOnClickListener(this)

        binding.auctionRel.setOnClickListener(this)
        binding.dealerMarkUpRel.setOnClickListener(this)
        binding.loyaltyProgramRel.setOnClickListener(this)

        binding.customPaymentRel.setOnClickListener(this)
        binding.apiSolutionRel.setOnClickListener(this)
        binding.deleteAccountRel.setOnClickListener(this)

        bottomBarBinding.homeRel.setOnClickListener(this)
        bottomBarBinding.dxeCalcRev.setOnClickListener(this)
        bottomBarBinding.categoryRel.setOnClickListener(this)
        bottomBarBinding.wishlistRel.setOnClickListener(this)
        bottomBarBinding.accountRel.setOnClickListener(this)
        bottomBarBinding.searchCircleRel.setOnClickListener(this)
        bottomBarBinding.cartRel.setOnClickListener(this)


        bottomBarBinding.homeImg.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey_light))
        bottomBarBinding.categoriesImg.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey_light))
        bottomBarBinding.wishImg.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey_light))
        bottomBarBinding.cartImg.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey_light))
        bottomBarBinding.accountImg.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple_light))

        bottomBarBinding.homeTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_light))
        bottomBarBinding.categoriesTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_light))
        bottomBarBinding.wishTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_light))
        bottomBarBinding.cartTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_light))
        bottomBarBinding.accountTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_light))



    }
    override fun onClick(view: View?) {
        val id = view?.id
        val userLogin = CommonUtility.getGlobalString(activity, "user_login")

        when (id) {
            R.id.back_img -> {
                Utils.hideKeyboard(activity)
                requireActivity().finish()
            }
            R.id.my_account_rel -> {
                Utils.hideKeyboard(activity)
                if (isArrowDown) {
                  binding.dropDownMyAccount.setImageResource(R.drawable.down)
                   binding.myAccountSubSection.visibility = View.GONE
                } else {
                    binding.dropDownMyAccount.setImageResource(R.drawable.up)
                    binding.myAccountSubSection.visibility = View.VISIBLE
                }
                isArrowDown = !isArrowDown
            }
            R.id.profile_rel -> {
                Utils.hideKeyboard(activity)
                if (userLogin.equals("yes", ignoreCase = true)) {
                    startActivity(Intent(activity, PersonalProfileActivity::class.java))
                    requireActivity().overridePendingTransition(0, 0)
                } else {
                    requireActivity().finish()
                }
            }
            R.id.address_rel -> {
                Utils.hideKeyboard(activity)
                if (userLogin.equals("yes", ignoreCase = true)) {
                    Constant.showRadioButtonForSelectAddress = ""
                    startActivity(Intent(activity, ShippingBillingAddressListActivity::class.java))
                    requireActivity().overridePendingTransition(0, 0)
                } else {
                    requireActivity().finish()
                }
            }
            R.id.kyc_verification_rel -> {
                Utils.hideKeyboard(activity)
                if (userLogin.equals("yes", ignoreCase = true)) {
                    val intent = if (userRole.equals("DEALER", ignoreCase = true)) {
                        Intent(activity, KYCVerificationActivity::class.java)
                    } else {
                        if (document_status.equals("0", ignoreCase = true)) {
                            Intent(activity, BuyerKYCVerificationDocUploadActivity::class.java)
                        } else {
                            Intent(activity, BuyerKYCVerificationActivity::class.java)
                        }
                    }
                    startActivity(intent)
                    requireActivity().overridePendingTransition(0, 0)
                } else {
                    requireActivity().finish()
                }
            }
            R.id.change_password_rel -> {
                Utils.hideKeyboard(activity)
                if (userLogin.equals("yes", ignoreCase = true)) {
                    startActivity(Intent(activity, ChangePasswordActivity::class.java))
                    requireActivity().overridePendingTransition(0, 0)
                } else {
                    requireActivity().finish()
                }
            }
            R.id.my_order_rel -> {
                Utils.hideKeyboard(activity)
                if (userLogin.equals("yes", ignoreCase = true)) {
                    Constant.comeFrom = ""
                    Constant.afterCancelOrderManageScreenCall = ""
                    Constant.afterReturnOrderManageScreenCall = ""
                    startActivity(Intent(activity, MyOrderListScreenActivity::class.java))
                    requireActivity().overridePendingTransition(0, 0)
                } else {
                    requireActivity().finish()
                }
            }
            R.id.wallet_rel -> {
                Utils.hideKeyboard(activity)
                if (userLogin.equals("yes", ignoreCase = true)) {
                    startActivity(Intent(activity, WalletScreenActivity::class.java))
                    requireActivity().overridePendingTransition(0, 0)
                } else {
                    requireActivity().finish()
                }
            }
            R.id.auction_rel -> {
                Utils.hideKeyboard(activity)
                if (!userLogin.equals("yes", ignoreCase = true)) {
                    requireActivity().finish()
                }
            }
            R.id.dealer_mark_up_rel -> {
                Utils.hideKeyboard(activity)
                if (userLogin.equals("yes", ignoreCase = true)) {
                    startActivity(Intent(activity, DealerMarkupScreenActivity::class.java))
                    requireActivity().overridePendingTransition(0, 0)
                } else {
                    requireActivity().finish()
                }
            }
            R.id.loyalty_program_rel -> {
                Utils.hideKeyboard(activity)
                if (!userLogin.equals("yes", ignoreCase = true)) {
                    requireActivity().finish()
                }
            }
            R.id.custom_payment_rel -> {
                Utils.hideKeyboard(activity)
                if (userLogin.equals("yes", ignoreCase = true)) {
                    startActivity(Intent(activity, CustomPaymentScreenActivity::class.java))
                    requireActivity().overridePendingTransition(0, 0)
                } else {
                    requireActivity().finish()
                }
            }
            R.id.api_solution_rel -> {
                Utils.hideKeyboard(activity)
                if (userLogin.equals("yes", ignoreCase = true)) {
                    startActivity(Intent(activity, ApiSolutionRequestActivity::class.java))
                    requireActivity().overridePendingTransition(0, 0)
                } else {
                    requireActivity().finish()
                }
            }
            R.id.delete_account_rel -> {
                Utils.hideKeyboard(activity)
                if (userLogin.equals("yes", ignoreCase = true)) {
                    deleteAccountConfirmationDialogPopup(
                        requireActivity(),
                        requireContext(),
                        getString(R.string.delete_account_msg)
                    )
                } else {
                    requireActivity().finish()
                }
            }
            R.id.home_rel ->{
                if (Constant.manageClickEventForRedirection.equals("placeOrderAddToCardFragment", ignoreCase = true)) {
                    Constant.manageClickEventForRedirection = ""
                } else {
                    // Do nothing
                }
                val newFragment = HomeFragment()

                // Remove previous fragments
                val fragmentManager = requireActivity().supportFragmentManager
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

                // Open new fragment
                val transaction = fragmentManager.beginTransaction()
                transaction.replace(R.id.container_body, newFragment)
                transaction.commit()
            }
            R.id.dxe_calc_rev ->{
                val intent = Intent(context, CalculatorActivity::class.java)
                startActivity(intent)
            }
            R.id.category_rel -> {
                val fragment = CategoryFragmentList()
                val fragmentManager = requireActivity().supportFragmentManager
                val transaction = fragmentManager.beginTransaction()
                transaction.replace(R.id.container_body, fragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
            R.id.wishlist_rel ->{
                val fragment = WishlistFragment()
                val fragmentManager = requireActivity().supportFragmentManager
                val transaction = fragmentManager.beginTransaction()
                transaction.replace(R.id.container_body, fragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
            R.id.account_rel->{
                val userLogin = CommonUtility.getGlobalString(activity, "user_login")
                if (userLogin.equals("yes", ignoreCase = true)) {
                    Log.e("In IF", "...890...#############.........")
                    val fragment = AccountSectionFragment()
                    val fragmentManager = requireActivity().supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.container_body, fragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()

                } else {
                    val intent = Intent(context, LoginScreenActivity::class.java)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(0, 0)
                }

            }
            R.id.place_order_tv ->{
                Utils.hideKeyboard(activity)
                val userLogin = CommonUtility.getGlobalString(activity, "user_login")
                if (userLogin.equals("yes", ignoreCase = true)) {
                    Constant.showRadioButtonForSelectAddress = "yes" // Under Address List Radio Button Visible
                    Constant.orderType = CART // Order Type
                    Constant.certificateNumber = "" // Blank Certificate Number
                    Constant.manageShippingBillingAddressSelection = ""
                    Constant.manageBillingByAddressAddUpdate = ""
                    Constant.manageShippingByAddressAddUpdate = ""
                    val intent = Intent(activity, PlaceOrderScreenActivity::class.java)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(0, 0)
                } else {
                    Constant.manageClickEventForRedirection = "placeOrderAddToCardFragment"
                    val intent = Intent(activity, LoginScreenActivity::class.java)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(0, 0)
                }

            }
            R.id.search_circle_rel -> {
                Constant.searchTitleName = "Solitaires"
                Constant.searchType = ApiConstants.NATURAL
                Constant.filterClear = ""
                val intent = Intent(context, SearchDiamondsActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(0, 0)
            }
            R.id.cart_rel -> {
                val fragment = AddToCartListFragment()
                val fragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.container_body, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()

            }
        }
    }

    fun showCardCount() {
        // Log.e("Constant.wishListCount : ", Constant.wishListCount.toString())

        if (Constant.cardCount.equals("", ignoreCase = true) || Constant.cardCount.equals("0", ignoreCase = true)) {
           bottomBarBinding.cartCountTv.visibility = View.GONE
        } else {
            bottomBarBinding.cartCountTv.text = Constant.cardCount
            bottomBarBinding.cartCountTv.visibility = View.VISIBLE
        }

        if (Constant.wishListCount.equals("", ignoreCase = true) || Constant.wishListCount.equals("0", ignoreCase = true)) {
            bottomBarBinding.wishListCountTv.visibility = View.GONE
        } else {
            bottomBarBinding.wishListCountTv.text = Constant.wishListCount
            bottomBarBinding.wishListCountTv.visibility = View.VISIBLE
        }
    }


    override fun itemClick(position: Int, action: String?) {
        TODO("Not yet implemented")
    }

    override fun actionInterface(value: String?, action: String?) {
        TODO("Not yet implemented")
    }


    fun deleteAccountConfirmationDialogPopup(activity: Activity, context: Context, message: String) {
        val dialogBuilder = android.app.AlertDialog.Builder(context)
        val inflater = activity.layoutInflater

        // Inflate the dialog layout using view binding
        val binding = DialogLayoutLogoutBinding.inflate(inflater)
        dialogBuilder.setView(binding.root)

        val alertDialog = dialogBuilder.create()

        binding.title.text = context.getString(R.string.app_name)
        binding.message.text = message

        binding.yesTv.setOnClickListener {
            deleteAccountConfirmationDialogPopupAgain(activity, context, context.getString(R.string.delete_account_msg1))
            alertDialog.dismiss()
        }

        binding.noTv.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCancelable(true)
        alertDialog.show()
    }

    fun deleteAccountConfirmationDialogPopupAgain(activity: Activity, context: Context, message: String) {
        val dialogBuilder = android.app.AlertDialog.Builder(context)
        val inflater = activity.layoutInflater

        // Inflate the dialog layout using view binding
        val binding = DialogLayoutLogoutBinding.inflate(inflater)
        dialogBuilder.setView(binding.root)

        val alertDialog = dialogBuilder.create()

        binding.title.text = context.getString(R.string.app_name)
        binding.message.text = message

        binding.yesTv.setOnClickListener {
            onDeleteAccountAPI(false)
            alertDialog.dismiss()
        }

        binding.noTv.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCancelable(true)
        alertDialog.show()
    }
    fun onDeleteAccountAPI(showLoader: Boolean) {
        val uuid = CommonUtility.getAndroidId(context)
        Log.e("-----uuid------", uuid)

        if (Utils.isNetworkAvailable(context)) {
            val urlParameter = hashMapOf(
                "deviceId" to uuid
            )

            vollyApiActivity = VollyApiActivity(
                context,
                this,
                urlParameter,
                ApiConstants.DELETE_ACCOUNT,
                ApiConstants.DELETE_ACCOUNT_ID,
                showLoader,
                "POST"
            )
        } else {
            showToast(ApiConstants.MSG_INTERNETERROR)
        }
    }


}