package net.fpoly.dailymart.view.products.edit_product

import android.app.Application
import androidx.lifecycle.ViewModel
import net.fpoly.dailymart.repository.CategoryRepository
import net.fpoly.dailymart.repository.SupplierRepository

class ProductEditViewModel(
    val app: Application,
    private val categoryRepo: CategoryRepository,
    private val supplierRepo: SupplierRepository,
) : ViewModel() {
}