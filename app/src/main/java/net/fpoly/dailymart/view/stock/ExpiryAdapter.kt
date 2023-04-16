package net.fpoly.dailymart.view.stock

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.fpoly.dailymart.R
import net.fpoly.dailymart.data.model.ExpiryRes
import net.fpoly.dailymart.databinding.ItemExpiryStockBinding
import net.fpoly.dailymart.extension.time_extention.date2String
import java.util.*
import kotlin.collections.ArrayList

class ExpiryAdapter(
    private val mContext: Context,
    private var mListExpiry: ArrayList<ExpiryRes> = ArrayList(),
    private val onSave: (ExpiryRes) -> Unit,
) : RecyclerView.Adapter<ExpiryAdapter.ItemView>() {

    class ItemView(val binding: ItemExpiryStockBinding) : ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setDate(list: ArrayList<ExpiryRes>) {
        mListExpiry = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        return ItemView(
            ItemExpiryStockBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mListExpiry.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemView, position: Int) {
        with(holder) {
            with(mListExpiry[position]) {
                binding.tvDate.text = "Hạn sử dụng: ${this.expiryDate.date2String()}"
                binding.tvQuantity.text = "Số lượng ${this.quantity}"
                binding.layoutDate.setOnClickListener {
                    datePicker(this.expiryDate) {
                        this.expiryDate = it
                        binding.tvDate.text = "Hạn sử dụng: ${it.date2String()}"
                    }
                }
                binding.layoutQuantity.setOnClickListener {
                    ChangeQuantityDialog(mContext, this.quantity) {
                        this.quantity = it
                        binding.tvQuantity.text = "Số lượng: $it"
                    }.show()
                }
                binding.tvSave.setOnClickListener { onSave(this) }
            }
        }
    }

    private fun datePicker(time: Long, onPick: (time: Long) -> Unit) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        val theme = R.style.DatePickerTheme
        val datePickerDialog = DatePickerDialog(mContext, theme, { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            onPick(calendar.timeInMillis)
        }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DATE])
        datePickerDialog.show()
    }
}