package com.cgy.wandroid.weight.loadCallback


import com.cgy.wandroid.R
import com.kingja.loadsir.callback.Callback


/**
 * Description:TODO
 * Create Time:2017/9/4 10:20
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */

class ErrorCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_error
    }
}
