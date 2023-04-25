package net.fpoly.dailymart.view.supplier

import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.annotation.MenuRes
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivitySupplierBinding
import net.fpoly.dailymart.extension.setupSnackbar
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref

class SupplierActivity : BaseActivity<ActivitySupplierBinding>(ActivitySupplierBinding::inflate) {

    private val viewModel: SupplierViewModel by viewModels { AppViewModelFactory }
    private lateinit var supplierAdapter: SupplierAdapter

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupListSupplier()
        setupSearchSupplier()
        setupBtnClear()
        setupSnackbar()
        setupBtnBack()
        setupCheckRole()
        setupRefresh()
    }

    private fun setupRefresh() {
        binding.refreshLayout.setColorSchemeColors(
            ContextCompat.getColor(
                this,
                R.color.pink_primary
            )
        )
        binding.refreshLayout.setOnRefreshListener {
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.getSupplierPage(clear = true, loading = false)
                binding.refreshLayout.isRefreshing = false
            }
        }
    }

    private fun setupCheckRole() {
        if (SharedPref.getUser(this).role == ROLE.staff) {
            binding.tvAdd.gone()
        }
    }

    private fun setupBtnBack() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupSnackbar() {
        binding.root.setupSnackbar(this, viewModel.showSnackbar)
    }

    override fun setupObserver() {
    }

    private fun setupBtnClear() {
        binding.imvClear.setOnClickListener { binding.edSearch.setText("") }
    }

    private fun setupSearchSupplier() {
        binding.edSearch.doAfterTextChanged {
            val text = binding.edSearch.text.toString().lowercase()
            if (text.isNotEmpty()) {
                binding.imvClear.visibility = View.VISIBLE
                val filter = viewModel.rootSupplier.filter {
                    it.phoneNumber.lowercase().contains(text) || it.supplierName.lowercase()
                        .contains(text)
                }
                viewModel.listSupplier.value = filter.toMutableList()
                viewModel.loadShowList()
            } else {
                binding.imvClear.visibility = View.GONE
                viewModel.listSupplier.value = viewModel.rootSupplier
                viewModel.loadShowList()
            }
        }
    }

    private fun setupListSupplier() {
        supplierAdapter = SupplierAdapter(viewModel)
        binding.listSupplier.adapter = supplierAdapter

        binding.btnMore.setOnClickListener {
            showMenu(it, R.menu.more_supplier)
        }

        binding.listSupplier.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    lifecycleScope.launch {
//                        viewModel.loadMorePage()
                    }
                }
            }
        })

    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(this, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.type_active -> {
                    viewModel.typeSupplier = SupplierViewModel.ACTIVE
                    viewModel.loadShowList()
                    true
                }

                R.id.type_disable -> {
                    viewModel.typeSupplier = SupplierViewModel.DISABLE
                    viewModel.loadShowList()
                    true
                }

                else -> true
            }
        }
        popup.show()
    }
}