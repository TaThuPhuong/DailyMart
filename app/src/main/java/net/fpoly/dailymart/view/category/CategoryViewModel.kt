package net.fpoly.dailymart.view.category

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.Category
import net.fpoly.dailymart.data.model.CategoryParam
import net.fpoly.dailymart.data.model.Response
import net.fpoly.dailymart.data.repository.CategoryRepositoryImpl
import net.fpoly.dailymart.utils.SharedPref

class CategoryViewModel(context: Context) : ViewModel() {

    val repository = CategoryRepositoryImpl()

    var listCategoryRemoteData = listOf<Category>()
    val listCategory = MutableLiveData(listOf<Category>())
    val token = SharedPref.getAccessToken(context)

    val totalCountCategory = listCategory.switchMap {
        MutableLiveData(it.size)
    }

    val showSnackbar = MutableLiveData<String>()
    val isLoading = MutableLiveData(true)

    init {
        getAllCategory()
    }

    private fun getAllCategory() {
        viewModelScope.launch {
            isLoading.postValue(true)
            when (val result = repository.getAllCategory(token)) {
                is Response.Success -> {
                    listCategoryRemoteData = result.data
                    listCategory.postValue(result.data)
                }
                is Response.Error -> {
                    showSnackbar.postValue(GET_ALL_FAILED)
                }
            }
            isLoading.postValue(false)
        }
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
        MoreCategoryPopup(context, category, this).show()
    }

    fun createNewCategory(categoryParam: CategoryParam) {
        viewModelScope.launch {
            isLoading.postValue(true)
            when (repository.addCategory(categoryParam, token)) {
                is Response.Success -> {
                    getAllCategory()
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
                    getAllCategory()
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
                    getAllCategory()
                    showSnackbar.postValue(REMOVE_SUCCESS)
                }
                is Response.Error -> {
                    showSnackbar.postValue(REMOVE_FAILED)
                }
            }
            isLoading.postValue(false)
        }
    }

    companion object {
        const val GET_ALL_FAILED = "Cập nhật danh sách thất bại"
        const val ADD_SUCCESS = "Thêm ngành hàng thành công"
        const val ADD_FAILED = "Thêm ngành hàng thất bại"
        const val EDIT_SUCCESS = "Sửa ngành hàng thành công"
        const val EDIT_FAILED = "Sửa ngành hàng thất bại"
        const val REMOVE_SUCCESS = "Xóa ngành hàng thành công"
        const val REMOVE_FAILED = "Xóa ngành hàng thất bại"
    }
}