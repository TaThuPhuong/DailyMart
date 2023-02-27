package net.fpoly.dailymart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import net.fpoly.dailymart.view.check_date.CheckDateViewModel
import net.fpoly.dailymart.view.login.LoginViewModel
import net.fpoly.dailymart.view.main.MainViewModel
import net.fpoly.dailymart.view.order.OrderViewModel
import net.fpoly.dailymart.view.register.RegisterViewModel
import net.fpoly.dailymart.view.report.ReportViewModel
import net.fpoly.dailymart.view.splash.SplashViewModel
import net.fpoly.dailymart.view.stock.StockActivity
import net.fpoly.dailymart.view.stock.StockViewModel
import net.fpoly.dailymart.view.tab.home.HomeViewModel
import net.fpoly.dailymart.view.tab.product.ProductViewModel
import net.fpoly.dailymart.view.tab.receipt.ReceiptViewModel
import net.fpoly.dailymart.view.tab.show_more.ShowMoreViewModel
import net.fpoly.dailymart.view.task.TaskViewModel
import net.fpoly.dailymart.view.work_sheet.WorkSheetViewModel

@Suppress("UNCHECKED_CAST")
val AppViewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        with(modelClass) {
            val app = checkNotNull(extras[APPLICATION_KEY]) as DailySmartApp
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
                    ReceiptViewModel()
                isAssignableFrom(ProductViewModel::class.java) ->
                    ProductViewModel()
                isAssignableFrom(ShowMoreViewModel::class.java) ->
                    ShowMoreViewModel()
                isAssignableFrom(TaskViewModel::class.java) ->
                    TaskViewModel(app)
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
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}