package com.teamhelper.imsdk

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.teamhelper.imsdk.data.LoginResultDataContent
import com.teamhelper.imsdk.databinding.ActivityMainBinding
import com.teamhelper.imsdk.event.BusinessEventListener
import com.teamhelper.imsdk.netcore.NetCore
import com.teamhelper.imsdk.netcore.ServerEventRegistry
import com.teamhelper.imsdk.netcore.event.ServerEventListener
import com.teamhelper.imsdk.protocol.Protocol

class MainActivity : AppCompatActivity(), ServerEventListener,
    BusinessEventListener {

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

        ServerEventRegistry.addServerEventListener(this)
        BusinessEventRegistry.addBusinessEventListener(this)
    }

    override fun onUserLogin(p: Protocol<LoginResultDataContent>) {
        Log.e("MainActivity", "onUserLoginEvent")
    }

    override fun onUserKickOut(p: Protocol<com.teamhelper.imsdk.data.KickOutDataContent>) {
        Log.e("MainActivity", "onUserKickOutEvent" + p.dataContent?.reason)
    }

    override fun onCommonDataReceived(p: ByteArray) {
        Log.e("MainActivity", "onCommonDataReceived")
    }

    override fun <T> onCommonDataReceived(p: Protocol<com.teamhelper.imsdk.data.CommonDataContent<T>>) {
        Log.e("MainActivity", "onCommonDataReceived")
    }


    override fun onHeartbeat(p: Protocol<String>) {
        Log.e("MainActivity", "onHeartbeat")
    }

    override fun onErrorReceived(p: Protocol<com.teamhelper.imsdk.data.ErrorDataContent>) {
        Log.e("MainActivity", "onErrorReceived")
    }

    override fun onAckReceived(p: Protocol<com.teamhelper.imsdk.data.AckDataContent>) {
        Log.e("MainActivity", "onAckReceived")
    }

    override fun onConnectOpen(response: String) {
        Log.e("MainActivity", "onConnectOpen")
    }

    override fun onTextMessageRecv(message: String) {
        Log.e("MainActivity", "onTextMessageRecv")
    }

    override fun onBinaryMessageRecv(binary: ByteArray) {
        Log.e("MainActivity", "onBinaryMessageRecv")
    }

    override fun onConnectClosed() {
        Log.e("MainActivity", "onConnectClosed")
    }

    override fun onReconnect(retryCnt: Int, delay: Int) {
        Log.e("MainActivity", "onReconnect")
    }
}