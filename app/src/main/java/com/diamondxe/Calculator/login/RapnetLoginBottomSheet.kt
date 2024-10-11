package com.dxe.calc.login

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.diamondxe.R
import com.diamondxe.databinding.BottomSheetRapnetLoginBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RapnetLoginBottomSheet : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "RapnetLoginBottomSheet"
    }

    private lateinit var binding: BottomSheetRapnetLoginBinding
    private var isPasswordVisible = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetRapnetLoginBinding.inflate(inflater, container, false)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        binding.rlClose.setOnClickListener {
            dismiss()
        }

        binding.rlShowHidePassword.setOnClickListener {
            showHidePassword()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        //full screen height of bottom sheet
        (dialog as BottomSheetDialog).behavior.peekHeight = getBottomSheetDialogDefaultHeight()
        dialog?.let {
            val bottomSheet = it.findViewById<View>(R.id.bottom_sheet_root)
            bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return displayMetrics.heightPixels// * 85 / 100
    }

    private fun showHidePassword() {
        if (isPasswordVisible) {
            binding.edtPassword.transformationMethod = null
            binding.ivPasswordVisibility.background = requireActivity().getDrawable(R.drawable.hide_password)
        } else {
            binding.edtPassword.transformationMethod = PasswordTransformationMethod()
            binding.ivPasswordVisibility.background = requireActivity().getDrawable(R.drawable.show_password)
        }
        isPasswordVisible = !isPasswordVisible
    }
}