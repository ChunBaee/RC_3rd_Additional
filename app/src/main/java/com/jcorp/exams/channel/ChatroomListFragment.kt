package com.jcorp.exams.channel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.jcorp.exams.R
import com.jcorp.exams.channel.adapter.SeveralAdapter
import com.jcorp.exams.channel.data.ChannelSeveralData
import com.jcorp.exams.databinding.ItemChatroomMessageBinding
import com.jcorp.exams.mViewModel


class ChatroomListFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: ItemChatroomMessageBinding
    private val viewModel by activityViewModels<mViewModel>()
    private lateinit var chatAdapter: SeveralAdapter
    private lateinit var voiceAdapter: SeveralAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ItemChatroomMessageBinding.inflate(inflater, container, false)

        chatAdapter = SeveralAdapter(requireActivity())
        voiceAdapter = SeveralAdapter(requireActivity())

        setUi()
        observe()

        binding.btnChatChannel.setOnClickListener(this)
        binding.btnVoiceChannel.setOnClickListener(this)

        return binding.root
    }

    private fun observe() {
        viewModel.channelChatList.observe(requireActivity(), Observer {
            chatAdapter.setData(it)
        })

        viewModel.channelVoiceList.observe(requireActivity(), Observer {
            voiceAdapter.setData(it)
        })

        viewModel.currentTab.observe(requireActivity(), Observer {
            FirebaseDatabase.getInstance()
                .getReference("${it.chatroomName}")
                .child("User")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (i in snapshot.children) {
                            Log.d("----", "onDataChange: ${i.key}")
                            viewModel.currentChannelUserId.value = i.key
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
        })
    }

    private fun setUi() {
        binding.chatroomTitle.text = viewModel.currentTab.value?.chatroomName

        binding.listChattingChannel.hasFixedSize()
        binding.listChattingChannel.adapter = chatAdapter

        FirebaseFirestore.getInstance()
            .collection("ChannelData")
            .document(viewModel.currentTab.value?.chatroomName.toString())
            .collection("Chat")
            .get()
            .addOnSuccessListener {
                val list = mutableListOf<ChannelSeveralData>()
                for (i in it) {
                    /*if (viewModel.mutableChannelChatList.size == it.size()) {
                    } else {
                        viewModel.mutableChannelChatList.add(
                            ChannelSeveralData(
                                R.drawable.icon_hashtag,
                                i.id,
                                null
                            )
                        )
                    chatlist.add(ChannelSeveralData(R.drawable.icon_hashtag, i.id, null))
                    viewModel.mutableChannelChatList = chatlist*/
                    list.add(ChannelSeveralData(R.drawable.icon_hashtag, i.id, null))
                    viewModel.mutableChannelChatList = list
                    viewModel.setChannelChatList()
                }
            }


        binding.listVoiceChannel.hasFixedSize()
        binding.listVoiceChannel.adapter = voiceAdapter

        FirebaseFirestore.getInstance()
            .collection("ChannelData")
            .document(viewModel.currentTab.value?.chatroomName.toString())
            .collection("Voice")
            .get()
            .addOnSuccessListener {
                val list = mutableListOf<ChannelSeveralData>()
                for (i in it) {
                    /*if (viewModel.mutableChannelVoiceList.contains(
                            ChannelSeveralData(
                                R.drawable.icon_voicechat,
                                i.id,
                                null
                            )
                        )
                    ) {

                    } else {
                        viewModel.mutableChannelVoiceList.add(
                            ChannelSeveralData(
                                R.drawable.icon_voicechat,
                                i.id,
                                null
                            )
                        )
                    }*/
                    list.add(ChannelSeveralData(R.drawable.icon_voicechat, i.id, null))
                    viewModel.mutableChannelVoiceList = list
                    viewModel.setChannelVoiceList()
                }
            }

        chatAdapter.clickListener(
            object : SeveralAdapter.ClickListener {
                override fun onClick(view: View, position: Int) {
                    viewModel.currentChatChannel.value =
                        viewModel.channelChatList.value?.get(position)
                    viewModel.pagerPosition.value = 1
                    viewModel.changeNowClick3(position)
                }

            })

    }


    override fun onClick(view: View) {
        Log.d("----", "onClick: ${view.id}")
        when (view.id) {
            R.id.btn_chat_channel -> {
                Log.d("----", "onClick: CHANNEL")
                if (binding.listChattingChannel.visibility == View.VISIBLE) {
                    binding.listChattingChannel.visibility = View.GONE
                    binding.imgFlipChat.setImageResource(R.drawable.icon_hide_tab)
                } else {
                    binding.listChattingChannel.visibility = View.VISIBLE
                    binding.imgFlipChat.setImageResource(R.drawable.icon_show_tab)
                }
            }

            R.id.btn_voice_channel -> {
                Log.d("----", "onClick: VOICE")
                if (binding.listVoiceChannel.visibility == View.VISIBLE) {
                    binding.listVoiceChannel.visibility = View.GONE
                    binding.imgFlipVoice.setImageResource(R.drawable.icon_hide_tab)
                } else {
                    binding.listVoiceChannel.visibility = View.VISIBLE
                    binding.imgFlipVoice.setImageResource(R.drawable.icon_show_tab)
                }
            }
        }
    }
}
