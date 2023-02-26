package net.fpoly.dailymart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import net.fpoly.dailymart.view.login.LoginViewModel
import net.fpoly.dailymart.view.main.MainViewModel
import net.fpoly.dailymart.view.register.RegisterViewModel
import net.fpoly.dailymart.view.splash.SplashViewModel
import net.fpoly.dailymart.view.tab.home.HomeViewModel
import net.fpoly.dailymart.view.tab.product.ProductViewModel
import net.fpoly.dailymart.view.tab.receipt.ReceiptViewModel
import net.fpoly.dailymart.view.tab.show_more.ShowMoreViewModel

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
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}