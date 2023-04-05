package net.fpoly.dailymart.view.category

import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivityCategoryBinding
import net.fpoly.dailymart.extension.setupSnackbar


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
    }

    private fun setupEdSearch() {
        binding.edSearch.doAfterTextChanged {
            val text = binding.edSearch.text
            if (text.isNotEmpty()) {
                viewModel.listCategory.value?.also { invoices ->
                    val result =
                        invoices.filter { it.id.contains(text) || it.name.contains(text) }
                            .toMutableList()
                    viewModel.listCategory.value = result
                }
            } else {
                viewModel.listCategory.value = viewModel.listCategoryRemoteData
            }
        }
    }

    private fun setupBtnBack() {
        binding.imvBack.setOnClickListener {
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

}

