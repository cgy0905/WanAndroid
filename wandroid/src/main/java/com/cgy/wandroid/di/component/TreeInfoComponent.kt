package com.cgy.wandroid.di.component

import com.cgy.wandroid.di.module.TreeInfoModule
import com.cgy.wandroid.ui.main.tree.treeinfo.TreeInfoFragment
import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.FragmentScope
import dagger.Component


@FragmentScope
@Component(modules = arrayOf(TreeInfoModule::class), dependencies = arrayOf(AppComponent::class))
interface TreeInfoComponent {
    fun inject(fragment: TreeInfoFragment)
}
