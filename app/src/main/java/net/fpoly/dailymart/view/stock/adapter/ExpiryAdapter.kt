package net.fpoly.dailymart.view.stock.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.ExpiryRes
import net.fpoly.dailymart.data.model.ExpiryUpdate
import net.fpoly.dailymart.databinding.ItemExpiryStockBinding
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.extension.time_extention.date2String
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.stock.ChangeQuantityAndDateDialog
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class ExpiryAdapter(
    private val mContext: Context,
    private val mRole: ROLE,
    private var mListExpiry: ArrayList<ExpiryRes> = ArrayList(),
    private val onChangeQuantity: (expiry: ExpiryRes, numChange: Int) -> Unit,
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
                binding.tvDate.text = "HSD: ${this.expiryDate.date2String()}"
                binding.tvQuantity.text = "SL: ${this.quantity}"
                binding.root.setOnClickListener {
                    val qt = binding.tvQuantity.text
                    val i = qt.indexOf(" ")
                    val quantity = try {
                        binding.tvQuantity.text.substring(i + 1, qt.length).toInt()
                    } catch (e: Exception) {
                        this.quantity
                    }
                    Log.e(TAG, "quantity:$quantity ")
                    ChangeQuantityAndDateDialog(
                        mContext,
                        quantity = quantity,
                        this.expiryDate
                    ) { newQuantity, newTime ->
                        val numChange = newQuantity - this.quantity
                        this.expiryDate = newTime
                        if (mRole == ROLE.manager) {
                            this.quantity = newQuantity
                            onUpdate(this) { b ->
                                if (b) {
                                    binding.tvDate.text = "HSD: ${this.expiryDate.date2String()}"
                                    binding.tvQuantity.text = "SL: $newQuantity"
                                    onChangeQuantity(this, numChange)
                                    showToast(mContext, "Cập nhập thành công")
                                } else {
                                    showToast(mContext, "Lỗi kết nối")
                                }
                            }
                        } else {
                            onUpdate(this) { b ->
                                if (b) {
                                    binding.tvDate.text = "HSD: ${this.expiryDate.date2String()}"
                                    binding.tvQuantity.text = "SL: $newQuantity"
                                    onChangeQuantity(this, numChange)
                                } else {
                                    showToast(mContext, "Lỗi kết nối")
                                }
                            }
                        }
                    }.show()
                }
            }
        }
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