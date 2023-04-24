package net.fpoly.dailymart.view.stock

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.Message
import net.fpoly.dailymart.data.model.ReportCheckStock
import net.fpoly.dailymart.data.model.dataToString
import net.fpoly.dailymart.databinding.ViewCreateReportBinding
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.extension.time_extention.date2String
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.firbase.database.ReportStockDao
import net.fpoly.dailymart.firbase.real_time.MessageDao
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.utils.sendNotification
import net.fpoly.dailymart.view.message.MessageViewModel
import net.fpoly.dailymart.view.stock.adapter.StockReportAdapter

class CreateReportView : RelativeLayout {

    private val mContext: Context
    private var mListStockCheck: List<StockCheck> = ArrayList()
    private lateinit var mStockReportAdapter: StockReportAdapter
    private lateinit var binding: ViewCreateReportBinding

    constructor(context: Context) : super(context) {
        mContext = context
        initView()
        initRecycleView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mContext = context
        initView()
        initRecycleView()
    }

    private fun initView() {
        binding = ViewCreateReportBinding.inflate(LayoutInflater.from(context), this, true)
        binding.imvClose.setOnClickListener {
            gone()
        }
        binding.tvSendReport.setOnClickListener {
            ReportStockDao.insertReport(
                ReportCheckStock(
                    System.currentTimeMillis(),
                    mListStockCheck.dataToString()
                )
            ) {
                if (it) {
                    showToast(mContext, "Gửi thành công")
                    sendMessage(mListStockCheck)
                    mListStockCheck = ArrayList()
                    gone()
                } else {
                    showToast(mContext, "Gửi thất bại")
                }
            }
        }
    }

    private fun initRecycleView() {
        mStockReportAdapter = StockReportAdapter(mListStockCheck)
        binding.rcvReportStock.adapter = mStockReportAdapter
    }

    @SuppressLint("SetTextI18n")
    fun setData(list: List<StockCheck>, isStaff: Boolean = true, time: Long = 0) {
        mListStockCheck = ArrayList()
        mListStockCheck = list
        mStockReportAdapter.setData(list)
        if (!isStaff) {
            binding.tvTitle.text = "Báo cáo kiểm tồn ngày ${time.date2String()}"
            binding.tvSendReport.gone()
        }
    }

    private fun sendMessage(list: List<StockCheck>) {
        val user = SharedPref.getUser(mContext)
        var message = ""
        list.forEach {
            message += getMessage(it)
        }
        val mes = Message(user.id, user.name, user.avatar, System.currentTimeMillis(), message)
        MessageDao.senMessage(message = mes) {
            CoroutineScope(Dispatchers.IO).launch {
                sendNotification(
                    "${user.name} đã gửi 1 báo cáo kiểm tồn",
                    "Báo cáo kiểm tồn ngày ${System.currentTimeMillis().date2String()}",
                    "",
                    user.id,
                    MessageViewModel.TOPIC
                )
            }
        }
    }

    private fun getMessage(stockCheck: StockCheck): String {
        return "${stockCheck.name}\nBarcode: ${stockCheck.barcode}\nHSD: ${stockCheck.date.date2String()}\n${stockCheck.note}\n --------------------\n"
    }
}