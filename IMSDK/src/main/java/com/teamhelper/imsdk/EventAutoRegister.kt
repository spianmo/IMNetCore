package com.teamhelper.imsdk

import android.content.Context
import com.highcapable.yukireflection.factory.searchClass
import com.teamhelper.imsdk.base.EventRegistry
import com.teamhelper.imsdk.base.EventSubscriber

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

        fun autoRegisterAllSubscribers(context: Context) {
            Companion::class.java.classLoader?.searchClass(context, async = true) {
                from(context.packageName)
                simpleName = "MainActivity"
            }?.waitAll { classes ->
                for (clazz in classes) {
                    try {
                        val constructor = clazz.getConstructor()
                        val subscriber = constructor.newInstance()
                        register(subscriber)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}
