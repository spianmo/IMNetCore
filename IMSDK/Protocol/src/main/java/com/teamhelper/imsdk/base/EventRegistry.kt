package com.teamhelper.imsdk.base


object EventRegistry {
    private val subscribers = mutableListOf<Any>()

    fun register(subscriber: Any) {
        subscribers.add(subscriber)
    }

    fun unregister(subscriber: Any) {
        subscribers.remove(subscriber)
    }

    fun post(event: BusinessEventType, vararg args: Any) {
        subscribers.forEach { subscriber ->
            val methods = subscriber.javaClass.declaredMethods
            methods.forEach { method ->
                method.getAnnotation(BusinessEvent::class.java)?.let {
                    if (event == it.event) {
                        method.invoke(subscriber, *args)
                    }
                }
            }
        }
    }

    fun post(event: ServerEventType, vararg args: Any) {
        subscribers.forEach { subscriber ->
            val methods = subscriber.javaClass.declaredMethods
            methods.forEach { method ->
                method.getAnnotation(ServerEvent::class.java)?.let {
                    if (event == it.event) {
                        method.invoke(subscriber, *args)
                    }
                }
            }
        }
    }
}
