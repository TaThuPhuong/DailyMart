package net.fpoly.dailymart.view.work_sheet

import android.util.Log
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.WorkSheet
import net.fpoly.dailymart.databinding.ActivityWorkSheetBinding
import java.util.*
import kotlin.collections.ArrayList

class WorkSheetActivity :
    BaseActivity<ActivityWorkSheetBinding>(ActivityWorkSheetBinding::inflate) {

    private val TAG = "YingMing"

    private val viewModel: WorkSheetViewModel by viewModels { AppViewModelFactory }

    private lateinit var mAdapter: ViewPagerAdapter
    private val mListFragment: ArrayList<SheetDayFragment> = ArrayList()
    private val mListTitle: ArrayList<String> = ArrayList()
    private var mListData: ArrayList<WorkSheet> = ArrayList()

    private val calender = Calendar.getInstance()
    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.getListWorkSheet()
        binding.imvBack.setOnClickListener { finish() }
    }

    override fun setupObserver() {
        viewModel.mListWorkSheet.observe(this) {
            Log.e(TAG, "mListData: $it")
            mListData = it
            initSheet()
        }
    }

    private fun initSheet() {
        val numDay = calender.getActualMaximum(Calendar.DAY_OF_MONTH)
        mListFragment.clear()
        mListTitle.clear()
        for (i in 0 until numDay) {
            val sheet = getSheetByDay(i + 1)
            val frag = SheetDayFragment(sheet)
            mListFragment.add(frag)
            mListTitle.add("${(i + 1)}/${calender[Calendar.MONTH] + 1}")
        }
        mAdapter = ViewPagerAdapter(
            supportFragmentManager,
            lifecycle,
            mListFragment
        )
        binding.viewPager.adapter = mAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = mListTitle[position]
        }.attach()

        binding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

            }
        })
    }

    private fun getSheetByDay(day: Int): WorkSheet {
        val calender = Calendar.getInstance()
        val year = calender[Calendar.YEAR]
        val month = calender[Calendar.MONTH] + 1
        calender.set(Calendar.DATE, day)
        for (sheet in mListData) {
            if (sheet.day == day && sheet.year == year && sheet.month == month) {
                return sheet
            }
        }
        return WorkSheet(
            calender.timeInMillis,
            day,
            calender[Calendar.MONTH] + 1,
            calender[Calendar.YEAR]
        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.getListWorkSheet()
    }
}