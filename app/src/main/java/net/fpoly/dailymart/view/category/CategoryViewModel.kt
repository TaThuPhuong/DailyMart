package net.fpoly.dailymart.view.category

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.Category
import net.fpoly.dailymart.data.model.CategoryAddParam
import net.fpoly.dailymart.data.model.CategoryParam
import net.fpoly.dailymart.data.model.Response
import net.fpoly.dailymart.data.repository.CategoryRepositoryImpl
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref

class CategoryViewModel(context: Context) : ViewModel() {

    val repository = CategoryRepositoryImpl()
    val user = SharedPref.getUser(context)

    var rootCategories = mutableListOf<Category>()
    val categoriesShowing = MutableLiveData(listOf<Category>())
    val token = SharedPref.getAccessToken(context)

    val totalCountCategory = categoriesShowing.switchMap {
        MutableLiveData(it.size)
    }

    val showSnackbar = MutableLiveData<String>()
    val isLoading = MutableLiveData(true)

    private var nowPage = 1
    var totalPage = 1
    var type = true

    init {
        viewModelScope.launch { getCategories() }
    }

    private suspend fun getCategories() {
        isLoading.postValue(true)
        when (val res = repository.getAllCategory(token)) {
            is Response.Success -> {
                totalPage = res.page
                rootCategories.addAll(res.data)
                nowPage++
                showListCategories()
            }

            is Response.Error -> showSnackbar.postValue(res.message)
        }
        isLoading.postValue(false)
    }

    fun showListCategories(list: MutableList<Category> = rootCategories) {
        val filter = list.filter { it.status == type }
        categoriesShowing.postValue(filter)
//        if (filter.size <= 10) loadMorePage()
    }

    private suspend fun reloadCategories() {
        rootCategories.clear()
        nowPage = 1
        getCategories()
    }

    fun clickAddNew(context: Context) {
        AddEditCategoryDialog(context, this, null).show()
    }

    fun clickEdit(context: Context, category: Category) {
        AddEditCategoryDialog(context, this, category).show()
    }

    fun clickRemove(context: Context, category: Category) {
        ConfirmRemoveCategoryDialog(context, category, this).show()
    }

    fun moreOptionCategory(context: Context, category: Category) {
        if (user.role == ROLE.staff) {
            showSnackbar.value = "Nhân viên không được sử dụng chức năng này"
            return
        }
        MoreCategoryPopup(context, category, this).show()
    }

    fun createNewCategory(categoryParam: CategoryAddParam) {
        viewModelScope.launch {
            isLoading.postValue(true)
            when (repository.addCategory(categoryParam, token)) {
                is Response.Success -> {
                    reloadCategories()
                    showSnackbar.postValue(ADD_SUCCESS)
                }

                is Response.Error -> {
                    showSnackbar.postValue(ADD_FAILED)
                }
            }
            isLoading.postValue(false)
        }
    }

    fun editCategory(idCategory: String, categoryParam: CategoryParam) {
        viewModelScope.launch {
            isLoading.postValue(true)
            when (repository.updateCategory(idCategory, categoryParam, token)) {
                is Response.Success -> {
                    reloadCategories()
                    showSnackbar.postValue(EDIT_SUCCESS)
                }

                is Response.Error -> {
                    showSnackbar.postValue(EDIT_FAILED)
                }
            }
            isLoading.postValue(false)
        }
    }

    fun removeCategory(idCategory: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            when (repository.removeCategory(idCategory, token)) {
                is Response.Success -> {
                    reloadCategories()
                    showSnackbar.postValue(REMOVE_SUCCESS)
                }

                is Response.Error -> {
                    showSnackbar.postValue(REMOVE_FAILED)
                }
            }
            isLoading.postValue(false)
        }
    }

//    fun loadMorePage() {
//        if (nowPage > totalPage) return
//        viewModelScope.launch {
//            when (val res = repository.getCategoriesPage(token, nowPage)) {
//                is Response.Success -> {
//                    totalPage = res.page
//                    rootCategories.addAll(res.data)
//                    rootCategories.toMutableSet()
//                    nowPage++
//                    showListCategories()
//                }
//
//                is Response.Error -> showSnackbar.postValue(res.message)
//            }
//        }
//    }

    companion object {
        const val ADD_SUCCESS = "Thêm ngành hàng thành công"
        const val ADD_FAILED = "Thêm ngành hàng thất bại"
        const val EDIT_SUCCESS = "Cập nhật ngành hàng thành công"
        const val EDIT_FAILED = "Cập nhật ngành hàng thất bại"
        const val REMOVE_SUCCESS = "Vô hiệu hóa ngành hàng thành công"
        const val REMOVE_FAILED = "Vô hiệu hóa ngành hàng thất bại"
    }
}