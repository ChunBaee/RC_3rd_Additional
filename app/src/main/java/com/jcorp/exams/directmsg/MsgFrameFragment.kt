package com.jcorp.exams.directmsg

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.ACTION_UP
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.firebase.database.*
import com.jcorp.exams.R
import com.jcorp.exams.databinding.FragmentMsgFrameBinding
import com.jcorp.exams.directmsg.adapter.RvChattingAdapter
import com.jcorp.exams.directmsg.data.ItemRvChatting
import com.jcorp.exams.mViewModel
import java.text.SimpleDateFormat

class MsgFrameFragment : Fragment(), View.OnClickListener, View.OnKeyListener {
    private lateinit var binding: FragmentMsgFrameBinding
    private val viewModel by activityViewModels<mViewModel>()
    private lateinit var dmAdapter: RvChattingAdapter
    private lateinit var channelAdapter: RvChattingAdapter

    private var curFriend = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMsgFrameBinding.inflate(inflater, container, false)

        dmAdapter = RvChattingAdapter(requireActivity())
        channelAdapter = RvChattingAdapter(requireActivity())

        observe()

        binding.btnDmMenu.setOnClickListener(this)
        binding.iconDmGroup.setOnClickListener(this)
        binding.edtBottomMsg.setOnKeyListener(this)


        return binding.root
    }

    private fun observe() {
        viewModel.currentTab.observe(requireActivity(), Observer {
            when (it.isDm) {
                true -> {
                    DMObserve()
                    setDmUi()
                }
                false -> {
                    ChannelObserve()
                    setChannelUi()
                }
            }
        })
    }

    private fun DMObserve() {
        viewModel.currentDmFriend.observe(requireActivity(), Observer {
            binding.txtFriendName.text = it.NickName
            binding.edtBottomMsg.hint = "@${it.NickName}에 메시지 보내기"
            curFriend = it.Id.toString()

            FirebaseDatabase.getInstance()
                .getReference("DMList")
                .child("${viewModel.curUserData.value?.Id} - ${it.Id}")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list = mutableListOf<ItemRvChatting>()
                        for (i in snapshot.children) {
                            list.add(i.getValue(ItemRvChatting::class.java)!!)
                            viewModel.mutableChatItemList = list
                            viewModel.setChatList()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        })
        viewModel.chatItemList.observe(requireActivity(), Observer {
            dmAdapter.setUsers(viewModel.curUserData.value, viewModel.currentDmFriend.value)
            dmAdapter.setData(it)
        })
    }

    private fun ChannelObserve() {
        viewModel.currentChatChannel.observe(requireActivity(), Observer {
            binding.txtFriendName.text = it.channelName
            binding.edtBottomMsg.hint = "#${it.channelName}에 메시지 보내기"
            //curFriend = it.Id.toString()
            Log.d("----", "onDataChange: ${it.channelName}")

            FirebaseDatabase.getInstance()
                .getReference("${viewModel.currentTab.value?.chatroomName}")
                .child("${it.channelName}")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list = mutableListOf<ItemRvChatting>()
                        var roof = 0
                        for (i in snapshot.children) {
                            list.add(i.getValue(ItemRvChatting::class.java)!!)
                            for (i in 0 until viewModel.mutableCurChannelUserList.size) {
                                if (viewModel.mutableCurChannelUserList[i].Id == list[roof].SenderId) {
                                    Log.d(
                                        "----",
                                        "onDataChange: IMG : ${viewModel.mutableCurChannelUserList[i].PhotoUrl.toString()}"
                                    )
                                    list[roof].profileUrl =
                                        viewModel.mutableCurChannelUserList[i].PhotoUrl.toString()
                                    list[roof].NickName =
                                        viewModel.mutableCurChannelUserList[i].NickName.toString()
                                    break
                                }
                            }
                            viewModel.mutableChatItemList = list
                            Log.d("****", "onDataChange: $list")
                            viewModel.setChatList()
                            roof++
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        })
        viewModel.chatItemList.observe(requireActivity(), Observer {
            channelAdapter.setData(it)
        })
    }

    private fun setDmUi() {
        binding.rvChat.hasFixedSize()
        binding.rvChat.adapter = dmAdapter
    }
    private fun setChannelUi() {
        binding.rvChat.hasFixedSize()
        binding.rvChat.adapter = channelAdapter
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_dm_menu -> {
                viewModel.pagerPosition.value = 0
            }

            R.id.icon_dm_group -> {
                viewModel.pagerPosition.value = 2
            }
        }
    }

    override fun onKey(view: View?, keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_ENTER -> {
                if (event?.action == ACTION_UP) {
                    if (viewModel.currentTab.value?.isDm == true) {
                        var msgMap = mutableMapOf<String, Any>()
                        msgMap["SenderId"] = viewModel.curUserData.value?.Id.toString()
                        msgMap["SendTime"] =
                            SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(System.currentTimeMillis())
                        msgMap["Content"] = binding.edtBottomMsg.text.toString()

                        var userMap = mutableMapOf<String, Any>()
                        userMap["${viewModel.curUserData.value?.Id}"] = true



                        if (binding.edtBottomMsg.text.isNotBlank()) {
                            FirebaseDatabase.getInstance()
                                .getReference("DMList")
                                .child("${viewModel.curUserData.value?.Id} - ${viewModel.currentDmFriend.value?.Id}")
                                .push()
                                .setValue(msgMap)
                                .addOnSuccessListener {
                                    binding.edtBottomMsg.setText("")
                                }
                        }
                    } else {
                        var msgMap = mutableMapOf<String, Any>()
                        msgMap["SenderId"] = viewModel.curUserData.value?.Id.toString()
                        msgMap["SendTime"] =
                            SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(System.currentTimeMillis())
                        msgMap["Content"] = binding.edtBottomMsg.text.toString()

                        var userMap = mutableMapOf<String, Any>()
                        userMap["${viewModel.curUserData.value?.Id}"] = true



                        if (binding.edtBottomMsg.text.isNotBlank()) {
                            FirebaseDatabase.getInstance()
                                .getReference(viewModel.currentTab.value?.chatroomName!!)
                                .child("${viewModel.currentChatChannel.value?.channelName}")
                                .push()
                                .setValue(msgMap)
                                .addOnSuccessListener {
                                    binding.edtBottomMsg.setText("")
                                }
                        }
                    }
                }
            }
        }
        return true
    }

}