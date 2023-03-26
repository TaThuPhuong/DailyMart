package net.fpoly.dailymart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import net.fpoly.dailymart.view.add_product.AddProductViewModel
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
import net.fpoly.dailymart.view.work_sheet.WorkSheetViewModel

@Suppress("UNCHECKED_CAST")
val AppViewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        with(modelClass) {
            val app = checkNotNull(extras[APPLICATION_KEY]) as DailySmartApp
            val context = app.context
            val userRepository = app.userRepository
            val taskRepository = app.taskRepository
            val invoiceRepository = app.invoiceRepository
            val categoryRepository = app.categoryRepository
            val ppRepository = app.productPriceRepository
            val pRepository = app.productRepository
            when {
                isAssignableFrom(SplashViewModel::class.java) ->
                    SplashViewModel()
                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel()
                isAssignableFrom(RegisterViewModel::class.java) ->
                    RegisterViewModel()
                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel()
                isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel()
                isAssignableFrom(ReceiptViewModel::class.java) ->
                    ReceiptViewModel(context, invoiceRepository)
                isAssignableFrom(GoodsViewModel::class.java) ->
                    GoodsViewModel()
                isAssignableFrom(ShowMoreViewModel::class.java) ->
                    ShowMoreViewModel()
                isAssignableFrom(TaskViewModel::class.java) ->
                    TaskViewModel(app, taskRepository, userRepository)
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
                    CategoryViewModel()
                isAssignableFrom(PayViewModel::class.java) ->
                    PayViewModel()
                isAssignableFrom(PaymentViewModel::class.java) ->
                    PaymentViewModel()
                isAssignableFrom(SupplierViewModel::class.java) ->
                    SupplierViewModel()
                isAssignableFrom(ProductsViewModel::class.java) ->
                    ProductsViewModel(pRepository)
                isAssignableFrom(AddTaskViewModel::class.java) ->
                    AddTaskViewModel(app, taskRepository, userRepository)
                isAssignableFrom(ChangePasswordViewModel::class.java) ->
                    ChangePasswordViewModel()
                isAssignableFrom(StaffViewModel::class.java) ->
                    StaffViewModel(userRepository)
                isAssignableFrom(AddStaffViewModel::class.java) ->
                    AddStaffViewModel(userRepository)
                isAssignableFrom(ProfileViewModel::class.java) ->
                    ProfileViewModel()
                isAssignableFrom(MessageViewModel::class.java) ->
                    MessageViewModel()
                isAssignableFrom(AddProductViewModel::class.java) ->
                    AddProductViewModel(pRepository,ppRepository)
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}