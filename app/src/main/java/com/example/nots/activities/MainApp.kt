package com.example.nots.activities

import android.app.Application
import com.example.nots.db.MainDatabase

class MainApp: Application() {
    val database by lazy { MainDatabase.getDatabase(this) }
}