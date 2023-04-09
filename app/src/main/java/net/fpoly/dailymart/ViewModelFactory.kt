package net.fpoly.dailymart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import net.fpoly.dailymart.data.repository.SupplierRepositoryImpl
import net.fpoly.dailymart.view.products.add_product.AddProductViewModel
import net.fpoly.dailymart.view.add_staff.AddStaffViewModel
import net.fpoly.dailymart.view.category.CategoryViewModel
import net.fpoly.dailymart.view.change_password.ChangePasswordViewModel
import net.fpoly.dailymart.view.check_date.CheckDateViewModel
import net.fpoly.dailymart.view.login.LoginViewModel
import net.fpoly.dailymart.view.main.MainViewModel
import net.fpoly.dailymart.view.message.MessageViewModel
import net.fpoly.dailymart.view.order.OrderViewModel
import net.fpoly.dailymart.view.pay.PayViewModel
import net.fpoly.dailymart.view.payment.PaymentViewModel
import net.fpoly.dailymart.view.products.ProductsViewModel
import net.fpoly.dailymart.view.task.detail_product.ProductDetailViewModel
import net.fpoly.dailymart.view.products.edit_product.ProductEditViewModel
import net.fpoly.dailymart.view.profile.ProfileViewModel
import net.fpoly.dailymart.view.register.RegisterViewModel
import net.fpoly.dailymart.view.report.ReportViewModel
import net.fpoly.dailymart.view.splash.SplashViewModel
import net.fpoly.dailymart.view.staff.StaffViewModel
import net.fpoly.dailymart.view.stock.StockViewModel
import net.fpoly.dailymart.view.supplier.SupplierViewModel
import net.fpoly.dailymart.view.tab.home.HomeViewModel
import net.fpoly.dailymart.view.tab.goods.GoodsViewModel
import net.fpoly.dailymart.view.tab.receipt.ReceiptViewModel
import net.fpoly.dailymart.view.tab.show_more.ShowMoreViewModel
import net.fpoly.dailymart.view.task.add_new.AddTaskViewModel
import net.fpoly.dailymart.view.task.TaskViewModel
import net.fpoly.dailymart.view.task.task_detail.TaskDetailViewModel
import net.fpoly.dailymart.view.task.task_edit.TaskEditViewModel
import net.fpoly.dailymart.view.work_sheet.EditWorkSheetViewModel
import net.fpoly.dailymart.view.work_sheet.WorkSheetViewModel

@Suppress("UNCHECKED_CAST")
val AppViewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        with(modelClass) {
            val app = checkNotNull(extras[APPLICATION_KEY]) as DailySmartApp
            val context = app.context
            val taskRepository = app.taskRepository
            val userRepository = app.userRepository
            val productRepository = app.productRepository
            val categoryRepository = app.categoryRepository
            val supplierRepository = app.supplierRepository

            when {
                isAssignableFrom(SplashViewModel::class.java) ->
                    SplashViewModel()
                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel()
                isAssignableFrom(RegisterViewModel::class.java) ->
                    RegisterViewModel()
                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(app, userRepository)
                isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel()
                isAssignableFrom(ReceiptViewModel::class.java) ->
                    ReceiptViewModel(context)
                isAssignableFrom(GoodsViewModel::class.java) ->
                    GoodsViewModel()
                isAssignableFrom(ShowMoreViewModel::class.java) ->
                    ShowMoreViewModel()
                isAssignableFrom(TaskViewModel::class.java) ->
                    TaskViewModel(app, taskRepository)
                isAssignableFrom(CheckDateViewModel::class.java) ->
                    CheckDateViewModel()
                isAssignableFrom(OrderViewModel::class.java) ->
                    OrderViewModel()
                isAssignableFrom(ReportViewModel::class.java) ->
                    ReportViewModel()
                isAssignableFrom(StockViewModel::class.java) ->
                    StockViewModel()
                isAssignableFrom(WorkSheetViewModel::class.java) ->
                    WorkSheetViewModel()
                isAssignableFrom(CategoryViewModel::class.java) ->
                    CategoryViewModel(context)
                isAssignableFrom(PayViewModel::class.java) ->
                    PayViewModel()
                isAssignableFrom(PaymentViewModel::class.java) ->
                    PaymentViewModel()
                isAssignableFrom(SupplierViewModel::class.java) ->
                    SupplierViewModel(context, SupplierRepositoryImpl())
                isAssignableFrom(ProductsViewModel::class.java) ->
                    ProductsViewModel(app, productRepository)
                isAssignableFrom(AddTaskViewModel::class.java) ->
                    AddTaskViewModel(app, taskRepository, userRepository)
                isAssignableFrom(ChangePasswordViewModel::class.java) ->
                    ChangePasswordViewModel()
                isAssignableFrom(StaffViewModel::class.java) ->
                    StaffViewModel()
                isAssignableFrom(AddStaffViewModel::class.java) ->
                    AddStaffViewModel()
                isAssignableFrom(ProfileViewModel::class.java) ->
                    ProfileViewModel()
                isAssignableFrom(MessageViewModel::class.java) ->
                    MessageViewModel()
                isAssignableFrom(AddProductViewModel::class.java) ->
                    AddProductViewModel(
                        app,
                        productRepository,
                        supplierRepository,
                        categoryRepository
                    )
                isAssignableFrom(TaskDetailViewModel::class.java) ->
                    TaskDetailViewModel()
                isAssignableFrom(TaskEditViewModel::class.java) ->
                    TaskEditViewModel(app, taskRepository)
                isAssignableFrom(ProductDetailViewModel::class.java) ->
                    ProductDetailViewModel()
                isAssignableFrom(ProductEditViewModel::class.java) ->
                    ProductEditViewModel(
                        app, productRepository,
                        categoryRepository,
                        supplierRepository
                    )
                isAssignableFrom(EditWorkSheetViewModel::class.java) ->
                    EditWorkSheetViewModel(app, userRepository)
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}