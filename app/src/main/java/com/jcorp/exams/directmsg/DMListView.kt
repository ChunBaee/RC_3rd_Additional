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
import com.jcorp.exams.R
import com.jcorp.exams.channel.ChatroomListFragment
import com.jcorp.exams.databinding.ViewDmListBinding
import com.jcorp.exams.directmsg.adapter.DmSideAdapter
import com.jcorp.exams.directmsg.data.ItemDmSideList
import com.jcorp.exams.mViewModel


class DMListView : Fragment() {
    private lateinit var binding : ViewDmListBinding
    private val viewModel by activityViewModels<mViewModel>()
    private lateinit var adapter : DmSideAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ViewDmListBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = requireActivity()


        adapter = DmSideAdapter(requireActivity())

        getData()
        setUi()
        observe()

        return binding.root
    }

    private fun observe() {
        viewModel.channelList.observe(requireActivity(), Observer {
            adapter.setData(it)
        })
        viewModel.currentTab.observe(requireActivity(), Observer {
            //binding.dmInfoTitle.text = it.chatroomName
            when(it.isDm) {
                true -> {
                    requireActivity().supportFragmentManager.beginTransaction().replace(R.id.dmList_container, DmListFragment()).commit()
                }
                else -> {
                    requireActivity().supportFragmentManager.beginTransaction().replace(R.id.dmList_container, ChatroomListFragment()).commit()
                }
            }
        })
    }

    private fun getData() {
        FirebaseFirestore.getInstance().collection("ChannelData")
            .get()
            .addOnSuccessListener {
                Log.d("----", "getData: ${it.toObjects(ItemDmSideList::class.java)}")
                viewModel.setBasicThings()
                for(i in it) {
                    viewModel.mutableChannelList.add(viewModel.mutableChannelList.size-2, i.toObject(ItemDmSideList::class.java))
                }
                //viewModel.mutableChannelList = it.toObjects(ItemDmList::class.java)

                viewModel.setChannelList()
            }
    }

    private fun setUi() {
        binding.sideDmList.adapter = adapter
        binding.sideDmList.hasFixedSize()

        adapter.clickListener(object : DmSideAdapter.ClickListener {
            override fun onClick(view: View, position: Int) {
                viewModel.changeNowClick(position)
            }
        })
    }
}