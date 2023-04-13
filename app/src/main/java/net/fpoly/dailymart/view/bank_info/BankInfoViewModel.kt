package net.fpoly.dailymart.view.bank_info

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.api.BankApiInstance
import net.fpoly.dailymart.data.model.BankAccountCheck
import net.fpoly.dailymart.data.model.BankAccountRequest
import net.fpoly.dailymart.data.model.BankInfo
import net.fpoly.dailymart.data.model.BankModel
import net.fpoly.dailymart.data.model.root.BankRoot
import net.fpoly.dailymart.firbase.database.BankDao
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BankInfoViewModel(private val app: Application) : ViewModel() {

    private val TAG = "YingMing"
    val mListBank = MutableLiveData<ArrayList<BankModel>>()
    val mBankAccountRequest = MutableLiveData<BankAccountRequest>()
    val message = MutableLiveData("")

    fun getListBank() {
        viewModelScope.launch(Dispatchers.IO) {
            val apiBank = BankApiInstance.apiBank
            apiBank.getListBank().enqueue(object : Callback<BankRoot> {
                override fun onResponse(
                    call: Call<BankRoot>,
                    response: Response<BankRoot>,
                ) {
                    if (response.isSuccessful) {
                        mListBank.postValue(response.body()?.data)
                    }
                }

                override fun onFailure(call: Call<BankRoot>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        }
    }

    fun checkBankAccount(accountCheck: BankAccountCheck) {
        viewModelScope.launch(Dispatchers.IO) {
            val apiBank = BankApiInstance.apiBank
            apiBank.checkAccount(accountCheck)
                .enqueue(object : Callback<BankAccountRequest> {
                    override fun onResponse(
                        call: Call<BankAccountRequest>,
                        response: Response<BankAccountRequest>,
                    ) {
                        if (response.isSuccessful) {
                            mBankAccountRequest.postValue(response.body())
                        }
                    }

                    override fun onFailure(call: Call<BankAccountRequest>, t: Throwable) {
                        Log.e(TAG, "onFailure: ${t.message}")
                    }
                })
        }
    }

    fun saveBankInfo(bankInfo: BankInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            BankDao.insertBankInfo(bankInfo) {
                message.postValue(it)
            }
        }
    }
}