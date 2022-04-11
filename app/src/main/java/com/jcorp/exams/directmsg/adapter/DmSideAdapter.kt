package com.jcorp.exams.directmsg.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jcorp.exams.R
import com.jcorp.exams.databinding.ItemDmListBinding
import com.jcorp.exams.directmsg.data.ItemDmSideList


class DmSideAdapter (context : Context) : RecyclerView.Adapter<DmSideAdapter.DmViewHolder>() {
    private lateinit var clickListener : ClickListener
    private var data = mutableListOf<ItemDmSideList>()
    private var mContext = context

    interface ClickListener {
        fun onClick (view : View, position : Int)
    }

    fun clickListener (clickListener : ClickListener) {
        this.clickListener = clickListener
    }

    inner class DmViewHolder( val binding : ItemDmListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : ItemDmSideList) {
            when(item.IconRes!!.length == 10) {
                true -> {
                    Glide.with(mContext)
                        .load(item.IconRes!!.toInt())
                        .into(binding.imgDmItemInner)

                    //binding.imgDmItem.setImageResource(R.color.black)
                }
                else -> {
                    Glide.with(mContext)
                        .load(item.IconRes)
                        .into(binding.imgDmItem)
                }
            }

            when(item.NowClick) {
                "T" -> {
                    binding.imgDmItem.setBackgroundResource(R.drawable.btn_dm_list_clicked)
                    binding.imgDmSideItem.visibility = View.VISIBLE
                }
                "F" -> {
                    binding.imgDmItem.setBackgroundResource(R.drawable.btn_dm_list_unclicked)
                    binding.imgDmSideItem.visibility = View.INVISIBLE
                }
            }
            binding.imgDmItem.clipToOutline = true

            Log.d("----", "bind: ${item}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DmViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemDmListBinding>(layoutInflater, R.layout.item_dm_list, parent, false)
        return DmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DmViewHolder, position: Int) {
        holder.bind(data[position])
        when(data[position].IconRes!!.length == 10) {
            true -> {
                Glide.with(mContext)
                    .load(data[position].IconRes!!.toInt())
                    .into(holder.binding.imgDmItemInner)
                //holder.binding.imgDmItem.setImageResource(R.color.discord_color_2)
            }
            else -> {
                Glide.with(mContext)
                    .load(data[position].IconRes)
                    .into(holder.binding.imgDmItem)
            }
        }

        if(data[position].haveImage == "T") {
            when (data[position].NowClick) {
                "T" -> {
                    holder.binding.imgDmItem.setBackgroundResource(R.drawable.btn_dm_list_clicked)
                    holder.binding.imgDmSideItem.visibility = View.VISIBLE
                }
                "F" -> {
                    holder.binding.imgDmItem.setBackgroundResource(R.drawable.btn_dm_list_unclicked)
                    holder.binding.imgDmSideItem.visibility = View.INVISIBLE
                }
            }

        } else {
            when(data[position].NowClick) {
                "T" -> {
                    holder.binding.imgDmItem.setBackgroundResource(R.drawable.btn_dm_list_clicked)
                    holder.binding.imgDmItem.setImageResource(R.color.sideitem_click_color)
                }
                "F" -> {
                    holder.binding.imgDmItem.setBackgroundResource(R.drawable.btn_dm_list_unclicked)
                    holder.binding.imgDmItem.setImageResource(R.color.discord_color_2)
                }
            }
        }
        if(position == 0) {
            holder.binding.dmDivider.visibility = View.VISIBLE
        } else {
            holder.binding.dmDivider.visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            clickListener.onClick(it, holder.adapterPosition)
        }
        holder.itemView.clipToOutline = true
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(list : MutableList<ItemDmSideList>) {
        data = list.toMutableList()
        notifyDataSetChanged()
    }
}