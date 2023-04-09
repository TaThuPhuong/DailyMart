package net.fpoly.dailymart.view.work_sheet

import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
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
    private val mListData: ArrayList<Sheet> = ArrayList()
    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        dataFake()
        initSheet()
    }

    override fun setupObserver() {

    }

    private fun initSheet() {
        val calender = Calendar.getInstance()
        val numDay = calender.getActualMaximum(Calendar.DAY_OF_MONTH)
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

    private fun getSheetByDay(day: Int): Sheet {
        for (sheet in mListData) {
            if (sheet.day == day) {
                return sheet
            }
        }
        return Sheet()
    }

    private fun dataFake() {
        for (i in 0 until 30) {
            val calender = Calendar.getInstance()
            calender.set(Calendar.DATE, i + 1)
            mListData.add(
                Sheet(
                    calender.timeInMillis,
                    i + 1,
                    arrayListOf("name 1", "nam 2", "name 3"),
                    arrayListOf("name 4", "nam 5", "name 6"),
                    arrayListOf("name 7", "nam 8", "name 9")
                )
            )
        }
    }
}

data class Sheet(
    val time: Long = 0,
    val day: Int = 0,
    val shift1: ArrayList<String>? = null,
    val shift2: ArrayList<String>? = null,
    val shift3: ArrayList<String>? = null,
)