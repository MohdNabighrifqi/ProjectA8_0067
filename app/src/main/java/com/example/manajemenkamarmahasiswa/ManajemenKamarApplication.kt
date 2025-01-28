package com.example.manajemenkamarmahasiswa

import android.app.Application
import com.example.manajemenkamarmahasiswa.di.AppContainer
import com.example.manajemenkamarmahasiswa.di.ManajemenKamarContainer

class ManajemenKamarApplication : Application(){
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = ManajemenKamarContainer()
    }
}