package net.fpoly.dailymart.view.category

import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import net.fpoly.dailymart.AppViewModelFactory
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
        setupEdSearch()
        setupListCategory()
        setupSnackbar()
        setupCheckRole()
    }

    private fun setupCheckRole() {
        if (SharedPref.getUser(this).role == ROLE.staff) {
            binding.imvAdd.gone()
        }
    }

    private fun setupEdSearch() {
        binding.edSearch.doAfterTextChanged {
            val text = binding.edSearch.text
            if (text.isNotEmpty()) {
                viewModel.listCategory.value?.also { invoices ->
                    val result =
                        invoices.filter { it.id.lowercase().contains(text) || it.name.lowercase().contains(text) }
                            .toMutableList()
                    viewModel.listCategory.value = result
                }
            } else {
                viewModel.listCategory.value = viewModel.listCategoryRemoteData
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
    }

    override fun setupObserver() {}

    private fun setupSnackbar() {
        binding.root.setupSnackbar(this, viewModel.showSnackbar)
    }

    companion object {
        const val TAG = "Senior"
    }

}

