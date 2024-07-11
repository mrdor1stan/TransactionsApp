package com.example.transactionsapp

import android.app.Application
import com.example.transactionsapp.di.AppContainer
import com.example.transactionsapp.di.DefaultAppContainer

class TransactionsApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
