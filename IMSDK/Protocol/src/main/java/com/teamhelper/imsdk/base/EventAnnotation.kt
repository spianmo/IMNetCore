package com.teamhelper.imsdk.base


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class EventSubscriber

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class BusinessEvent(val event: BusinessEventType)

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ServerEvent(val event: ServerEventType)


enum class BusinessEventType {
    onUserLogin, onUserKickOut, onEchoReceived, onHeartbeat, onErrorReceived, onTokenExpired, onTokenRefresh, onCommonDataReceived
}

enum class ServerEventType {
    onConnectOpen, onTextMessageRecv, onBinaryMessageRecv, onConnectClosed, onReconnect
}