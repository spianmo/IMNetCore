package com.teamhelper.imsdk

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cn.teamhelper.signal.protocol.ProtocolProto
import cn.teamhelper.signal.protocol.ProtocolProto.Protocol
import com.teamhelper.imsdk.base.EventSubscriber
import com.teamhelper.imsdk.base.ServerEvent
import com.teamhelper.imsdk.base.ServerEventType
import com.teamhelper.imsdk.databinding.ActivityMainBinding
import com.teamhelper.imsdk.netcore.NetCore
import com.teamhelper.imsdk.protocol.ProtocolType

@EventSubscriber
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.fab.setOnClickListener {
            NetCore.instance.connect(wsUrl = "ws://192.168.3.55:9904")
            NetCore.instance.sendBinaryMessage(Protocol.newBuilder().apply {
                type = ProtocolType.C.FROM_CLIENT_TYPE_OF_LOGIN
                platform = ProtocolProto.Platform.ANDROID
            }.build().toByteArray())
        }
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