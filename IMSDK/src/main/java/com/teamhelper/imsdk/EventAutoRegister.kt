package com.teamhelper.imsdk

import android.content.Context
import com.highcapable.yukireflection.factory.searchClass
import com.teamhelper.imsdk.base.EventRegistry
import com.teamhelper.imsdk.base.EventSubscriber
import de.robv.android.xposed.DexposedBridge
import de.robv.android.xposed.XC_MethodHook


class EventAutoRegister {
    companion object {
        fun register(subscriber: Any) {
            val subscriberClass = subscriber.javaClass
            if (subscriberClass.isAnnotationPresent(EventSubscriber::class.java)) {
                EventRegistry.register(subscriber)
            }
        }

        fun unregister(subscriber: Any) {
            val subscriberClass = subscriber.javaClass
            if (subscriberClass.isAnnotationPresent(EventSubscriber::class.java)) {
                EventRegistry.unregister(subscriber)
            }
        }

        fun autoRegisterAllSubscribers(context: Context) {
            Companion::class.java.classLoader?.searchClass(context) {
                from(context.packageName)
                method {
                    name = "onCreate"
                }.count(num = 1)
                method {
                    name = "onDestroy"
                }.count(num = 1)
            }?.all()?.stream()?.distinct()?.filter {
                it.isAnnotationPresent(EventSubscriber::class.java)
            }?.forEach { clazz ->
                DexposedBridge.hookAllConstructors(
                    clazz,
                    object : XC_MethodHook() {
                        @Throws(Throwable::class)
                        override fun afterHookedMethod(param: MethodHookParam) {
                            super.afterHookedMethod(param)
                            register(param.thisObject)
                        }
                    })

                DexposedBridge.hookAllMethods(
                    clazz,
                    "onDestroy",
                    object : XC_MethodHook() {
                        @Throws(Throwable::class)
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            super.beforeHookedMethod(param)
                            unregister(param.thisObject)
                        }
                    })
            }
        }
    }
}
