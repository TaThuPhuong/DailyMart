package net.fpoly.dailymart.data.model.param

import com.google.gson.annotations.SerializedName

data class SupplierParam(
    val supplierName: String,
    var phoneNumber: String,
)

data class SupplierParamList(@SerializedName("data")val mList : List<SupplierParam>)

