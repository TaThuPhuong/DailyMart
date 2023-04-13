package net.fpoly.dailymart.view.work_sheet.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.fpoly.dailymart.data.model.UserRes
import net.fpoly.dailymart.databinding.ItemNameOnlyBinding
import net.fpoly.dailymart.databinding.ItemStaffDialogBinding

class UserAdapter(
    private var mList: List<UserRes> = ArrayList(),
    val onChose: (UserRes) -> Unit,
) :
    RecyclerView.Adapter<UserAdapter.ItemView>() {

    class ItemView(val binding: ItemStaffDialogBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<UserRes>) {
        mList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        return ItemView(
            ItemStaffDialogBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ItemView, position: Int) {
        with(holder) {
            with(mList[position]) {
                binding.tvName.text = this.name
                binding.tvPhone.text = this.phoneNumber
                binding.root.setOnClickListener {
                    onChose(this)
                }
            }
        }
    }
}