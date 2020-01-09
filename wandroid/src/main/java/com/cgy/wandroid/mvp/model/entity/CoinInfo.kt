package com.cgy.wandroid.mvp.model.entity

import java.io.Serializable

/**
 * @author: cgy
 * @date 2020/1/9 14:55
 * @description: 分享人信息
 */
data class CoinInfo (var coinCount : Int, var rank : Int, var userId : Int, var username:String) : Serializable