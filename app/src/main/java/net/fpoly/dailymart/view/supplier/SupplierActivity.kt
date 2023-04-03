package net.fpoly.dailymart.view.supplier

import android.annotation.SuppressLint
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.databinding.ActivitySupplierBinding
import org.checkerframework.checker.units.qual.m

class SupplierActivity : BaseActivity<ActivitySupplierBinding>(ActivitySupplierBinding::inflate) {

    private val viewModel: SupplierViewModel by viewModels { AppViewModelFactory }
    private lateinit var mSupplierAdapter: SupplierAdapter

    private var mlistSupplier : List<Supplier> = ArrayList()
    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.tvAdd.setOnClickListener {
            AddSupplierDialog(this) {
                viewModel.insertSuppliers(Supplier(name = it.name, phone = it.phone))
                viewModel.getAllSuppliers()
            }.show()
        }
        initRecycleView()
    }


    @SuppressLint("SetTextI18n")
    override fun setupObserver() {
    viewModel.listSupplier.observe(this){listSupplier ->

        mSupplierAdapter.setSupplierData(listSupplier)
//        mlistSupplier = listSupplier
        viewModel.getAllSuppliers()


    }
    }
    private fun initRecycleView() {
        mSupplierAdapter = SupplierAdapter(this,mlistSupplier){
            binding.rcvListSupplier.layoutManager = LinearLayoutManager(this)
        }
        binding.rcvListSupplier.adapter = mSupplierAdapter

    }
}