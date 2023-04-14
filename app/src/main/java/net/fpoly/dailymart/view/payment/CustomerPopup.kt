package net.fpoly.dailymart.view.payment

import androidx.core.widget.doAfterTextChanged
import net.fpoly.dailymart.base.BaseDialog
import net.fpoly.dailymart.data.model.Customer
import net.fpoly.dailymart.data.model.CustomerParam
import net.fpoly.dailymart.databinding.PopupCustomerPickBinding
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.extension.view_extention.visible

class CustomerPopup(val activity: PaymentActivity, private val viewModel: PaymentViewModel) :
    BaseDialog<PopupCustomerPickBinding>(activity, PopupCustomerPickBinding::inflate) {

    private var customers = listOf<Customer>()
    val adapter = CustomerAdapter(viewModel) {
        viewModel.customerSelected(it)
        dismiss()
    }

    override fun initData() {
        viewModel.customers.observe(activity) {
            customers = it
            adapter.submitList(it)
        }
        binding.listCustomer.adapter = adapter
        setupSearch()
        setupBtnBack()
        setupBtnAdd()
        setupBtnCreateCustomer()
    }

    private fun setupBtnCreateCustomer() {
        binding.btnCreate.setOnClickListener {
            createParamCustomer()
        }
    }

    private fun createParamCustomer() {
        if (isValidate()) {
            val name = binding.nameCustomer.text.toString()
            val phoneNumber = binding.phoneCustomer.text.toString()
            val param = CustomerParam(name = name, phoneNumber = phoneNumber)
            viewModel.createCustomer(param) {
                binding.phoneCustomer.setText("")
                binding.nameCustomer.setText("")
                binding.layoutAdd.gone()
                binding.layoutSearch.visible()
                binding.title.text = LIST_CUSTOMER
            }
        }
    }

    private fun isValidate(): Boolean {
        val phoneNumber = binding.phoneCustomer.text.toString().replace(" ", "")
        val name = binding.nameCustomer.text.toString().replace(" ", "")
        val regex = Regex("(84|0[3|5|7|8|9])+([0-9]{8})\\b")
        var isValidate = true

        if (name.length < 3) {
            binding.nameCustomer.error = VALIDATE_NAME
            isValidate = false
        }

        if (name.toIntOrNull() != null) {
            binding.nameCustomer.error = VALIDATE_NAME
            isValidate = false
        }

        if (phoneNumber.length > 11 || !regex.matches(phoneNumber)) {
            binding.phoneCustomer.error = VALIDATE_NUMBER
            isValidate = false
        }
        if (customers.any { it.phoneNumber == phoneNumber }) {
            binding.phoneCustomer.error = VALIDATE_NUMBER_HAVED
            isValidate = false
        }

        return isValidate
    }

    private fun setupBtnAdd() {
        binding.btnAdd.setOnClickListener {
            binding.layoutSearch.gone()
            binding.layoutAdd.visible()
            binding.title.text = ADD_NEW
        }
    }

    private fun setupBtnBack() {
        binding.imvClose.setOnClickListener {
            dismiss()
        }
    }

    private fun setupSearch() {
        binding.searchBar.doAfterTextChanged { editable ->
            val keyword = editable.toString()
            if (keyword.isEmpty()) {
                adapter.submitList(customers)
                return@doAfterTextChanged
            }
            val filter =
                customers.filter {
                    it.name.lowercase().contains(keyword) || it.phoneNumber.contains(
                        keyword
                    )
                }
            adapter.submitList(filter)
        }
    }

    companion object {
        const val ADD_NEW = "Thêm khách hàng"
        const val LIST_CUSTOMER = "Danh sách khách hàng"
        const val VALIDATE_NAME = "Tên khách hàng không hợp lệ"
        const val VALIDATE_NUMBER = "Số điện thoại không hợp lệ"
        const val VALIDATE_NUMBER_HAVED = "Số điện thoại đã tồn tại"


    }
}