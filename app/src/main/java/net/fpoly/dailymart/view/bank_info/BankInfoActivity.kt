package net.fpoly.dailymart.view.bank_info

import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.BankAccountCheck
import net.fpoly.dailymart.data.model.BankInfo
import net.fpoly.dailymart.data.model.BankModel
import net.fpoly.dailymart.databinding.ActivityBankInfoBinding
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.extension.view_extention.getTextOnChange
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.extension.view_extention.setVisibility
import net.fpoly.dailymart.extension.view_extention.visible
import net.fpoly.dailymart.utils.SharedPref

class BankInfoActivity : BaseActivity<ActivityBankInfoBinding>(ActivityBankInfoBinding::inflate) {

    private val TAG = "YingMing"
    private val viewModel: BankInfoViewModel by viewModels { AppViewModelFactory }

    private lateinit var mBankAdapter: BankAdapter
    private var mListBank: List<BankModel> = ArrayList()
    private var mAccountNumber: String = ""
    private var mBin: Int = 0
    private var mBankInfo: BankInfo = BankInfo()
    private var isShowListBank = false
    private var isGetListBankSuccess = false

    override fun setupData() {
        initBankRecycleView()
        viewModel.getListBank()
        setBankInfo(SharedPref.getBankInfo(this))
        binding.tvCheckAccount.setOnClickListener {
            viewModel.checkBankAccount(BankAccountCheck(mBin, mAccountNumber))
        }
        binding.edAccountNumber.getTextOnChange {
            mAccountNumber = it
            if (it.isNotEmpty()) binding.tvCheckAccount.visible() else binding.tvCheckAccount.gone()
        }

        binding.edBankName.getTextOnChange {
            setSearchBank(it)
        }
        binding.tvSave.setOnClickListener {
            viewModel.saveBankInfo(mBankInfo)
            SharedPref.setBankInfo(this, mBankInfo)
        }

        binding.imvDrop.setOnClickListener {
            if (isGetListBankSuccess) {
                isShowListBank = !isShowListBank
                binding.rcvListBank.setVisibility(!isShowListBank)
                if (isShowListBank) {
                    binding.imvDrop.rotation = 0f
                } else {
                    binding.imvDrop.rotation = 180f
                }
            }
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun setupObserver() {
        viewModel.mListBank.observe(this) {
            mBankAdapter.setData(it)
            mListBank = it
            if (it.isNotEmpty()) isGetListBankSuccess = true
        }
        viewModel.mBankAccountRequest.observe(this) {
            if (it.code == "00") {
                it.data?.let { data ->
                    binding.tvAccountName.text = data.accountName
                    mBankInfo.accountName = data.accountName.replace(" ", "%20")
                    mBankInfo.accountNumber = mAccountNumber
                    binding.tvCheckAccount.gone()
                }
            } else {
                binding.tvAccountName.text = it.desc
            }
        }
        viewModel.message.observe(this) {
            if (it.isNotEmpty()) {
                showToast(this, it)
            }
        }
        viewModel.saveSuccess.observe(this) {
            if (it) finish()
        }
    }

    private fun initBankRecycleView() {
        mBankAdapter = BankAdapter(this, mListBank) {
            Glide.with(this).load(it.logo).into(binding.imvLogo)
            binding.edBankName.setText(it.name)
            mBin = it.bin.toInt()
            binding.rcvListBank.gone()
            mBankInfo.bankId = it.bin
            mBankInfo.bankName = it.name
            mBankInfo.logo = it.logo
        }
        binding.rcvListBank.adapter = mBankAdapter
    }

    private fun setSearchBank(value: String) {
        binding.rcvListBank.visible()
        val listFilter = mListBank.filter { it.name.contains(value, true) }
        if (value.isNotEmpty()) mBankAdapter.setData(listFilter)
        if (value.isEmpty()) mBankAdapter.setData(mListBank)
    }

    private fun setBankInfo(bankInfo: BankInfo) {
        binding.rcvListBank.gone()
        binding.tvCheckAccount.gone()
        Glide.with(this).load(bankInfo.logo).placeholder(R.drawable.img_default)
            .error(R.drawable.img_default).into(binding.imvLogo)
        binding.edBankName.setText(bankInfo.bankName)
        binding.edAccountNumber.setText(bankInfo.accountNumber)
        binding.tvAccountName.text = bankInfo.accountName.replace("%20", " ")
    }
}