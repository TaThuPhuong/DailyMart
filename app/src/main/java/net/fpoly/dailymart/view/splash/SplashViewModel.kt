package net.fpoly.dailymart.view.splash

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class SplashViewModel : ViewModel() {
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
}