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
import net.fpoly.dailymart.repository.UserRepository
import net.fpoly.dailymart.utils.SharedPref
import java.util.*

class SplashViewModel(private val app: Application, private val userRepo: UserRepository) :
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

//    fun checkActive(context: Context): Boolean {
//        val user = SharedPref.getUser(context)
//        if (user.id.isNotEmpty()) {
//            viewModelScope.launch(Dispatchers.IO){
//                userRepo.get
//            }
//        }
//    }
}