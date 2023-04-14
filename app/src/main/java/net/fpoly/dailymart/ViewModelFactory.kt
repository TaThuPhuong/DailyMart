package net.fpoly.dailymart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import net.fpoly.dailymart.data.model.param.ForgotPass
import net.fpoly.dailymart.data.repository.InvoiceRepositoryImpl
import net.fpoly.dailymart.data.repository.SupplierRepositoryImpl
import net.fpoly.dailymart.view.products.add_product.AddProductViewModel
import net.fpoly.dailymart.view.add_staff.AddStaffViewModel
import net.fpoly.dailymart.view.bank_info.BankInfoViewModel
import net.fpoly.dailymart.view.category.CategoryViewModel
import net.fpoly.dailymart.view.change_password.ChangePasswordViewModel
import net.fpoly.dailymart.view.check_date.CheckDateViewModel
import net.fpoly.dailymart.view.forget_password.ForgetPassViewModel
import net.fpoly.dailymart.view.detailinvoice.DetailInvoiceViewModel
import net.fpoly.dailymart.view.login.LoginViewModel
import net.fpoly.dailymart.view.main.MainViewModel
import net.fpoly.dailymart.view.message.MessageViewModel
import net.fpoly.dailymart.view.order.OrderViewModel
import net.fpoly.dailymart.view.pay.AddInvoiceExportViewModel
import net.fpoly.dailymart.view.payment.PaymentViewModel
import net.fpoly.dailymart.view.products.ProductsViewModel
import net.fpoly.dailymart.view.products.detail_product.ProductDetailViewModel
import net.fpoly.dailymart.view.products.edit_product.ProductEditViewModel
import net.fpoly.dailymart.view.profile.ProfileViewModel
import net.fpoly.dailymart.view.register.RegisterViewModel
import net.fpoly.dailymart.view.report.ReportViewModel
import net.fpoly.dailymart.view.reset_password.ResetPasswordViewModel
import net.fpoly.dailymart.view.splash.SplashViewModel
import net.fpoly.dailymart.view.staff.StaffActivity
import net.fpoly.dailymart.view.staff.StaffViewModel
import net.fpoly.dailymart.view.staff.details.DetailsStaffActivity
import net.fpoly.dailymart.view.stock.StockViewModel
import net.fpoly.dailymart.view.supplier.SupplierViewModel
import net.fpoly.dailymart.view.tab.home.HomeViewModel
import net.fpoly.dailymart.view.tab.goods.GoodsViewModel
import net.fpoly.dailymart.view.tab.invoice.InvoiceViewModel
import net.fpoly.dailymart.view.tab.show_more.ShowMoreViewModel
import net.fpoly.dailymart.view.task.add_new.AddTaskViewModel
import net.fpoly.dailymart.view.task.TaskViewModel
import net.fpoly.dailymart.view.task.task_detail.TaskDetailViewModel
import net.fpoly.dailymart.view.task.task_edit.TaskEditViewModel
import net.fpoly.dailymart.view.work_sheet.WorkSheetViewModel
import net.fpoly.dailymart.view.work_sheet.edit_work_sheet.EditWorkSheetViewModel

@Suppress("UNCHECKED_CAST")
val AppViewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        with(modelClass) {
            val app = checkNotNull(extras[APPLICATION_KEY]) as DailySmartApp
            var activity = DetailsStaffActivity()
            val context = app.context
            val taskRepository = app.taskRepository
            val userRepository = app.userRepository
            val productRepository = app.productRepository
            val categoryRepository = app.categoryRepository
            val supplierRepository = app.supplierRepository
            val notificationRepo = app.notificationRepo

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
                    HomeViewModel(app, taskRepository, notificationRepo, InvoiceRepositoryImpl())
                isAssignableFrom(InvoiceViewModel::class.java) ->
                    InvoiceViewModel(context)
                isAssignableFrom(GoodsViewModel::class.java) ->
                    GoodsViewModel()
                isAssignableFrom(ShowMoreViewModel::class.java) ->
                    ShowMoreViewModel(app)
                isAssignableFrom(TaskViewModel::class.java) ->
                    TaskViewModel(app, taskRepository)
                isAssignableFrom(CheckDateViewModel::class.java) ->
                    CheckDateViewModel(app, productRepository)
                isAssignableFrom(OrderViewModel::class.java) ->
                    OrderViewModel(context)
                isAssignableFrom(ReportViewModel::class.java) ->
                    ReportViewModel(context)
                isAssignableFrom(StockViewModel::class.java) ->
                    StockViewModel()
                isAssignableFrom(WorkSheetViewModel::class.java) ->
                    WorkSheetViewModel()
                isAssignableFrom(CategoryViewModel::class.java) ->
                    CategoryViewModel(context)
                isAssignableFrom(AddInvoiceExportViewModel::class.java) ->
                    AddInvoiceExportViewModel(context)
                isAssignableFrom(PaymentViewModel::class.java) ->
                    PaymentViewModel(context)
                isAssignableFrom(SupplierViewModel::class.java) ->
                    SupplierViewModel(context, SupplierRepositoryImpl())
                isAssignableFrom(ProductsViewModel::class.java) ->
                    ProductsViewModel(app, productRepository)
                isAssignableFrom(AddTaskViewModel::class.java) ->
                    AddTaskViewModel(app, taskRepository, userRepository)
                isAssignableFrom(ChangePasswordViewModel::class.java) ->
                    ChangePasswordViewModel(app)
                isAssignableFrom(StaffViewModel::class.java) ->
                    StaffViewModel(app)
                isAssignableFrom(AddStaffViewModel::class.java) ->
                    AddStaffViewModel(app)
                isAssignableFrom(ProfileViewModel::class.java) ->
                    ProfileViewModel(app, userRepository)
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
                    TaskDetailViewModel(app, taskRepository)
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
                isAssignableFrom(ForgetPassViewModel::class.java) ->
                    ForgetPassViewModel(app)
                isAssignableFrom(ResetPasswordViewModel::class.java) ->
                    ResetPasswordViewModel(app)
                isAssignableFrom(EditWorkSheetViewModel::class.java) ->
                    EditWorkSheetViewModel(app, userRepository)
                isAssignableFrom(BankInfoViewModel::class.java) ->
                    BankInfoViewModel(app)
                isAssignableFrom(DetailInvoiceViewModel::class.java) -> {
                    DetailInvoiceViewModel(app)
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}