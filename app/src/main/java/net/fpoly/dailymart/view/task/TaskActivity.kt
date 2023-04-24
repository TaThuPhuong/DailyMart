package net.fpoly.dailymart.view.task

import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.databinding.ActivityTaskBinding
import net.fpoly.dailymart.extension.view_extention.*
import net.fpoly.dailymart.view.task.adapter.ViewPagerAdapter
import net.fpoly.dailymart.view.task.add_new.AddTaskActivity

class TaskActivity : BaseActivity<ActivityTaskBinding>(ActivityTaskBinding::inflate),
    View.OnClickListener {

    private val TAG = "YingMing"

    private val viewModel: TaskViewModel by viewModels { AppViewModelFactory }

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun setOnClickListener() {
        binding.btnBack.setOnClickListener(this)
        binding.imvAdd.setOnClickListener(this)
        binding.imvClear.setOnClickListener(this)
    }

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setSearchTask()
        val listTitle = Task.listTitle

        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle, Task.listTitle)
        binding.viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = listTitle[position]
        }.attach()

        binding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.getAllTask(binding.viewPager.currentItem)
                binding.pbLoading.visible()
                binding.viewPager.gone()
            }
        })
        binding.layoutRefresh.setOnRefreshListener {
            viewModel.getAllTask(binding.viewPager.currentItem)
        }
    }

    override fun setupObserver() {
        viewModel.listTask.observe(this) {
            binding.pbLoading.gone()
            binding.viewPager.visible()
            binding.layoutRefresh.isRefreshing = false
        }
    }


    override fun onClick(v: View?) {
        when (v) {
            binding.btnBack -> finish()
            binding.imvAdd -> {
                startActivity(Intent(this, AddTaskActivity::class.java))
            }
            binding.imvClear -> {
                binding.edSearch.setText("")
                binding.imvClear.gone()
                binding.tvNoData.gone()
            }
        }
    }

    private fun setSearchTask() {
        binding.edSearch.getTextOnChange {
            viewModel.textSearch.postValue(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllTask(binding.viewPager.currentItem)
    }
}