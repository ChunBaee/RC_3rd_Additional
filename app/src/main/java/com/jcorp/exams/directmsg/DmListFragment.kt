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
import com.jcorp.exams.databinding.ItemDirectMessageBinding
import com.jcorp.exams.directmsg.adapter.DmFriendAdapter
import com.jcorp.exams.directmsg.data.DmFriendData
import com.jcorp.exams.mViewModel

class DmListFragment : Fragment() {
    private val viewModel by activityViewModels<mViewModel>()
    private lateinit var binding : ItemDirectMessageBinding
    private lateinit var adapter : DmFriendAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ItemDirectMessageBinding.inflate(inflater, container, false)

        setUi()
        getData()
        observe()

        return binding.root
    }

    private fun observe() {
        viewModel.dmStrList.observe(requireActivity(), Observer {
            Log.d("----", "observe: ${viewModel.dmStrList.value?.size}")
            var roof = 0
            for(i in 0 until it.size) {
                FirebaseFirestore.getInstance().collection("UserData")
                    .document(it[i])
                    .get()
                    .addOnSuccessListener { data ->
                        if(viewModel.mutableDmList.size == it.size) {
                            data.toObject(DmFriendData::class.java)?.let { it1 -> viewModel.mutableDmList.set(roof, it1) }
                        } else {
                            data.toObject(DmFriendData::class.java)?.let { it1 -> viewModel.mutableDmList.add(it1) }
                        }
                        roof++
                        viewModel.setDmList()
                    }
            }
        })

        viewModel.dmList.observe(requireActivity(), Observer {
            adapter.setData(it)
        })
    }

    private fun setUi() {
        binding.dmTitle.text = viewModel.currentTab.value?.chatroomName
        adapter = DmFriendAdapter(requireActivity())
        binding.rvDmFriendList.hasFixedSize()
        binding.rvDmFriendList.adapter = adapter

        adapter.clickListener(object : DmFriendAdapter.ClickListener {
            override fun onClick(view: View, position: Int) {
                viewModel.changeNowClick2(position)
                viewModel.pagerPosition.value = 1
            }
        })

    }

    private fun getData() {
        var roof = 0
        FirebaseFirestore.getInstance().collection("DmListData")
            .get()
            .addOnSuccessListener {
                for(i in it) {
                    if(viewModel.mutableDmStrList.size == it.size()) {
                        viewModel.mutableDmStrList[roof] = i.id
                    } else {
                        viewModel.mutableDmStrList.add(i.id)
                    }
                    roof++
                }
                viewModel.setDmStrList()
            }
    }
}