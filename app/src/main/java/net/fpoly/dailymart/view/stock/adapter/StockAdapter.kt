package net.fpoly.dailymart.view.stock

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import net.fpoly.dailymart.data.model.*
import net.fpoly.dailymart.databinding.ItemStockBinding
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.extension.time_extention.date2String
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.extension.view_extention.hide
import net.fpoly.dailymart.extension.view_extention.visible
import net.fpoly.dailymart.firbase.database.LossesDao
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.stock.adapter.ExpiryAdapter
import java.util.*
import kotlin.math.abs

class StockAdapter(
    private val mContext: Context,
    private var mListProduct: List<Product>,
    private val onChange: (StockCheck) -> Unit,
) :
    RecyclerView.Adapter<StockAdapter.ItemViewStock>() {

    class ItemViewStock(val binding: ItemStockBinding) : ViewHolder(binding.root)

    private val TAG = "YingMing"
    private var isShowExpiry = false
    private lateinit var mExpiryAdapter: ExpiryAdapter
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
                var lossProduct = 0L
                mExpiryAdapter =
                    ExpiryAdapter(
                        mContext, mRole,
                        onChangeQuantity = { ex, n ->
                            val newTotal = this.expires.sumOf { e -> e.quantity }
                            if (n > 0) {
                                message = "Dư $n"
                                onChange(
                                    StockCheck(
                                        ex.id,
                                        this.name,
                                        this.barcode,
                                        ex.expiryDate,
                                        message
                                    )
                                )
                            } else {
                                message = "Thiếu ${abs(n)}"
                                onChange(
                                    StockCheck(
                                        ex.id,
                                        this.name,
                                        this.barcode,
                                        ex.expiryDate,
                                        message
                                    )
                                )
                            }
                            if (mRole == ROLE.manager) {
                                if (newTotal < this.totalQuantity) {
                                    binding.tvAdd.visible()
                                    binding.tvAdd.text =
                                        "Thêm ${this.totalQuantity - newTotal} vào hàng thất thoát"
                                    lossProduct =
                                        ((this.totalQuantity - newTotal) * this.sellPrice)
                                } else {
                                    binding.tvQuantity.text = "SL: $newTotal"
                                    binding.tvAdd.gone()
                                }
                            } else {
                                binding.tvAdd.gone()
                                binding.tvQuantity.text = "SL :${this.totalQuantity + n}"
                            }
                        })
                binding.tvAdd.setOnClickListener {
                    val cal = Calendar.getInstance()
                    LossesDao.insert(
                        Losses(
                            cal.timeInMillis,
                            cal[Calendar.MONTH] + 1,
                            cal[Calendar.YEAR],
                            lossProduct
                        )
                    ) { b ->
                        if (b) {
                            this.totalQuantity = this.expires.sumOf { it.quantity }
                            binding.tvQuantity.text =
                                "Tổng số lượng: ${this.totalQuantity}"
                            binding.tvAdd.text = "Đã thêm vào hàng thất thoát"
                            lossProduct = 0L
                        } else {
                            showToast(mContext, "Đã thêm thất bại")
                        }
                    }
                }
                binding.imvArrow.hide(this.expires.isEmpty())
                binding.tvName.text = this.name
                binding.tvBarcode.text = "Barcode: ${this.barcode}"
                binding.tvQuantity.text = "SL: ${this.totalQuantity}"
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
}

data class StockCheck(
    var expiryId: String,
    var name: String = "",
    var barcode: String = "",
    var date: Long = 0L,
    var note: String = "",
)