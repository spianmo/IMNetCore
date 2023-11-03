package com.teamhelper.imsdk

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.teamhelper.imsdk.base.EventSubscriber
import com.teamhelper.imsdk.base.ServerEvent
import com.teamhelper.imsdk.base.ServerEventType
import com.teamhelper.imsdk.databinding.ActivityMainBinding

@EventSubscriber
class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @ServerEvent(ServerEventType.onConnectClosed)
    fun onConnectClosed() {
        Log.e("SecondActivity", "onConnectClosed")
    }

    @ServerEvent(ServerEventType.onReconnect)
    fun onReconnect(retryCnt: Int, delay: Int) {
        Log.e("SecondActivity", "onReconnect")
    }
}