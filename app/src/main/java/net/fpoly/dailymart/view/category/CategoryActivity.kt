package net.fpoly.dailymart.view.category
import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.Category
import net.fpoly.dailymart.data.model.param.CategoryParam
import net.fpoly.dailymart.databinding.ActivityCategoryBinding


class CategoryActivity : BaseActivity<ActivityCategoryBinding>(ActivityCategoryBinding::inflate) {
    val TAG = "CategoryActivity"
    private val viewModel: CategoryViewModel by viewModels { AppViewModelFactory }
    private lateinit var mCategoryAdapter: CategoryAdapter
    private val token= "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoibWFuYWdlciIsImlhdCI6MTY4MDQ1MDM5MiwiZXhwIjoxNjgwNTM2NzkyfQ.9eX83EmjrNfekfLOBTnes2TYOplKEy9nxvtHFahGET8"
    private  var mListCategory: List<CategoryParam> = ArrayList()
    @SuppressLint("SetTextI18n")
    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.imvAdd.setOnClickListener {
            AddCategoryDialog(this){
//                viewModel.insertCategory(Category(id = it,name = it))
//                viewModel.getAllCategory()
            }.show()
        }
        initRecycleView()
        viewModel.getAllCategory(token)
    }
    @SuppressLint("SetTextI18n")
    override fun setupObserver() {
        viewModel.listCategory.observe(this) { listCategory ->
            // thao tác với list category ở đây
            Log.d(TAG, "setupObserver: ${listCategory}"  )
//            viewModel.getAllCategory(token)
            mListCategory = listCategory.data
            mCategoryAdapter.setCategoryData(mListCategory)
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

