package net.fpoly.dailymart.view.work_sheet

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.fpoly.dailymart.data.model.WorkSheet
import net.fpoly.dailymart.firbase.database.WorkSheetDao
import java.util.Calendar

class WorkSheetViewModel : ViewModel() {

    val mListWorkSheet = MutableLiveData<ArrayList<WorkSheet>>(ArrayList())

    fun getListWorkSheet() {
        val cal = Calendar.getInstance()
        WorkSheetDao.getWorkSheet((cal[Calendar.MONTH] + 1), cal[Calendar.YEAR]) {
            mListWorkSheet.value = it
        }
    }
}