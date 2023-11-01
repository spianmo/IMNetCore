package com.teamhelper.imsdk

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.teamhelper.imsdk.base.BusinessEvent
import com.teamhelper.imsdk.base.BusinessEventType
import com.teamhelper.imsdk.base.EventSubscriber
import com.teamhelper.imsdk.base.ServerEvent
import com.teamhelper.imsdk.base.ServerEventType
import com.teamhelper.imsdk.data.AckDataContent
import com.teamhelper.imsdk.data.CommonDataContent
import com.teamhelper.imsdk.data.ErrorDataContent
import com.teamhelper.imsdk.data.KickOutDataContent
import com.teamhelper.imsdk.data.LoginResultDataContent
import com.teamhelper.imsdk.databinding.ActivityMainBinding
import com.teamhelper.imsdk.netcore.NetCore
import com.teamhelper.imsdk.protocol.Protocol

@EventSubscriber
class MainActivity : AppCompatActivity() {

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
    }

    @BusinessEvent(BusinessEventType.onUserLogin)
    fun onUserLogin(p: Protocol<LoginResultDataContent>) {
        Log.e("MainActivity", "onUserLoginEvent")
    }

    @BusinessEvent(BusinessEventType.onUserKickOut)
    fun onUserKickOut(p: Protocol<KickOutDataContent>) {
        Log.e("MainActivity", "onUserKickOutEvent" + p.dataContent?.reason)
    }

    @BusinessEvent(BusinessEventType.onCommonDataReceived)
    fun onCommonDataReceived(p: ByteArray) {
        Log.e("MainActivity", "onCommonDataReceived")
    }

    @BusinessEvent(BusinessEventType.onCommonDataReceived)
    fun <T> onCommonDataReceived(p: Protocol<CommonDataContent<T>>) {
        Log.e("MainActivity", "onCommonDataReceived")
    }

    @BusinessEvent(BusinessEventType.onHeartbeat)
    fun onHeartbeat(p: Protocol<String>) {
        Log.e("MainActivity", "onHeartbeat")
    }

    @BusinessEvent(BusinessEventType.onErrorReceived)
    fun onErrorReceived(p: Protocol<ErrorDataContent>) {
        Log.e("MainActivity", "onErrorReceived")
    }

    @BusinessEvent(BusinessEventType.onAckReceived)
    fun onAckReceived(p: Protocol<AckDataContent>) {
        Log.e("MainActivity", "onAckReceived")
    }

    @ServerEvent(ServerEventType.onConnectOpen)
    fun onConnectOpen(response: String) {
        Log.e("MainActivity", "onConnectOpen")
    }

    @ServerEvent(ServerEventType.onTextMessageRecv)
    fun onTextMessageRecv(message: String) {
        Log.e("MainActivity", "onTextMessageRecv")
    }

    @ServerEvent(ServerEventType.onBinaryMessageRecv)
    fun onBinaryMessageRecv(binary: ByteArray) {
        Log.e("MainActivity", "onBinaryMessageRecv")
    }

    @ServerEvent(ServerEventType.onConnectClosed)
    fun onConnectClosed() {
        Log.e("MainActivity", "onConnectClosed")
    }

    @ServerEvent(ServerEventType.onReconnect)
    fun onReconnect(retryCnt: Int, delay: Int) {
        Log.e("MainActivity", "onReconnect")
    }
}