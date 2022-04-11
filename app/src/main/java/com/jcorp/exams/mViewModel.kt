package com.jcorp.exams

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jcorp.exams.channel.data.ChannelSeveralData
import com.jcorp.exams.directmsg.data.DmFriendData
import com.jcorp.exams.directmsg.data.ItemDmSideList
import com.jcorp.exams.directmsg.data.ItemRvChatting

class mViewModel : ViewModel() {
    var userEmail : String = ""

    private val _curUserData = MutableLiveData<CurUserData>()
    val curUserData : LiveData<CurUserData> = _curUserData

    var mutableChannelList = mutableListOf<ItemDmSideList>()
    private val _channelList = MutableLiveData<MutableList<ItemDmSideList>>()
    val channelList : LiveData<MutableList<ItemDmSideList>> = _channelList

    var mutableDmStrList = mutableListOf<String>()
    private val _dmStrList = MutableLiveData<MutableList<String>>()
    val dmStrList : LiveData<MutableList<String>> = _dmStrList

    var mutableDmList = mutableListOf<DmFriendData>()
    private val _dmList = MutableLiveData<MutableList<DmFriendData>>()
    val dmList : LiveData<MutableList<DmFriendData>> = _dmList

    var mutableChatItemList = mutableListOf<ItemRvChatting>()
    private val _chatItemList = MutableLiveData<MutableList<ItemRvChatting>>()
    val chatItemList : LiveData<MutableList<ItemRvChatting>> = _chatItemList

    var mutableChannelChatList = mutableListOf<ChannelSeveralData>()
    private val _channelChatList = MutableLiveData<MutableList<ChannelSeveralData>>()
    val channelChatList : LiveData<MutableList<ChannelSeveralData>> = _channelChatList

    var mutableChannelVoiceList = mutableListOf<ChannelSeveralData>()
    private val _channelVoiceList = MutableLiveData<MutableList<ChannelSeveralData>>()
    val channelVoiceList : LiveData<MutableList<ChannelSeveralData>> = _channelVoiceList

    var mutableCurChannelUserList = mutableListOf<DmFriendData>()
    private val _curChannelUserList = MutableLiveData<MutableList<DmFriendData>>()
    val curChannelUserList : LiveData<MutableList<DmFriendData>> = _curChannelUserList

    val currentTab = MutableLiveData<ItemDmSideList>()
    val currentDmFriend = MutableLiveData<DmFriendData>()
    val currentChatChannel = MutableLiveData<ChannelSeveralData>()
    val currentChannelUserId = MutableLiveData<String>()

    val shouldHideBottomView = MutableLiveData<Boolean>(false)

    val pagerPosition = MutableLiveData<Int>(0)


    fun setCurUserData (data : CurUserData) {
        _curUserData.value = data
    }

    fun setBasicThings() {
        mutableChannelList.add(0,ItemDmSideList(R.drawable.icon_message.toString(), "다이렉트 메시지", "T", "F", true))
        mutableChannelList.add(mutableChannelList.size,ItemDmSideList(R.drawable.icon_plus.toString(), "", "F", "F"))
        mutableChannelList.add(mutableChannelList.size,ItemDmSideList(R.drawable.icon_organization_chart.toString(), "", "F", "F"))

        currentTab.value = mutableChannelList[0]
    }

    fun setChannelList () {
        _channelList.value = mutableChannelList
    }

    fun setDmStrList() {
        _dmStrList.value = mutableDmStrList
    }

    fun setDmList() {
        _dmList.value = mutableDmList
    }

    fun setChatList() {
        _chatItemList.value = mutableChatItemList
    }

    fun setChannelChatList() {
        _channelChatList.value = mutableChannelChatList
    }

    fun setChannelVoiceList() {
        _channelVoiceList.value = mutableChannelVoiceList
    }

    fun setChannelChatUserList() {
        _curChannelUserList.value = mutableCurChannelUserList
    }

    fun changeNowClick(position : Int) {
        for(i in 0 until mutableChannelList.size) {
            mutableChannelList[i].NowClick = "F"
        }
        mutableChannelList[position].NowClick = "T"
        currentTab.value = mutableChannelList[position]
        setChannelList()
    }

    fun changeNowClick2(position : Int) {
        for(i in 0 until mutableDmList.size) {
            mutableDmList[i].NowClick = "F"
        }
        mutableDmList[position].NowClick = "T"
        currentDmFriend.value = mutableDmList[position]
        setDmList()
    }

    fun changeNowClick3(position : Int) {
        for(i in 0 until mutableChannelChatList.size) {
            mutableChannelChatList[i].NowClick = "F"
        }
        mutableChannelChatList[position].NowClick = "T"
        //currentDmFriend.value = mutableDmList[position]
        setChannelChatList()
    }

    fun changeBottomViewState() {
        when(pagerPosition.value) {
            0, 2 -> {
                shouldHideBottomView.value = false
            }
            1 -> {
                shouldHideBottomView.value = true
            }
        }
        Log.d("----", "changeBottomViewState: ${shouldHideBottomView.value}")
    }

}