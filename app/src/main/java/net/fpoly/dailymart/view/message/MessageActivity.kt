package net.fpoly.dailymart.view.message

import android.annotation.SuppressLint
import android.graphics.Rect
import android.util.Log
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.Message
import net.fpoly.dailymart.databinding.ActivityMessageBinding
import net.fpoly.dailymart.extension.view_extention.getTextOnChange
import net.fpoly.dailymart.extension.view_extention.setVisibility
import net.fpoly.dailymart.firbase.real_time.MessageDao
import net.fpoly.dailymart.utils.Constant
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref

class MessageActivity : BaseActivity<ActivityMessageBinding>(ActivityMessageBinding::inflate) {

    private val TAG = "YingMing"
    private val viewModel: MessageViewModel by viewModels { AppViewModelFactory }

    private var mListMessage: List<Message> = ArrayList()
    private lateinit var mMessageAdapter: MessageAdapter

    private var mMessage: String? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        mMessage = intent.getStringExtra(Constant.MESSAGE)
        mMessage?.let { binding.edMessage.setText(it) }
        binding.imvBack.setOnClickListener { finish() }

        binding.edMessage.getTextOnChange {
            viewModel.onMessageChange(it)
            Log.e(TAG, "setupData: $it")
        }
        viewModel.getAllMessage()
        initRecycleMessage()

        binding.imvSend.setOnClickListener {
            checkKeyBoard()
            viewModel.onSendMessage {
                binding.edMessage.setText("")
            }
            val lastPosition = mMessageAdapter.itemCount - 1
            if (lastPosition != -1) {
                binding.rcvMessage.smoothScrollToPosition(lastPosition)
            }
        }
        binding.imvDelete.setOnClickListener {
            DeleteMessageDialog(this) {
                viewModel.onDelete()
            }.show()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObserver() {
        viewModel.listMessage.observe(this) {
            if (it.isNotEmpty()) {
                mMessageAdapter.setData(it)
                binding.rcvMessage.scrollToPosition(it.size - 1)
            }
        }
    }

    private fun initRecycleMessage() {
        val user = SharedPref.getUser(this)
        binding.imvDelete.setVisibility(user.role != ROLE.staff)
        mMessageAdapter = MessageAdapter(this, mListMessage, user.id)
        binding.rcvMessage.adapter = mMessageAdapter
    }

    private fun checkKeyBoard() {
        val root = binding.root
        root.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val r = Rect()
                root.getWindowVisibleDisplayFrame(r)
                val height = root.rootView.height - r.height()
                if (height > 0.25 * root.rootView.height) {
                    if (mListMessage.isNotEmpty()) {
                        binding.rcvMessage.scrollToPosition(mListMessage.size - 1)
                        root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
            }

        })
    }
}