package net.fpoly.dailymart.view.message

import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivityMessageBinding

class MessageActivity : BaseActivity<ActivityMessageBinding>(ActivityMessageBinding::inflate) {

    private val viewModel: MessageViewModel by viewModels { AppViewModelFactory }

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun setupObserver() {

    }
}