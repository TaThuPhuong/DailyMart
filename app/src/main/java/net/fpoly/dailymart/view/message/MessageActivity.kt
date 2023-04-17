package net.fpoly.dailymart.view.message

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.data.model.Message
import net.fpoly.dailymart.databinding.ActivityMessageBinding
import net.fpoly.dailymart.extension.view_extention.getTextOnChange
import net.fpoly.dailymart.extension.view_extention.setVisibility
import net.fpoly.dailymart.utils.Constant
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.splash.SplashActivity

class MessageActivity : AppCompatActivity() {

    private val TAG = "YingMing"
    private val viewModel: MessageViewModel by viewModels { AppViewModelFactory }

    private var mListMessage: List<Message> = ArrayList()
    private lateinit var mMessageAdapter: MessageAdapter

    private lateinit var binding: ActivityMessageBinding

    private var mMessage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val user = SharedPref.getUser(this)
        if (user.id == "") {
            startActivity(Intent(this, SplashActivity::class.java))
            finishAffinity()
        }
        WindowCompat.getInsetsController(window, binding.root).let {
            it.isAppearanceLightNavigationBars = false
            it.isAppearanceLightStatusBars = false
        }
        setupData()
        setupObserver()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        mMessage = intent.getStringExtra(Constant.MESSAGE)
        mMessage?.let { binding.edMessage.setText(it) }
        binding.btnBack.setOnClickListener { finish() }

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
    fun setupObserver() {
        viewModel.listMessage.observe(this) {
            if (it.isNotEmpty()) {
                mMessageAdapter.setData(it)
                mListMessage = it
            }
        }
        viewModel.isGetDone.observe(this) {
            Log.e(TAG, "setupObserver: $it -- ${mListMessage.isNotEmpty()}")
            if (it && mListMessage.isNotEmpty()) {
                Log.e(TAG, "mListMessage size: ${mListMessage.size}")
                binding.rcvMessage.scrollToPosition(mListMessage.size - 1)
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