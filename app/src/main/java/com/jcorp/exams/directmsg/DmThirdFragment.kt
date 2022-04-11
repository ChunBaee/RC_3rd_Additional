package com.jcorp.exams.directmsg

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.firebase.firestore.FirebaseFirestore
import com.jcorp.exams.databinding.FragmentDmThirdBinding
import com.jcorp.exams.directmsg.adapter.DmFriendAdapter
import com.jcorp.exams.directmsg.data.DmFriendData
import com.jcorp.exams.mViewModel

class DmThirdFragment : Fragment() {

    private lateinit var binding : FragmentDmThirdBinding
    private val viewModel by activityViewModels<mViewModel>()
    private lateinit var dmAdapter : DmFriendAdapter
    private lateinit var channelAdapter : DmFriendAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDmThirdBinding.inflate(inflater, container, false)


        observe()

        return binding.root
    }

    private fun observe() {

        viewModel.currentTab.observe(requireActivity(), Observer {
            when(it.isDm) {
                true -> {
                    DMObserve()
                    setDMUi()
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
            binding.toolbarName.text = it.NickName

            val list = mutableListOf<DmFriendData>()
            list.add(DmFriendData("", viewModel.currentDmFriend.value?.NickName, viewModel.currentDmFriend.value?.PhotoUrl, "", ""))
            list.add(DmFriendData("", viewModel.curUserData.value?.NickName, viewModel.curUserData.value?.PhotoUrl, "", ""))
            dmAdapter.setData(list)
        })
    }

    private fun ChannelObserve() {
        viewModel.currentChatChannel.observe(requireActivity(), Observer {
            binding.toolbarName.setText("${it.channelName}")
        })

        val list = mutableListOf<DmFriendData>()
        viewModel.currentChannelUserId.observe(requireActivity(), Observer {
            FirebaseFirestore.getInstance()
                .collection("UserData")
                .document("$it.com")
                .get()
                .addOnSuccessListener { data ->
                    if(list.contains(data.toObject(DmFriendData::class.java))) {
                    } else {
                        list.add(data.toObject(DmFriendData::class.java)!!)
                    }
                    viewModel.mutableCurChannelUserList = list
                    Log.d("----", "ChannelObserve: $list")
                    viewModel.setChannelChatUserList()
                }
        })

        viewModel.curChannelUserList.observe(requireActivity(), Observer {
            //Log.d("----", "ChannelObserve: ${viewModel.mutableCurChannelUserList}")
            binding.txtMember.setText("멤버 ㅡ ${viewModel.mutableCurChannelUserList.size}")
            channelAdapter.setData(it)
        })
    }

    private fun setDMUi() {
        binding.txtMember.setText("멤버 ㅡ 2")
        binding.rvMember.hasFixedSize()
        dmAdapter = DmFriendAdapter(requireActivity())

        binding.rvMember.adapter = dmAdapter
    }

    private fun setChannelUi() {
        binding.rvMember.hasFixedSize()
        channelAdapter = DmFriendAdapter(requireActivity())

        binding.txtInvite.setText("멤버 초대하기")

        binding.rvMember.adapter = channelAdapter

    }
}