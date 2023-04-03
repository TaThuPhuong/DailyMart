package net.fpoly.dailymart.data.model.param

import com.google.gson.annotations.SerializedName

data class CategoryParam(
    @SerializedName("industryName") val name: String,
)
data class CategoryParamList(@SerializedName("data")val data : List<CategoryParam>)
