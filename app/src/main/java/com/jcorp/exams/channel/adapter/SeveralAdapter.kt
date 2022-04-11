package com.jcorp.exams.channel.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jcorp.exams.R
import com.jcorp.exams.channel.data.ChannelSeveralData
import com.jcorp.exams.databinding.ItemChannelInnerBinding

class SeveralAdapter(context : Context) : RecyclerView.Adapter<SeveralAdapter.SeveralViewHolder>() {
    private lateinit var clickListener : ClickListener
    private val mContext = context
    private var data = mutableListOf<ChannelSeveralData>()

    interface ClickListener {
        fun onClick (view : View, position : Int)
    }

    fun clickListener (clickListener : ClickListener) {
        this.clickListener = clickListener
    }


    inner class SeveralViewHolder (val binding : ItemChannelInnerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (item : ChannelSeveralData) {
            binding.channelTitle.text = item.channelName
            binding.channelMark.setImageResource(item.channelTag!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeveralViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemChannelInnerBinding>(layoutInflater, R.layout.item_channel_inner, parent, false)
        return SeveralViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SeveralViewHolder, position: Int) {
        holder.bind(data[position])

        holder.binding.channelTitle.text = data[position].channelName
        holder.binding.channelMark.setImageResource(data[position].channelTag!!)

        when(data[position].NowClick) {
            "T" -> {
                holder.binding.layoutChannelChat.setCardBackgroundColor(ResourcesCompat.getColor(mContext.resources, R.color.dmlist_friend_clicked_color,null))
                holder.binding.channelTitle.setTextColor(ResourcesCompat.getColor(mContext.resources, R.color.white, null))
            }
            "F" -> {
                holder.binding.layoutChannelChat.setCardBackgroundColor(ResourcesCompat.getColor(mContext.resources, android.R.color.transparent, null))
                holder.binding.channelTitle.setTextColor(ResourcesCompat.getColor(mContext.resources, R.color.dmlist_friend_name_color, null))
            }
        }

        holder.itemView.setOnClickListener {
            clickListener.onClick(it, holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(list : MutableList<ChannelSeveralData>) {
        data = list.toMutableList()
        notifyDataSetChanged()
    }

}