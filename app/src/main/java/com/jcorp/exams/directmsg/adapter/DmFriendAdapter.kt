package com.jcorp.exams.directmsg.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.Resource
import com.jcorp.exams.R
import com.jcorp.exams.databinding.ItemDirectMessageBinding
import com.jcorp.exams.databinding.ItemDmListBinding
import com.jcorp.exams.databinding.ItemDmMsgListBinding
import com.jcorp.exams.directmsg.data.DmFriendData

class DmFriendAdapter (context : Context) : RecyclerView.Adapter<DmFriendAdapter.DmFriendViewHolder>() {
    private lateinit var clickListener : ClickListener
    private var data = mutableListOf<DmFriendData>()
    private var mContext = context

    interface ClickListener {
        fun onClick (view : View, position : Int)
    }
    fun clickListener (clickListener : ClickListener) {
        this.clickListener = clickListener
    }

    inner class DmFriendViewHolder (val binding : ItemDmMsgListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : DmFriendData) {
            Glide.with(mContext)
                .load(item.PhotoUrl)
                .into(binding.imgFriendProfile)

            binding.imgFriendProfile.setBackgroundResource(R.drawable.btn_dm_list_unclicked)


            binding.txtFriendName.text = item.NickName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DmFriendViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        val binding = DataBindingUtil.inflate<ItemDmMsgListBinding>(layoutInflater, R.layout.item_dm_msg_list, parent, false)
        return DmFriendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DmFriendViewHolder, position: Int) {
        holder.bind(data[position])

        Glide.with(mContext)
            .load(data[position].PhotoUrl)
            .into(holder.binding.imgFriendProfile)
        holder.binding.imgFriendProfile.setBackgroundResource(R.drawable.btn_dm_list_unclicked)
        holder.binding.imgFriendProfile.clipToOutline = true
        holder.binding.txtFriendName.text = data[position].NickName

        when(data[position].NowClick) {
            "T" -> {
                holder.binding.layoutFriendList.setCardBackgroundColor(ResourcesCompat.getColor(mContext.resources, R.color.dmlist_friend_clicked_color,null))
                holder.binding.txtFriendName.setTextColor(ResourcesCompat.getColor(mContext.resources, R.color.white, null))
            }
            "F" -> {
                holder.binding.layoutFriendList.setCardBackgroundColor(ResourcesCompat.getColor(mContext.resources, android.R.color.transparent, null))
                holder.binding.txtFriendName.setTextColor(ResourcesCompat.getColor(mContext.resources, R.color.dmlist_friend_name_color, null))
            }
        }
        holder.itemView.setOnClickListener {
            clickListener.onClick(it, holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(list : MutableList<DmFriendData>) {
        data = list.toMutableList()
        notifyDataSetChanged()
    }
}