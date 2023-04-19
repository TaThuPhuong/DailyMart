package net.fpoly.dailymart.view.task

import android.content.Context
import android.util.Log
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.databinding.DialogPickTimeBinding
import net.fpoly.dailymart.extension.custom_view.NumberPickView
import net.fpoly.dailymart.extension.num_extension.toStrDecimal
import java.util.Calendar

class PickTimeDialog(
    private val mContext: Context,
    private val onChoseTime: (time: Long) -> Unit,
) :
    BaseBottomDialog<DialogPickTimeBinding>(mContext, DialogPickTimeBinding::inflate) {

    private var listMinute = arrayOfNulls<String>(60)
    private var listHour: Array<String?> = arrayOf(
        "00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
        "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"
    )

    var currentHour = 7
    var currentMinute = 0

    override fun initData() {
        val calendar = Calendar.getInstance()
        currentHour = calendar[Calendar.HOUR_OF_DAY]
        currentMinute = calendar[Calendar.MINUTE]
        Log.d("YingMing", "initData: $currentHour -$currentMinute")
        for (i in listMinute.indices) {
            listMinute[i] = i.toStrDecimal()
        }

        binding.npvPopupPick1.minValue = 0
        binding.npvPopupPick1.setDisplayedValues(listHour, false)
        binding.npvPopupPick1.maxValue = listHour.size - 1
        binding.npvPopupPick1.value = currentHour
        binding.npvPopupPick1.wrapSelectorWheel = true

        binding.npvPopupPick2.minValue = 0
        binding.npvPopupPick2.setDisplayedValues(listMinute, false)
        binding.npvPopupPick2.maxValue = listMinute.size - 1
        binding.npvPopupPick2.value = currentMinute
        binding.npvPopupPick2.wrapSelectorWheel = true

        binding.npvPopupPick1.setOnValueChangedListener(object :
            NumberPickView.OnValueChangeListener {
            override fun onValueChange(picker: NumberPickView?, oldVal: Int, newVal: Int) {
                currentHour = newVal
            }
        })
        binding.npvPopupPick2.setOnValueChangedListener(object :
            NumberPickView.OnValueChangeListener {
            override fun onValueChange(picker: NumberPickView?, oldVal: Int, newVal: Int) {
                currentMinute = newVal - 1
            }
        })
        binding.btnSave.setOnClickListener {
            calendar.set(Calendar.HOUR_OF_DAY, currentHour)
            calendar.set(Calendar.MINUTE, currentMinute)
            onChoseTime(calendar.timeInMillis)
            dismiss()
        }
    }
}