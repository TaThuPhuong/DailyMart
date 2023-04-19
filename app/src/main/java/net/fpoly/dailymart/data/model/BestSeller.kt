package net.fpoly.dailymart.data.model

data class BestSeller(val sanPham: BestSellProduct, var soLuongDaBan: Int = 0)

data class BestSellProduct(
    val _id: String,
    val supplier: String,
    val industry: String,
    val barcode: String,
    val productName: String,
    val importPrice: Int,
    val sellPrice: Int,
    val unit: String,
    val totalQuantity: Int,
    val expires: ArrayList<String> = ArrayList(),
    val img_product: String,
)