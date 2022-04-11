package com.jcorp.exams.channel.data

import com.jcorp.exams.directmsg.data.DmFriendData

data class ChannelSeveralData(
    var channelTag : Int? = 0,
    var channelName: String? = "",
    var member: MutableList<DmFriendData>?,
    var NowClick : String? = "F"
)
