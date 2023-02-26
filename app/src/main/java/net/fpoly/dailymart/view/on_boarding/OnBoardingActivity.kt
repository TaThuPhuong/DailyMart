package net.fpoly.dailymart.view.on_boarding

import android.content.Intent
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.widget.ViewPager2
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivityOnBoardingBinding
import net.fpoly.dailymart.view.login.LoginActivity
import net.fpoly.dailymart.view.register.RegisterActivity

class OnBoardingActivity :
    BaseActivity<ActivityOnBoardingBinding>(ActivityOnBoardingBinding::inflate) {

    private lateinit var mIntroAdapter: OnBoardingAdapter

    override fun setupData() {
        initViewPager(supportFragmentManager, lifecycle)

        binding.btnStartNow.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun setupObserver() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })
    }

    private fun initViewPager(fragmentManager: FragmentManager, lifecycle: Lifecycle) {
        mIntroAdapter = OnBoardingAdapter(fragmentManager, lifecycle)
        binding.viewPager.adapter = mIntroAdapter
        binding.indicator.setViewPager(binding.viewPager)
    }

    override fun onBackPressed() {
        if (binding.viewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            binding.viewPager.setCurrentItem(binding.viewPager.currentItem - 1, true)
        }
    }
}