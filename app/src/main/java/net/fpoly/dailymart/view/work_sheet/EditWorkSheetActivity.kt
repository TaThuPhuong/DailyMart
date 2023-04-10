package net.fpoly.dailymart.view.work_sheet

import android.annotation.SuppressLint
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.UserRes
import net.fpoly.dailymart.data.model.WorkSheet
import net.fpoly.dailymart.databinding.ActivityEditWorkSheetBinding
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.utils.Constant
import net.fpoly.dailymart.view.work_sheet.adapter.SheetEditAdapter
import java.text.SimpleDateFormat

class EditWorkSheetActivity :
    BaseActivity<ActivityEditWorkSheetBinding>(ActivityEditWorkSheetBinding::inflate) {

    private var mWorkSheet: WorkSheet? = null

    private var mListUser: List<UserRes> = ArrayList()

    private val viewModel: EditWorkSheetViewModel by viewModels { AppViewModelFactory }

    private lateinit var mSheetAdapter1: SheetEditAdapter
    private lateinit var mSheetAdapter2: SheetEditAdapter
    private lateinit var mSheetAdapter3: SheetEditAdapter

    @SuppressLint("SetTextI18n", "SimpleDateFormat", "NotifyDataSetChanged")
    override fun setupData() {
        mWorkSheet = intent.getSerializableExtra(Constant.WORK_SHEET) as WorkSheet?
        mWorkSheet?.let { workSheet ->
            binding.tvTitle.text =
                "Lịch làm ${SimpleDateFormat("dd/MM/yyyy").format(workSheet.time)}"
            setData(workSheet)
            binding.tvAddShift1.setOnClickListener {
                ChoseUserDialog(this, mListUser) {
                    workSheet.shift1.add(it.name)
                    mSheetAdapter1.notifyDataSetChanged()
                }.show()
            }
            binding.tvAddShift2.setOnClickListener {
                ChoseUserDialog(this, mListUser) {
                    workSheet.shift2.add(it.name)
                    mSheetAdapter2.notifyDataSetChanged()
                }.show()
            }
            binding.tvAddShift3.setOnClickListener {
                ChoseUserDialog(this, mListUser) {
                    workSheet.shift3.add(it.name)
                    mSheetAdapter3.notifyDataSetChanged()
                }.show()
            }
        }
        binding.tvSave.setOnClickListener {
            mWorkSheet?.let { sheet ->
                viewModel.saveWorkSheet(sheet)
            }
        }
        binding.imvBack.setOnClickListener { finish() }
    }

    override fun setupObserver() {
        viewModel.mListUser.observe(this) {
            mListUser = it
        }
        viewModel.saveSuccess.observe(this) {
            if (it) {
                showToast(this, "Thêm lịch thành công")
                finish()
            } else {
                showToast(this, "Lưu thất bại")
            }
        }
    }

    private fun setData(workSheet: WorkSheet) {
        mSheetAdapter1 = SheetEditAdapter(workSheet.shift1) {
            workSheet.shift1.remove(it)
        }
        mSheetAdapter2 = SheetEditAdapter(workSheet.shift2) {
            workSheet.shift2.remove(it)
        }
        mSheetAdapter3 = SheetEditAdapter(workSheet.shift3) {
            workSheet.shift3.remove(it)
        }
        binding.rcvCa1.adapter = mSheetAdapter1
        binding.rcvCa2.adapter = mSheetAdapter2
        binding.rcvCa3.adapter = mSheetAdapter3
    }
}