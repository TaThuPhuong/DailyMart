package net.fpoly.dailymart.view.staff

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.data.model.param.Datum
import net.fpoly.dailymart.data.model.param.RegisterParam
import net.fpoly.dailymart.databinding.ItemStaffBinding
import kotlin.math.log

class StaffAdapter(
    private val mContext: Context,
    private var mListStaff: List<Datum>,
    private val onClick: (Datum) -> Unit,
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

    override fun onBindViewHolder(holder: ItemView, position: Int) {
        with(holder) {
            with(mListStaff[position]) {
                binding.tvName.text = this.name
                binding.tvPhone.text = this.phoneNumber
                binding.tvRole.text = this.role
                binding.root.setOnClickListener {
                    onClick(this)
                    Log.d("tuvm", "onBindViewHolder: onclick")
                }
            }
        }
    }

}