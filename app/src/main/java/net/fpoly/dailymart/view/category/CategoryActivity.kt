package net.fpoly.dailymart.view.category

import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.annotation.MenuRes
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivityCategoryBinding
import net.fpoly.dailymart.extension.setupSnackbar
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref

class CategoryActivity : BaseActivity<ActivityCategoryBinding>(ActivityCategoryBinding::inflate) {

    private val viewModel: CategoryViewModel by viewModels { AppViewModelFactory }
    private lateinit var adapter: CategoryAdapter

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupBtnBack()
        setupBtnMore()
        setupEdSearch()
        setupListCategory()
        setupSnackbar()
        setupCheckRole()
    }

    private fun setupBtnMore() {
        binding.btnMore.setOnClickListener {
            showMenu(it, R.menu.more_supplier)
        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(this, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.type_active -> {
                    viewModel.type = true
                    viewModel.showListCategories()
                    true
                }

                R.id.type_disable -> {
                    viewModel.type = false
                    viewModel.showListCategories()
                    true
                }

                else -> true
            }
        }
        popup.show()
    }

    private fun setupCheckRole() {
        if (SharedPref.getUser(this).role == ROLE.staff) {
            binding.imvAdd.gone()
        }
    }

    private fun setupEdSearch() {
        binding.edSearch.doAfterTextChanged {
            val text = binding.edSearch.text.toString()
            if (text.isNotEmpty()) {
                val result =
                    viewModel.rootCategories.filter {
                        it.id.contains(text, true) || it.name.contains(text, true)
                    }.toMutableList()
                viewModel.showListCategories(result)
            } else {
                viewModel.showListCategories()
            }
        }
    }

    private fun setupBtnBack() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupListCategory() {
        adapter = CategoryAdapter(viewModel)
        binding.listCategory.adapter = adapter

        binding.listCategory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    viewModel.loadMorePage()
                }
            }
        })
    }

    override fun setupObserver() {}

    private fun setupSnackbar() {
        binding.root.setupSnackbar(this, viewModel.showSnackbar)
    }

    companion object {
        const val TAG = "Senior"
    }

}

