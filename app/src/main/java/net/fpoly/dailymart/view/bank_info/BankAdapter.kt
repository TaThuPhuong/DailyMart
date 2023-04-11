package net.fpoly.dailymart.view.bank_info

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.fpoly.dailymart.data.model.BankModel
import net.fpoly.dailymart.databinding.ItemBankBinding

class BankAdapter(
    val mContext: Context,
    var mListBank: List<BankModel>,
    val onSelect: (BankModel) -> Unit,

    ) : RecyclerView.Adapter<BankAdapter.ItemView>() {

    inner class ItemView(val binding: ItemBankBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<BankModel>) {
        mListBank = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        return ItemView(ItemBankBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemView, position: Int) {
        val model = mListBank[position]
        holder.binding.root.setOnClickListener {
            onSelect(model)
        }
        with(holder) {
            with(model) {
                Glide.with(mContext).load(this.logo).into(binding.imvLogo)
                binding.tvBankName.text = this.name
            }
        }
    }

    override fun getItemCount(): Int {
        return mListBank.size
    }
}