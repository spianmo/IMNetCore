package com.teamhelper.imsdk


object EventRegistry {
    private val subscribers = mutableListOf<Any>()

    fun register(subscriber: Any) {
        subscribers.add(subscriber)
    }

    fun unregister(subscriber: Any) {
        subscribers.remove(subscriber)
    }

    fun post(event: Any) {
        subscribers.forEach { subscriber ->
            val methods = subscriber.javaClass.declaredMethods
            methods.forEach { method ->
                val annotationBusinessEvent = method.getAnnotation(BusinessEvent::class.java)
                if (annotationBusinessEvent != null) {
                    if (method.parameterCount == 1 && method.parameterTypes[0].isAssignableFrom(
                            event.javaClass
                        )
                    ) {
                        method.invoke(subscriber, event)
                    }
                }
                val annotationServerEvent = method.getAnnotation(ServerEvent::class.java)
                if (annotationServerEvent != null) {
                    if (method.parameterCount == 1 && method.parameterTypes[0].isAssignableFrom(
                            event.javaClass
                        )
                    ) {
                        method.invoke(subscriber, event)
                    }
                }
            }
        }
    }
}
