package net.fpoly.dailymart.view.stock

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.fpoly.dailymart.R
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.ExpiryRes
import net.fpoly.dailymart.data.model.ExpiryUpdate
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.databinding.ItemStockBinding
import net.fpoly.dailymart.extension.time_extention.date2String
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.extension.view_extention.hide
import net.fpoly.dailymart.extension.view_extention.setVisibility
import net.fpoly.dailymart.extension.view_extention.visible
import net.fpoly.dailymart.utils.Constant
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.message.MessageActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class StockAdapter(
    private val mContext: Context,
    private var mListProduct: List<Product>,
) :
    RecyclerView.Adapter<StockAdapter.ItemViewStock>() {

    class ItemViewStock(val binding: ItemStockBinding) : ViewHolder(binding.root)

    private val TAG = "YingMing"
    private var isShowExpiry = false
    private lateinit var mExpiryAdapter: ExpiryAdapter
    private val mToken = SharedPref.getAccessToken(mContext)
    private val mRole = SharedPref.getUser(mContext).role

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Product>) {
        mListProduct = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewStock {
        return ItemViewStock(
            ItemStockBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mListProduct.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewStock, position: Int) {
        var message = ""
        with(holder) {
            with(mListProduct[position]) {
                mExpiryAdapter =
                    ExpiryAdapter(
                        mContext, mRole,
                        onChange = {
                            val newTotal = this.expires.sumOf { e -> e.quantity }
                            if (newTotal < this.totalQuantity) {
                                binding.tvMakeMessage.setVisibility(mRole == ROLE.staff)
                                binding.tvMakeMessage.text =
                                    "Gửi hàng thất thoát tới nhóm"
                                message =
                                    "${this.name} - HSD: ${it.expiryDate.date2String()} thiếu ${totalQuantity - newTotal} ${this.unit}"
                            } else {
                                binding.tvMakeMessage.gone()
                            }
                        }, onSave = {
                            onUpdate(it) {
                                val newTotal = this.expires.sumOf { e -> e.quantity }
                                if (newTotal < this.totalQuantity) {
                                    binding.tvAdd.setVisibility(mRole == ROLE.manager)
                                    binding.tvAdd.text =
                                        "Thêm ${this.totalQuantity - newTotal} vào hàng thất thoát"
                                }
                                binding.tvQuantity.text =
                                    "Tổng số lượng: $newTotal"
                            }
                        })
                binding.tvMakeMessage.setOnClickListener {
                    val intent = Intent(mContext, MessageActivity::class.java)
                    intent.putExtra(Constant.MESSAGE, message)
                    mContext.startActivity(intent)
                    message = ""
                }
                binding.tvAdd.setOnClickListener {

                }
                binding.imvArrow.hide(this.expires.isEmpty())
                binding.tvName.text = this.name
                binding.tvBarcode.text = "Barcode: ${this.barcode}"
                binding.tvQuantity.text = "Tổng số lượng: ${this.totalQuantity}"
                mExpiryAdapter.setDate(this.expires)
                binding.rcvExpiry.adapter = mExpiryAdapter
                binding.imvArrow.setOnClickListener {
                    isShowExpiry = !isShowExpiry
                    if (isShowExpiry) {
                        binding.layoutExpiry.visible()
                        binding.imvArrow.rotation = 90f
                    } else {
                        binding.layoutExpiry.gone()
                        binding.imvArrow.rotation = 180f
                    }
                }
            }
        }
    }

    private fun onUpdate(expiry: ExpiryRes, onSuccess: () -> Unit) {
        val api = ServerInstance.apiProduct
        try {
            api.updateExpiry(mToken, expiry.id, ExpiryUpdate(expiry.expiryDate, expiry.quantity))
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>,
                    ) {
                        if (response.isSuccessful) {
                            onSuccess()
                        }
                        Log.e(TAG, "onUpdate expiry: ${response.body()?.string()}")
                        Log.e(TAG, "onUpdate expiry : ${response.errorBody()?.string()}")
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e(TAG, "onUpdate expiry Throwable: $t")
                    }

                })
        } catch (e: Exception) {
            Log.e(TAG, "onUpdate expiry Exception: $e")
        }
    }
}