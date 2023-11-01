package com.teamhelper.imsdk

import org.reflections.Reflections

class EventAutoRegister {
    companion object {
        private fun register(subscriber: Any) {
            val subscriberClass = subscriber.javaClass
            if (subscriberClass.isAnnotationPresent(EventSubscriber::class.java)) {
                EventRegistry.register(subscriber)
            }
        }

        private fun unregister(subscriber: Any) {
            EventRegistry.unregister(subscriber)
        }

        fun autoRegisterAllSubscribers(basePackage: String) {
            val reflections = Reflections(basePackage)
            val subscriberClasses = reflections.getTypesAnnotatedWith(EventSubscriber::class.java)
            for (subscriberClass in subscriberClasses) {
                try {
                    val constructor = subscriberClass.getConstructor()
                    val subscriber = constructor.newInstance()
                    register(subscriber)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
