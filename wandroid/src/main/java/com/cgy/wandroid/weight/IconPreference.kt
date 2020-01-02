package com.cgy.wandroid.weight

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.cgy.wandroid.R
import com.cgy.wandroid.util.SettingUtil

/**
 * @author: cgy
 * @date 2020/1/2 13:54
 * @description:
 */
class IconPreference(context: Context, attrs : AttributeSet) : Preference(context, attrs) {
    var circleImageView : MyColorCircleView? = null

    init {
        widgetLayoutResource = R.layout.item_icon_preference_preview
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        val color = SettingUtil.getColor(context)
        circleImageView = holder?.itemView?.findViewById(R.id.iv_preview)
        circleImageView?.color = color
        circleImageView?.border = color
    }

    override fun setViewId(viewId: Int) {
        val color = SettingUtil.getColor(context)
        circleImageView?.color = color
        circleImageView?.border = color
    }
}