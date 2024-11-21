package com.example.repocollector

import android.app.Application
import android.util.Log
import com.facebook.FacebookSdk
import com.google.firebase.FirebaseApp


class RepoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        FacebookSdk.sdkInitialize(this)
    }
}
