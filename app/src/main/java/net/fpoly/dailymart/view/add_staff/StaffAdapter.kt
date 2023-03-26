package net.fpoly.dailymart.view.add_staff

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.databinding.ItemStaffBinding

class StaffAdapter(
    private val mContext: Context,
    private var mListStaff: List<User>,
    private val onClick: (User) -> Unit,
) : RecyclerView.Adapter<StaffAdapter.ItemView>() {
    class ItemView(val binding: ItemStaffBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setUserData(listUser: List<User>) {
        mListStaff = listUser
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        return ItemView(
            ItemStaffBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mListStaff.size
    }

    override fun onBindViewHolder(holder: ItemView, position: Int) {
        with(holder) {
            with(mListStaff[position]) {
                binding.tvName.text = this.name
                binding.tvPhone.text = this.phone
                binding.tvRole.text = this.role
            }
        }
    }

}