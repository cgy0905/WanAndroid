package com.cgy.wandroid.ui.setting

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.cgy.wandroid.R
import com.cgy.wandroid.event.LoginFreshEvent
import com.cgy.wandroid.ui.login.LoginActivity
import com.cgy.wandroid.util.CacheUtil
import com.cgy.wandroid.weight.IconPreference
import com.jess.arms.integration.AppManager

/**
 * @author: cgy
 * @date 2020/1/2 13:42
 * @description:
 */
class GeneralPreferenceFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var colorPreview: IconPreference? = null
    private lateinit var parentActivity: SettingActivity


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.root_preferences)
        parentActivity = activity as SettingActivity
        colorPreview = findPreference("color")
        setText()
        findPreference<Preference>("exit")?.isVisible = CacheUtil.isLogin()//未登录时,退出登录需要隐藏
        findPreference<Preference>("exit")?.setOnPreferenceClickListener { preference ->
            MaterialDialog(parentActivity).show {
                title(R.string.title)
                message(text = "确定退出登录吗？")
                        .positiveButton(text = "退出") {
                            LoginFreshEvent(false, arrayListOf()).post()
                            CacheUtil.setUser(null)
                            AppManager.getAppManager().startActivity(Intent(parentActivity, LoginActivity::class.java))
                            parentActivity.finish()
                        }
                negativeButton(R.string.cancel)
            }
            false
        }
    }

    private fun setText() {

    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}