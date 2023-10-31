package com.teamhelper.imsdk

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.teamhelper.imsdk.databinding.ActivityMainBinding
import com.teamhelper.imsdk.netcore.NetCore
import com.teamhelper.imsdk.netcore.NetEventRegistry
import com.teamhelper.imsdk.netcore.data.AckDataContent
import com.teamhelper.imsdk.netcore.data.CommonDataContent
import com.teamhelper.imsdk.netcore.data.ErrorDataContent
import com.teamhelper.imsdk.netcore.data.KickOutDataContent
import com.teamhelper.imsdk.netcore.data.LoginResultDataContent
import com.teamhelper.imsdk.netcore.event.ServerEventListener
import com.teamhelper.imsdk.netcore.protocol.Protocol

class MainActivity : AppCompatActivity(), ServerEventListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener {
            NetCore.instance.connect()
            Handler(mainLooper).postDelayed({
                NetCore.instance.sendTextMessage("Hello World")
            }, 5000)
        }

        NetEventRegistry.addServerEventListener(this)
    }

    override fun onUserLogin(p: Protocol<LoginResultDataContent>) {
        Log.e("MainActivity", "onUserLoginEvent")
    }

    override fun onUserKickOut(p: Protocol<KickOutDataContent>) {
        Log.e("MainActivity", "onUserKickOutEvent" + p.dataContent?.reason)
    }

    override fun onCommonDataReceived(p: ByteArray) {
        Log.e("MainActivity", "onCommonDataReceived")
    }

    override fun <T> onCommonDataReceived(p: Protocol<CommonDataContent<T>>) {
        Log.e("MainActivity", "onCommonDataReceived")
    }


    override fun onHeartbeat(p: Protocol<String>) {
        Log.e("MainActivity", "onHeartbeat")
    }

    override fun onErrorReceived(p: Protocol<ErrorDataContent>) {
        Log.e("MainActivity", "onErrorReceived")
    }

    override fun onAckReceived(p: Protocol<AckDataContent>) {
        Log.e("MainActivity", "onAckReceived")
    }
}