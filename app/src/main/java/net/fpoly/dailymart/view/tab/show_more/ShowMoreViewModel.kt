package net.fpoly.dailymart.view.tab.show_more

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref

class ShowMoreViewModel(private val app: Application) : ViewModel() {

    private val mUser = SharedPref.getUser(app)
    val role = MutableLiveData(false)

    init {
        role.value = mUser.role != ROLE.staff
    }
}