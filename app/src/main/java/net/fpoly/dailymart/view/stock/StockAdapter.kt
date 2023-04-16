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
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.databinding.ItemStockBinding
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.extension.view_extention.hide
import net.fpoly.dailymart.extension.view_extention.visible
import net.fpoly.dailymart.utils.SharedPref
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
        with(holder) {
            with(mListProduct[position]) {
                mExpiryAdapter =
                    ExpiryAdapter(
                        mContext, onSave = {
                            onUpdate(it)
                        })
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

    private fun onUpdate(expiry: ExpiryRes) {
        val api = ServerInstance.apiProduct
        try {
            api.updateExpiry(mToken, ExpiryUpdate(expiry.expiryDate, expiry.quantity))
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>,
                    ) {
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