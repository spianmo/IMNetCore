package com.teamhelper.imsdk

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.teamhelper.imsdk.base.EventSubscriber
import com.teamhelper.imsdk.base.ServerEvent
import com.teamhelper.imsdk.base.ServerEventType
import com.teamhelper.imsdk.databinding.ActivityMainBinding
import com.teamhelper.imsdk.netcore.NetCore

@EventSubscriber
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        TestSubscriber()

        binding.fab.setOnClickListener {
            NetCore.instance.connect()
            Handler(mainLooper).postDelayed({
                NetCore.instance.sendTextMessage("Hello World")
            }, 10000)
        }
    }

    @ServerEvent(ServerEventType.onReconnect)
    fun onReconnect(retryCnt: Int, delay: Int) {
        Log.e("MainActivity", "onReconnect")
    }
}