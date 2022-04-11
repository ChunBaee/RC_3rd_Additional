package com.jcorp.exams.directmsg.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jcorp.exams.CurUserData
import com.jcorp.exams.R
import com.jcorp.exams.databinding.ItemRvChattingBinding
import com.jcorp.exams.directmsg.data.DmFriendData
import com.jcorp.exams.directmsg.data.ItemRvChatting

class RvChattingAdapter(context : Context) : RecyclerView.Adapter<RvChattingAdapter.ChatViewHolder>() {
    private var data = mutableListOf<ItemRvChatting>()
    private var mUser : CurUserData? = null
    private var mCurFriend : DmFriendData? = null

    private var mContext = context

    inner class ChatViewHolder(val binding : ItemRvChattingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : ItemRvChatting) {
            binding.txtChatContent.text = item.Content
            binding.txtChatUserTime.text = item.SendTime

            if(item.SenderId == mUser?.Id && item.profileUrl?.isEmpty() == true) {
                binding.txtChatUserName.text = mUser?.NickName
                Glide.with(mContext)
                    .load(mUser?.PhotoUrl)
                    .into(binding.imgChatUserProfile)
            } else if(item.SenderId == mCurFriend?.Id && item.profileUrl?.isEmpty() == true) {
                binding.txtChatUserName.text = mCurFriend?.NickName
                Glide.with(mContext)
                    .load(mCurFriend?.PhotoUrl)
                    .into(binding.imgChatUserProfile)
            } else {
                binding.txtChatUserName.text = item.NickName
                Glide.with(mContext)
                    .load(item.profileUrl)
                    .into(binding.imgChatUserProfile)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        val binding = DataBindingUtil.inflate<ItemRvChattingBinding>(layoutInflater, R.layout.item_rv_chatting, parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(data[position])
        Log.d("****", "onBindViewHolder: ${data[position]}")

        holder.binding.txtChatUserTime.text = data[position].SendTime
        holder.binding.txtChatContent.text = data[position].Content

        if(data[position].SenderId == mUser?.Id && data[position].profileUrl?.isEmpty() == true) {
            holder.binding.txtChatUserName.text = mUser?.NickName
            Glide.with(mContext)
                .load(mUser?.PhotoUrl)
                .into(holder.binding.imgChatUserProfile)
        } else if(data[position].SenderId == mCurFriend?.Id && data[position].profileUrl?.isEmpty() == true) {
            holder.binding.txtChatUserName.text = mCurFriend?.NickName
            Glide.with(mContext)
                .load(mCurFriend?.PhotoUrl)
                .into(holder.binding.imgChatUserProfile)
        } else {
            holder.binding.txtChatUserName.text = data[position].NickName
            Glide.with(mContext)
                .load(data[position].profileUrl)
                .into(holder.binding.imgChatUserProfile)
        }


    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(list : MutableList<ItemRvChatting>) {
        data = list.toMutableList()
        notifyDataSetChanged()
    }

    fun setUsers (user : CurUserData?, friend : DmFriendData?) {
        mUser = user
        mCurFriend = friend
    }

}