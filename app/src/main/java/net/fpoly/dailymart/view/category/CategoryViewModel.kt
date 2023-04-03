package net.fpoly.dailymart.view.category

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.param.CategoryParam
import net.fpoly.dailymart.data.model.param.CategoryParamList
import net.fpoly.dailymart.repository.CategoryRepositoryss
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryViewModel() : ViewModel() {
    private val TAG = "CategoryVM"
    private val _listCategory = MutableLiveData<CategoryParamList>()
    val listCategory: LiveData<CategoryParamList> = _listCategory

    private val _newCategory = MutableLiveData<CategoryParam>()
    private val newCategory: LiveData<CategoryParam> = _newCategory

    private val categoryRepository = CategoryRepositoryss()

    fun getAllCategory(token: String){
        viewModelScope.launch {
            try {
                categoryRepository.getAllCategory(token).enqueue(object : Callback<CategoryParamList> {
                    override fun onResponse(
                        call: Call<CategoryParamList>,
                        response: Response<CategoryParamList>
                    ) {
                        _listCategory.value = response.body()
                        Log.d(TAG, "onResponse: ${response.body()}")
                    }
                    override fun onFailure(call: Call<CategoryParamList>, t: Throwable) {
                    }
                })
            }catch (e:Exception){
                Log.e(TAG, "getAll: error: $e")
            }
            }
        }

    fun insertCategory(token: String,category : CategoryParam){
        viewModelScope.launch {
            try {
                categoryRepository.insertCategory(token , category).enqueue(object : Callback<CategoryParam>{
                    override fun onResponse(
                        call: Call<CategoryParam>,
                        response: Response<CategoryParam>
                    ) {
                        Log.e(TAG, "onRespons: add success: ${response}")
                        Log.e(TAG, "onRespons: add success: ${call}")
                    }
                    override fun onFailure(call: Call<CategoryParam>, t: Throwable) {

                    }

                })

//                categoryRepository.insertCategory(token, category).enqueue(object : Callback<CategoryParam> {
//                    override fun onResponse(
//                        call: Call<CategoryParam>,
//                        response: Response<CategoryParam>
//                    ) {
//                        if (response.body() != null) {
//                            Log.d(TAG, "onRespons: add success: ${response.body()}")
//                        } else {
//                            Log.e(TAG, "onResponse: $response", )
//                        }
//                    }
//
//                    override fun onFailure(call: Call<CategoryParam>, t: Throwable) {
//                        Log.e(TAG, "onFailure: add failed: $t")
//                    }
//                })
            }catch (e:Exception){
                Log.e(TAG, "insertCategory: error: $e")
            }
        }
    }
    }

//    fun searchCategoryName(nameSearch : String){
//        viewModelScope.launch {
//            categoryRepository.searchCategory("%$nameSearch%").collect{
//                categoryRepository.searchCategory(nameSearch)
//
//            }
//        }
//    }
//    fun insertCategory(category: Category) {
//        viewModelScope.launch {
//            categoryRepository.insertCategory(category)
//        }
//    }
//    fun deleteCategory(category: Category) {
//        viewModelScope.launch {
//            categoryRepository.deleteCategory(category)
//        }
//    }
//}