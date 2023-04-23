package net.fpoly.dailymart.view.staff

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import net.fpoly.dailymart.R
import net.fpoly.dailymart.data.model.param.Datum
import net.fpoly.dailymart.databinding.ItemStaffBinding

class StaffAdapter(
    private val mContext: Context,
    private var mListStaff: List<Datum>,
    private val onClick: (Datum) -> Unit,
    private val onCall: (Datum) -> Unit,
) : RecyclerView.Adapter<StaffAdapter.ItemView>() {
    class ItemView(val binding: ItemStaffBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setUserData(listUser: List<Datum>) {
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemView, position: Int) {
        with(holder) {
            with(mListStaff[position]) {
                if (this.status) {
                    binding.root.background =
                        ContextCompat.getDrawable(mContext, R.drawable.bg_white_rd_12)
                    binding.tvPhone.setTextColor(ContextCompat.getColor(mContext, R.color.gray_light))
                } else {
                    binding.root.background =
                        ContextCompat.getDrawable(mContext, R.drawable.bg_gray_light_rd_12)
                    binding.tvPhone.setTextColor(ContextCompat.getColor(mContext, R.color.white))
                }
                binding.imvPhone.setOnClickListener {
                    onCall(this)
                }
                binding.tvName.text = this.name
                binding.tvPhone.text = "SĐT: ${this.phoneNumber}"
                binding.tvRole.text = this.role.value
                binding.root.setOnClickListener {
                    onClick(this)
                }
            }
        }
    }
}