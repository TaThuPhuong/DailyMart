package net.fpoly.dailymart.view.check_date

import android.annotation.SuppressLint
import android.content.Context
import com.bumptech.glide.Glide
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.data.model.ExpiryCheck
import net.fpoly.dailymart.databinding.DialogCheckDateDetailBinding
import net.fpoly.dailymart.extension.time_extention.date2String
import net.fpoly.dailymart.extension.view_extention.setVisibility
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref

class CheckDateDetailDialog(
    private val mContext: Context,
    private val expiry: ExpiryCheck,
    private val onDestroy: () -> Unit,
    private val onMakeMessage: () -> Unit,
) :
    BaseBottomDialog<DialogCheckDateDetailBinding>(
        mContext,
        DialogCheckDateDetailBinding::inflate
    ) {
    @SuppressLint("SetTextI18n")
    override fun initData() {
        val role = SharedPref.getUser(mContext).role
        val outOfDate = System.currentTimeMillis() > expiry.expiryDate
        binding.tvCancelProduct.setVisibility(role != ROLE.staff && outOfDate)
        binding.tvMakeMessage.setVisibility(role == ROLE.staff && outOfDate)
        binding.tvClose.setVisibility(!outOfDate)
        binding.tvName.text = expiry.productName
        binding.tvDate.text = expiry.expiryDate.date2String()
        binding.tvBarcode.text = "Barcode: ${expiry.barcode}"
        binding.tvQuantity.text = expiry.quantity.toString()
        Glide.with(mContext).load(expiry.image)
            .placeholder(R.drawable.img_default).into(binding.imvImage)
        binding.imvClose.setOnClickListener {
            dismiss()
        }
        binding.tvClose.setOnClickListener {
            dismiss()
        }
        binding.tvCancelProduct.setOnClickListener {
            onDestroy()
            dismiss()
        }
        binding.tvMakeMessage.setOnClickListener {
            onMakeMessage()
            dismiss()
        }
    }
}