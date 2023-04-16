package net.fpoly.dailymart.view.stock

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.fpoly.dailymart.R
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.ExpiryRes
import net.fpoly.dailymart.data.model.ExpiryUpdate
import net.fpoly.dailymart.databinding.ItemExpiryStockBinding
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.extension.time_extention.date2String
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.extension.view_extention.setVisibility
import net.fpoly.dailymart.extension.view_extention.visible
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class ExpiryAdapter(
    private val mContext: Context,
    private var mListExpiry: ArrayList<ExpiryRes> = ArrayList(),
    private val onChangeQuantity: (expiry: ExpiryRes) -> Unit,
) : RecyclerView.Adapter<ExpiryAdapter.ItemView>() {

    class ItemView(val binding: ItemExpiryStockBinding) : ViewHolder(binding.root)

    private val TAG = "YingMing"
    private val mToken = SharedPref.getAccessToken(mContext)

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
                        onUpdate(this) { b ->
                            if (b) {
                                binding.tvDate.text = "Hạn sử dụng: ${it.date2String()}"
                                showToast(mContext, "Cập nhập thành công")
                            } else {
                                showToast(mContext, "Lỗi kết nối")
                            }
                        }
                    }
                }
                binding.layoutQuantity.setOnClickListener {
                    ChangeQuantityDialog(mContext, this.quantity) {
                        this.quantity = it
                        onUpdate(this) { b ->
                            if (b) {
                                binding.tvQuantity.text = "Số lượng: $it"
                                onChangeQuantity(this)
                                showToast(mContext, "Cập nhập thành công")
                            } else {
                                showToast(mContext, "Lỗi kết nối")
                            }
                        }
                    }.show()
                }
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

    private fun onUpdate(expiry: ExpiryRes, onSuccess: (b: Boolean) -> Unit) {
        val api = ServerInstance.apiProduct
        try {
            api.updateExpiry(mToken, expiry.id, ExpiryUpdate(expiry.expiryDate, expiry.quantity))
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>,
                    ) {
                        if (response.isSuccessful) {
                            onSuccess(true)
                        } else {
                            onSuccess(false)
                        }
                        Log.e(TAG, "onUpdate expiry: ${response.body()?.string()}")
                        Log.e(TAG, "onUpdate expiry : ${response.errorBody()?.string()}")
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        onSuccess(false)
                        Log.e(TAG, "onUpdate expiry Throwable: $t")
                    }

                })
        } catch (e: Exception) {
            onSuccess(false)
            Log.e(TAG, "onUpdate expiry Exception: $e")
        }
    }
}