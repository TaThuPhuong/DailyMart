package net.fpoly.dailymart.view.work_sheet.edit_work_sheet

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.UserRes
import net.fpoly.dailymart.data.model.WorkSheet
import net.fpoly.dailymart.firbase.database.WorkSheetDao
import net.fpoly.dailymart.repository.UserRepository
import net.fpoly.dailymart.utils.SharedPref
import java.text.SimpleDateFormat

class EditWorkSheetViewModel(private val app: Application, private val userRepo: UserRepository) :
    ViewModel() {

    private val TAG = "YingMing"
    val mListUser = MutableLiveData<List<UserRes>>(ArrayList())
    val saveSuccess = MutableLiveData(false)
    private val mToken = SharedPref.getAccessToken(app)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val res = userRepo.getAllUser2(mToken)
            if (res.isSuccess()) {
                res.data?.let {
                    mListUser.postValue(it)
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun saveWorkSheet(sheet: WorkSheet) {
        Log.e(TAG, "sheet: $sheet")
        WorkSheetDao.insertWorkSheet(sheet, SimpleDateFormat("dd_MM_yyyy").format(sheet.time)) {
            saveSuccess.postValue(it)
        }
    }
}