package net.fpoly.dailymart.view.category
import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.Category
import net.fpoly.dailymart.databinding.ActivityCategoryBinding


class CategoryActivity : BaseActivity<ActivityCategoryBinding>(ActivityCategoryBinding::inflate) {
    val TAG = "TT"
    private val viewModel: CategoryViewModel by viewModels { AppViewModelFactory }
    private lateinit var mCategoryAdapter: CategoryAdapter

    private  var mListCategory: List<Category> = ArrayList()
    @SuppressLint("SetTextI18n")
    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.imvAdd.setOnClickListener {
            AddCategoryDialog(this){
                viewModel.insertCategory(Category(id = it,name = it))
                viewModel.getAllCategory()
            }.show()
        }
        initRecycleView()
    }
    @SuppressLint("SetTextI18n")
    override fun setupObserver() {
        viewModel.listCategory.observe(this) { listCategory ->
            // thao tác với list category ở đây
            mCategoryAdapter.setCategoryData(listCategory)
//            mListCategory = listCategory
            viewModel.getAllCategory()
            binding.tong.text = "Tổng số : "+mCategoryAdapter.itemCount.toString()
        }
    }

//    private fun searchCatgory(){
//        binding.edSearch.getTextOnChange{
//        }
//    }
    private fun initRecycleView() {
        mCategoryAdapter = CategoryAdapter(this,mListCategory){
            binding.rcvCategory.layoutManager = LinearLayoutManager(this)
        }
        binding.rcvCategory.adapter = mCategoryAdapter
    }
}

