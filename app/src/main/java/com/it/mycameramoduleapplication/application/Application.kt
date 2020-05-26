package com.it.mycameramoduleapplication.application


import android.app.Application
import android.content.Context


class Application : Application() {

    var mContext: Context? = null


    companion object AppContext {
        lateinit var instance: Application
        fun getContext(): Context {
            return instance
        }
    }

    init {
        instance = this
    }


    override fun onCreate() {
        super.onCreate()

        mContext = applicationContext

    }


}
