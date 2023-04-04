package net.fpoly.dailymart.view.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.Category

class CategoryViewModel() : ViewModel() {
    private val _listCategory = MutableLiveData<List<Category>>(ArrayList())
    val listCategory: LiveData<List<Category>> = _listCategory

    fun getAllCategory(){

    }

//    fun searchCategoryName(nameSearch : String){
//        viewModelScope.launch {
//            categoryRepository.searchCategory("%$nameSearch%").collect{
//                categoryRepository.searchCategory(nameSearch)
//
//            }
//        }
//    }

    fun insertCategory(category: Category) {

    }
    fun deleteCategory(category: Category) {

    }
}