package net.fpoly.dailymart.view.supplier

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.data.model.param.SupplierParam
import net.fpoly.dailymart.data.model.param.SupplierParamList
import net.fpoly.dailymart.databinding.ActivitySupplierBinding
import org.checkerframework.checker.units.qual.m

class SupplierActivity : BaseActivity<ActivitySupplierBinding>(ActivitySupplierBinding::inflate) {
    val TAG ="SuppActivity"
    private val viewModel: SupplierViewModel by viewModels { AppViewModelFactory }
    private lateinit var mSupplierAdapter: SupplierAdapter
    private val token ="Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoibWFuYWdlciIsImlhdCI6MTY4MDQ1MDM5MiwiZXhwIjoxNjgwNTM2NzkyfQ.9eX83EmjrNfekfLOBTnes2TYOplKEy9nxvtHFahGET8"

    private var mlistSupplier : List<SupplierParam> = ArrayList()

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.tvAdd.setOnClickListener {
            AddSupplierDialog(this) {
//                viewModel.insertSuppliers(Supplier(name = it.name, phone = it.phone))
            }.show()
        }
        initRecycleView()
        viewModel.getAllSuppliers(token)
    }


    @SuppressLint("SetTextI18n")
    override fun setupObserver() {
    viewModel.listSupplier.observe(this){listSupplier ->
        Log.d(TAG, "setupObserver: ${listSupplier}"  )
        viewModel.getAllSuppliers(token)
        mlistSupplier = listSupplier.mList
        mSupplierAdapter.setSupplierData(mlistSupplier)
    }
    }
    private fun initRecycleView() {
        mSupplierAdapter = SupplierAdapter(this,mlistSupplier){
            binding.rcvListSupplier.layoutManager = LinearLayoutManager(this)
        }
        binding.rcvListSupplier.adapter = mSupplierAdapter

    }
}