package net.fpoly.dailymart.view.task.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.data.model.UserRes
import net.fpoly.dailymart.databinding.ItemStaffBinding

class StaffAdapter(
    private var mListUser: List<UserRes>,
    private val onSelectUser: (UserRes) -> Unit
) :
    RecyclerView.Adapter<StaffAdapter.ItemView>() {

    class ItemView(val binding: ItemStaffBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setUserData(listUser: List<UserRes>) {
        mListUser = listUser
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
        return mListUser.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemView, position: Int) {
        with(holder) {
            with(mListUser[position]) {
                binding.tvName.text = this.name
                binding.tvPhone.text = "SĐT: ${this.phoneNumber}"
                binding.tvRole.text = this.role.value
                binding.root.setOnClickListener {
                    onSelectUser(this)
                }
            }
        }
    }
}