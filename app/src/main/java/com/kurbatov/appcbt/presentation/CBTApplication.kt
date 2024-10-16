package com.kurbatov.appcbt.presentation

import android.app.Application
import com.kurbatov.appcbt.di.DaggerApplicationComponent

class CBTApplication : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}