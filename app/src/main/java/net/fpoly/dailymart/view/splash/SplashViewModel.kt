package net.fpoly.dailymart.view.splash

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.Response
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.repository.NotificationRepository
import net.fpoly.dailymart.repository.UserRepository
import net.fpoly.dailymart.utils.SharedPref
import java.util.*

class SplashViewModel(
    private val app: Application,
    private val userRepo: UserRepository,
    private val notificationRepo: NotificationRepository,
) :
    ViewModel() {
    private var timer: Timer? = null

    val loadingSplash = MutableLiveData<Int>()

    fun loadSplash() {
        val handler = Handler(Looper.getMainLooper())
        val progress = intArrayOf(0)

        @SuppressLint("SetTextI18n")
        val update = Runnable {
            if (progress[0] < 200) {
                progress[0]++
                loadingSplash.value = progress[0]
            } else {
                timer?.cancel()
            }
        }
        timer?.cancel()
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 100, 10)
    }

    fun checkActive(context: Context, onCheck: (Boolean) -> Unit) {
        val user = SharedPref.getUser(context)
        val token = SharedPref.getAccessToken(context)
        if (user.id.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                when (val res = userRepo.getUserById(token, user.id)) {
                    is Response.Error -> {
                        onCheck(false)
                        reset(false)
                    }
                    is Response.Success -> {
                        onCheck(res.data.status)
                        reset(res.data.status)
                    }
                }
            }
        }
    }

    private fun reset(b: Boolean) {
        if (!b) {
            SharedPref.setAccessToken(app, "")
            SharedPref.insertUser(app, User())
            notificationRepo.deleteSeenNotification()
        }
    }
}