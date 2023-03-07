package net.fpoly.dailymart.view.tab.receipt

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseFragment
import net.fpoly.dailymart.databinding.ReceiptFragmentBinding
import net.fpoly.dailymart.view.pay.PayActivity

class ReceiptFragment : BaseFragment<ReceiptFragmentBinding>(ReceiptFragmentBinding::inflate),
    View.OnClickListener {

    private val viewModel: ReceiptViewModel by viewModels { AppViewModelFactory }

    override fun setOnClickListener() {
        binding.imvAdd.setOnClickListener(this)
    }

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun setupObserver() {

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.imvAdd -> startActivity(Intent(mContext, PayActivity::class.java))
        }
    }
}