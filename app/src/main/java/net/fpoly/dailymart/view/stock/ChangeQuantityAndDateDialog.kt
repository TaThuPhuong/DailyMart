package net.fpoly.dailymart.view.stock

import android.content.Context
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.databinding.DialogChangeQuantityBinding
import net.fpoly.dailymart.extension.view_extention.getTextOnChange
import java.util.*

class ChangeQuantityAndDateDialog(
    private val mContext: Context,
    private var quantity: Int,
    private var time: Long,
    private val onSave: (newQuantity: Int, newTime: Long) -> Unit,
) :
    BaseBottomDialog<DialogChangeQuantityBinding>(mContext, DialogChangeQuantityBinding::inflate) {

    private val calendar = Calendar.getInstance()
    private var maxDayInMonths = 0
    override fun initData() {
        initNumPicker()
        binding.edQuantity.setText(quantity.toString())
        binding.edQuantity.getTextOnChange {
            if (it.isNotEmpty()) {
                quantity = try {
                    it.toInt()
                } catch (e: Exception) {
                    0
                }
            }
        }
        binding.tvSave.setOnClickListener {
            onSave(quantity, getTime())
            dismiss()
        }
        binding.imvClose.setOnClickListener { dismiss() }
    }

    private fun getMaxDayInMonths(month: Int, year: Int): Int {
        return if (month == 2) {
            if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
        } else if (month == 4 || month == 6 || month == 9 || month == 11) 30 else 31
    }

    private fun initNumPicker() {
        calendar.timeInMillis = time
        maxDayInMonths =
            getMaxDayInMonths(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR))
        binding.pickerDate.minValue = 1
        binding.pickerDate.maxValue = maxDayInMonths
        binding.pickerDate.value = calendar.get(Calendar.DAY_OF_MONTH)
        binding.pickerMonth.minValue = 1
        binding.pickerMonth.maxValue = 12
        binding.pickerMonth.value = calendar.get(Calendar.MONTH) + 1
        binding.pickerYear.minValue = 2023
        binding.pickerYear.maxValue = 2100
        binding.pickerYear.value = calendar.get(Calendar.YEAR)

        binding.pickerDate.wrapSelectorWheel = true
        binding.pickerMonth.wrapSelectorWheel = true
        binding.pickerYear.wrapSelectorWheel = true

        binding.pickerMonth.setOnValueChangedListener { _, _, newVal ->
            maxDayInMonths = getMaxDayInMonths(newVal, binding.pickerYear.value)
            binding.pickerDate.maxValue = maxDayInMonths
        }
        binding.pickerYear.setOnValueChangedListener { _, _, newVal ->
            maxDayInMonths = getMaxDayInMonths(binding.pickerMonth.value, newVal)
            binding.pickerDate.maxValue = maxDayInMonths
        }
    }

    private fun getTime(): Long {
        calendar.set(
            binding.pickerYear.value,
            binding.pickerMonth.value - 1,
            binding.pickerDate.value
        )
        return calendar.timeInMillis
    }
}