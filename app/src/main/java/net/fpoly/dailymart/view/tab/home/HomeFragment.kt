package net.fpoly.dailymart.view.tab.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseFragment
import net.fpoly.dailymart.data.model.RecentNotification
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.databinding.HomeFragmentBinding
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.extension.view_extention.setMarginsStatusBar
import net.fpoly.dailymart.extension.view_extention.setVisibility
import net.fpoly.dailymart.utils.Constant
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.check_date.CheckDateActivity
import net.fpoly.dailymart.view.pay.AddInvoiceExportActivity
import net.fpoly.dailymart.view.message.MessageActivity
import net.fpoly.dailymart.view.profile.ProfileActivity
import net.fpoly.dailymart.view.report.ReportActivity
import net.fpoly.dailymart.view.stock.StockActivity
import net.fpoly.dailymart.view.tab.home.adapter.NotificationAdapter
import net.fpoly.dailymart.view.tab.home.adapter.TaskRecentAdapter
import net.fpoly.dailymart.view.task.TaskActivity
import net.fpoly.dailymart.view.task.task_detail.TaskDetailActivity
import net.fpoly.dailymart.view.work_sheet.WorkSheetActivity

class HomeFragment : BaseFragment<HomeFragmentBinding>(HomeFragmentBinding::inflate),
    View.OnClickListener {

    private val TAG = "YingMing"
    private val viewModel: HomeViewModel by viewModels { AppViewModelFactory }

    private var mUser: User? = null

    private var mListNotification: List<RecentNotification> = ArrayList()
    private lateinit var mNotificationAdapter: NotificationAdapter

    private var mListTask: List<Task> = ArrayList()
    private lateinit var mTaskRecentAdapter: TaskRecentAdapter

    private var mReloadData: Runnable? = null
    private var isRunning = false
    private val mHandlerLoopTime = Handler(Looper.myLooper()!!)
    override fun setOnClickListener() {
        binding.imvAvatarToolbar.setOnClickListener(this)
        binding.imvNotification.setOnClickListener(this)
        binding.layoutReport.setOnClickListener(this)
        binding.imvCheckDate.setOnClickListener(this)
        binding.imvReport.setOnClickListener(this)
        binding.imvWorkSheet.setOnClickListener(this)
        binding.imvStock.setOnClickListener(this)
        binding.imvTask.setOnClickListener(this)
        binding.imvPay.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    override fun setupData() {
        binding.layoutToolbar.setMarginsStatusBar(mContext)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        mUser = SharedPref.getUser(mContext)
        mUser?.let {
            Glide.with(mContext).load(it.avatar).placeholder(R.drawable.img_avatar_default)
                .into(binding.imvAvatarToolbar)
            binding.tvName.text = "Chào, ${it.name}"
            initTaskRecent()
            binding.imvReport.setVisibility(it.role == ROLE.manager)
            binding.imvMessage.setVisibility(it.role == ROLE.staff)
        }
        initNotification()
        viewModel.getAllNotification()
        viewModel.getAllTask()
        viewModel.getInvoiceToday()
        mReloadData = Runnable {
            viewModel.getAllTask()
            viewModel.getAllNotification()
            viewModel.getInvoiceToday()
            if (isRunning) {
                autoReload()
            }
        }
        autoReload()
    }

    private fun autoReload() {
        isRunning = true
        mReloadData?.let {
            mHandlerLoopTime.postDelayed(it, 30000)
        }
    }

    override fun setupObserver() {
        viewModel.listNotification.observe(this) {
            mListNotification = it
            mNotificationAdapter.setData(it)
            binding.tvNumNotification.text = it.size.toString()
        }
        viewModel.listTask.observe(this) {
            mListTask = it
            mTaskRecentAdapter.setData(it)
        }
        viewModel.getTaskSuccess.observe(this) {
            if (it) binding.pbLoading.gone()
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.imvAvatarToolbar -> openActivity(ProfileActivity::class.java)
            binding.imvNotification -> {}
            binding.layoutReport, binding.imvReport -> if (mUser!!.role == ROLE.manager) openActivity(
                ReportActivity::class.java
            )
            binding.imvCheckDate -> openActivity(CheckDateActivity::class.java)
            binding.imvWorkSheet -> openActivity(WorkSheetActivity::class.java)
            binding.imvStock -> openActivity(StockActivity::class.java)
            binding.imvTask -> openActivity(TaskActivity::class.java)
            binding.imvPay -> openActivity(AddInvoiceExportActivity::class.java)
            binding.imvMessage -> openActivity(MessageActivity::class.java)
        }
    }

    private fun openActivity(c: Class<*>) {
        startActivity(Intent(mContext, c))
    }

    private fun initTaskRecent() {
        mTaskRecentAdapter = TaskRecentAdapter(mUser!!, mListTask) {
            val intent = Intent(mContext, TaskDetailActivity::class.java)
            intent.putExtra(Constant.TASK, it)
            startActivity(intent)
        }
        binding.rcvRecentTask.adapter = mTaskRecentAdapter
    }

    private fun initNotification() {
        mNotificationAdapter = NotificationAdapter(mListNotification) {
            if (it.isNotEmpty()) {
                val intent = Intent(mContext, TaskDetailActivity::class.java)
                intent.putExtra(Constant.TASK_ID, it)
                startActivity(intent)
                viewModel.onClickNotification()
            } else {
                openActivity(MessageActivity::class.java)
                viewModel.onClickNotification()
            }
        }
        binding.rcvNotification.adapter = mNotificationAdapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllNotification()
        viewModel.getAllTask()
    }

    override fun onDestroy() {
        isRunning = false
        mReloadData?.let {
            mHandlerLoopTime.removeCallbacks(it)
        }
        super.onDestroy()
    }
}