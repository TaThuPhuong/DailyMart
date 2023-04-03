package net.fpoly.dailymart.view.supplier

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.databinding.ItemSupplierBinding

class SupplierAdapter(
    private val mContext: Context,
    private var mListSupplier: List<Supplier>,
    private val onClick: (Supplier) -> Unit
) : RecyclerView.Adapter<SupplierAdapter.ItemSupplier>() {

    class ItemSupplier(val binding: ItemSupplierBinding):RecyclerView.ViewHolder(binding.root)
    @SuppressLint("NotifyDataSetChanged")
    fun setSupplierData(listSupplier: List<Supplier>){
        mListSupplier = listSupplier
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemSupplier {
        return ItemSupplier(ItemSupplierBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: ItemSupplier, position: Int) {
        with(holder){
            with(mListSupplier[position]){

                binding.tvSupplierName.text = this.name
                binding.tvSupplierPhone.text= this.phone
                binding.root.setOnClickListener{
                    onClick(this)
                }
            }
        }
    }

    override fun getItemCount(): Int {
      return mListSupplier.size
    }

}