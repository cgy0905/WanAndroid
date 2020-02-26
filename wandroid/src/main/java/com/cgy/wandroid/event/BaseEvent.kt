package com.cgy.wandroid.event

import org.greenrobot.eventbus.EventBus

open class BaseEvent {
    fun post() {
        EventBus.getDefault().post(this)
    }

}
