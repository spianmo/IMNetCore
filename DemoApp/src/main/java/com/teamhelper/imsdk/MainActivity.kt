package com.teamhelper.imsdk

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cn.teamhelper.signal.protocol.ProtocolProto
import cn.teamhelper.signal.protocol.ProtocolProto.Protocol
import com.google.gson.Gson
import com.teamhelper.imsdk.base.EventSubscriber
import com.teamhelper.imsdk.base.ServerEvent
import com.teamhelper.imsdk.base.ServerEventType
import com.teamhelper.imsdk.data.LoginDataContent
import com.teamhelper.imsdk.databinding.ActivityMainBinding
import com.teamhelper.imsdk.netcore.NetCore
import com.teamhelper.imsdk.netcore.event.ServerEventListener
import java.util.UUID

@EventSubscriber
class MainActivity : AppCompatActivity(), ServerEventListener {

    private lateinit var binding: ActivityMainBinding
    val socketClientList = mutableListOf<NetCore>()
    val startTimestampMap = mutableMapOf<NetCore, Long>()
    val endTimestampMap = mutableMapOf<NetCore, Long>()
    val diffTimestampMap = mutableMapOf<NetCore, Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        for (i in 0..1000) {
            val socketClient = NetCore()
            socketClientList.add(socketClient)
        }
        startTimestampMap[socketClientList[0]] = System.currentTimeMillis()
        socketClientList[0].connect(NetCore.Companion.SocketProtocol.TCP, "101.34.86.110", 9998, false)
        binding.fab.setOnClickListener {
            socketClientList[0].sendBinaryMessage(
                Protocol.newBuilder()
                    .setType(0)
                    .setFrom("1")
                    .setPlatform(ProtocolProto.Platform.ANDROID)
                    .setTo("0")
                    .setToPlatform(ProtocolProto.Platform.SERVER)
                    .setFp(UUID.randomUUID().toString())
                    .setDataContent(
                        Gson().toJson(
                            LoginDataContent(
                                loginUserId = "1",
                                loginToken = "123456",
                                timestamp = System.currentTimeMillis() + 1000000000,
                                extra = "",
                                firstLoginTime = 0
                            )
                        )
                    )
                    .setTypeu(-1)
                    .build().toByteArray()
            )
        }
    }

    @ServerEvent(ServerEventType.onConnectOpen)
    override fun onConnectOpen(client: NetCore, response: String) {
        Log.e("MainActivity", "onConnectOpen")
        endTimestampMap[client] = System.currentTimeMillis()

        Log.e("==========>", "endTimestampMap Size: ${endTimestampMap.size}")
        if (endTimestampMap.size == 1000) {
            //计算 最小连接耗时(ms)、最大连接耗时(ms)、总连接耗时(ms)、平均连接耗时(ms)
            endTimestampMap.forEach { (key, value) ->
                diffTimestampMap[key] = value - startTimestampMap[key]!!
            }
            val min = diffTimestampMap.values.minOrNull()
            val max = diffTimestampMap.values.maxOrNull()
            val total = diffTimestampMap.values.sum()
            val average = total / diffTimestampMap.size
            Log.e("==========>", "min: $min, max: $max, total: $total, average: $average")
            //打印出所有的value
            Log.e("==========>", diffTimestampMap.values.toString())
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                startTimestampMap[socketClientList[endTimestampMap.size]] = System.currentTimeMillis()
                socketClientList[endTimestampMap.size].connect(
                    NetCore.Companion.SocketProtocol.TCP,
                    "101.34.86.110",
                    9998,
                    false
                )

            }, 100)
        }
    }

    @ServerEvent(ServerEventType.onTextMessageRecv)
    override fun onTextMessageRecv(client: NetCore, message: String) {
        Log.e("MainActivity", "onTextMessageRecv")
    }

    @ServerEvent(ServerEventType.onBinaryMessageRecv)
    override fun onBinaryMessageRecv(client: NetCore, binary: ByteArray) {
        Log.e("MainActivity", "onBinaryMessageRecv")
    }

    @ServerEvent(ServerEventType.onConnectClosed)
    override fun onConnectClosed(client: NetCore, code: Int, reason: String) {
        Log.e("MainActivity", "onConnectClosed: $code $reason")
    }

    @ServerEvent(ServerEventType.onReconnect)
    override fun onReconnect(client: NetCore, retryCnt: Int, delay: Int) {
        Log.e("MainActivity", "onReconnect")
    }
}